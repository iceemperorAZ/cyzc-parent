package com.jingliang.mall.controller;

import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 首页数据Controller
 *
 * @author Zhenfeng Li
 * @date 2020-01-02 14:36:40
 */
@RestController
@Api(tags = "首页数据")
public class IndexController {
    private final UserService userService;
    private final BuyerService buyerService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final WechatManageService wechatManageService;

    public IndexController(UserService userService, BuyerService buyerService, OrderService orderService, OrderDetailService orderDetailService, WechatManageService wechatManageService) {
        this.userService = userService;
        this.buyerService = buyerService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.wechatManageService = wechatManageService;
    }

    /**
     * 查询近[type]天的成交额 -1:全部，几天就传几 ,0:表示当天
     */
    @GetMapping("/turnover")
    @ApiOperation(value = "查询近[type]天的成交额 -1:全部，几天就传几")
    public Result<Double> turnover(Integer type) {
        if (type == null) {
            return Result.buildParamFail();
        }
        Date startTime = null;
        Date endTime = null;
        if (type > -1) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            endTime = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, -type);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            startTime = calendar.getTime();
        }
        long totalPrice = 0;
        //查询会员在指定时间的所有已经完成支付的订单
        Date finalStartTime = startTime;
        Date finalEndTime = endTime;
        Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            //订单状态300-600表示已经支付
            predicateList.add(cb.between(root.get("orderStatus"), 300, 600));
            if (finalStartTime != null) {
                predicateList.add(cb.between(root.get("payEndTime"), finalStartTime, finalEndTime));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            return query.getRestriction();
        };
        List<Order> orders = orderService.findAll(orderSpecification);
        if (orders.size() > 0) {
            for (Order order : orders) {
                //统计所有订单价格
                totalPrice += order.getPayableFee();
            }
        }
        return Result.buildQueryOk((totalPrice * 1.00) / 100);
    }


    //查询近7/15/30/90/180天的成交额排名前10的商品


    //查询近7/15/30/90/180天的成交额排名前10的销售


    public static void main(String[] args) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_YEAR, -180);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));
    }

}
