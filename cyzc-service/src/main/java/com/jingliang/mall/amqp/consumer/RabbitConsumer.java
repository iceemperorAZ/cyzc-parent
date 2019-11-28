package com.jingliang.mall.amqp.consumer;

import com.jingliang.mall.entity.Coupon;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.entity.Sku;
import com.jingliang.mall.service.CouponService;
import com.jingliang.mall.service.OrderService;
import com.jingliang.mall.service.SkuService;
import com.jingliang.mall.wx.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 库存消费者
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-10-14 10:25
 */
@Component
@Slf4j
public class RabbitConsumer {
    private final SkuService skuService;
    private final OrderService orderService;
    private final WechatService wechatService;
    private final CouponService couponService;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public RabbitConsumer(SkuService skuService, OrderService orderService, WechatService wechatService, CouponService couponService) {
        this.skuService = skuService;
        this.orderService = orderService;
        this.wechatService = wechatService;
        this.couponService = couponService;
    }

    /**
     * 库存消费者
     */
    @RabbitListener(queues = {"${rabbitmq.sku.queue}"}, containerFactory = "singleListenerContainer")
    public void skuProcess(@Payload Sku sku) {
        //sku.getSkuLineNum()加为正数，减为负数
        log.debug("接收到库存消息，进行更新操作{}", sku);
        skuService.updateSkuByProductId(sku);
    }

    /**
     * 用户下单成功后超时未支付-监听者
     */
    @RabbitListener(queues = {"${rabbitmq.order.pay.dead.real.queue}"}, containerFactory = "singleListenerContainer")
    public void consumeExpireOrder(@Payload Order order) {
        try {
            log.debug("用户下单成功后超时未支付-监听者-接收消息:{}", order);
            if (order != null) {
                Date date = new Date();
                log.debug("{}", order);
                Long orderId = order.getId();
                //判断订单状态，如果为待支付则恢复库存，否则什么也不做
                order = orderService.findById(orderId);
                log.debug("支付过期的订单Id为：" + orderId);
                if (order.getOrderStatus() > 100) {
                    log.info("Id为[{}]的订单已经完成支付或取消支付,状态码[{}]", orderId, order.getOrderStatus());
                    return;
                }
                //通过调用微信订单查询接口，确认是否未支付
                Map<String, String> map = wechatService.payOrderQuery(order.getOrderNo());
                if (StringUtils.equals(map.get("trade_state"), "SUCCESS")) {
                    //如果支付成功但订单状态未修改，则修改订单状态为已支付
                    order.setOrderStatus(300);
                    String timeEnd = map.get("time_end");
                    try {
                        order.setPayEndTime(dateFormat.parse(timeEnd));
                    } catch (ParseException e) {
                        order.setPayEndTime(date);
                    }
                    order.setPayNo(map.get("transaction_id"));
                    order.setUpdateTime(date);
                    orderService.update(order);
                    return;
                }
                //修改订单状态为已取消
                order.setOrderStatus(200);
                order.setFinishTime(date);
                order.setUpdateTime(date);
                //修改订单状态为已取消
                log.info("Id为[{}]的订单，支付超时，系统自动取消,已恢复库存数量", order.getId());
                orderService.update(order);
            }
        } catch (Exception e) {
            log.error("用户下单成功后超时未支付-监听者-发生异常：", e.fillInStackTrace());
        }
    }

    /**
     * 优惠券消费者
     */
    @RabbitListener(queues = {"${rabbitmq.coupon.queue}"}, containerFactory = "singleListenerContainer")
    public void couponProcess(@Payload Coupon coupon) {
        //加为正数，减为负数
        log.debug("接收到优惠券消息，进行更新操作{}", coupon);
        couponService.updateCouponById(coupon);
    }
}
