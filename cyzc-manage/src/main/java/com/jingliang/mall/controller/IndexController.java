package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallConstant;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.service.*;
import io.swagger.annotations.Api;
import org.springframework.data.jpa.domain.Specification;
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
     * 查询近7/15/30/90/180天的成交额
     * 查询总成交额
     * -1:全部，0:7天，1:15天，2:30天，3:90天，4:180天
     */

    public MallResult<Double> turnover(Integer type) {
        if (type == null) {
            return MallResult.buildParamFail();
        }
        Date startTime = null;
        Date endTime = null;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        switch (type) {
            case -1:
                break;
            case 0:
                endTime = calendar.getTime();
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                startTime = calendar.getTime();
                break;
            case 1:
                endTime = calendar.getTime();
                calendar.add(Calendar.DAY_OF_YEAR, -15);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                startTime = calendar.getTime();
                break;
            case 2:
                endTime = calendar.getTime();
                calendar.add(Calendar.DAY_OF_YEAR, -30);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                startTime = calendar.getTime();
                break;
            case 3:
                endTime = calendar.getTime();
                calendar.add(Calendar.DAY_OF_YEAR, -90);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                startTime = calendar.getTime();
                break;
            case 4:
                endTime = calendar.getTime();
                calendar.add(Calendar.DAY_OF_YEAR, -180);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                startTime = calendar.getTime();
                break;
            default:
                return MallResult.build(MallConstant.FAIL, MallConstant.TEXT_PARAM_VALUE_FAIL);
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
        return null;
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
