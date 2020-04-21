package com.jingliang.mall.task;

import com.jingliang.mall.entity.Order;
import com.jingliang.mall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 订单自动确认定时任务
 *
 * @author Zhenfeng Li
 * @date 2020-03-30 15:45:47
 */
@Slf4j
@Component
@EnableScheduling
public class Task {
    @Value("${default.redis.key}")
    String defaultPrefix;

    private final RedisTemplate<String, Object> redisTemplate;
    private final OrderService orderService;

    public Task(RedisTemplate<String, Object> redisTemplate, OrderService orderService) {
        this.redisTemplate = redisTemplate;
        this.orderService = orderService;
    }

    /**
     * 订单自动确认定时任务
     * 每天00:00:00查询出发货三天还没有的确认收货的单子，执行自动确认
     */
    @Scheduled(cron = "0 0 0 * * ? ")
    public void orderAutoConfirm() {
        LocalDate now = LocalDate.now();
        Long increment = redisTemplate.opsForValue().increment(defaultPrefix + "orderAutoConfirm" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now));
        log.info("---------------{}", increment);
        if (increment == null || increment == 1) {
            //订单状态 100:待支付 200:已取消 300:已支付/待发货,400:已发货/待收货,500:已送达/待确认,600:已完成，700:已退货（不扣绩效），800:已退货（扣绩效）
            Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态300-600表示已经支付
                predicateList.add(cb.between(root.get("orderStatus"), 400, 500));
                //三天前已经发货的进行自动确认
                Calendar instance = Calendar.getInstance();
                instance.add(Calendar.DATE, -3);
                predicateList.add(cb.lessThan(root.get("updateTime"), instance.getTime()));
                query.where(cb.and(predicateList.toArray(new Predicate[0])));
                return query.getRestriction();
            };
            List<Order> orders = orderService.findAll(orderSpecification);
            orders.forEach(order -> {
                orderService.update(new Order().setId(order.getId()).setOrderStatus(600));
            });
        }
    }
}
