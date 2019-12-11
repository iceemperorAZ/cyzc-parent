package com.jingliang.mall.amqp.producer;

import com.jingliang.mall.entity.Coupon;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.entity.Sku;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 库存生产者
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-10-14 10:23
 */
@Component
@Slf4j
public class RabbitProducer {
    private final RabbitTemplate rabbitTemplate;
    private final ConfigurableEnvironment env;

    public RabbitProducer(RabbitTemplate rabbitTemplate, ConfigurableEnvironment env) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        this.env = env;
    }

    /**
     * 库存用于单生产者-》单消费者
     */
    public void sendSku(Sku sku) {
        log.debug("rabbitmq发送库存消息:{}", sku);
        rabbitTemplate.convertAndSend(env.getProperty("rabbitmq.basic.exchange"), env.getProperty("rabbitmq.sku.routing.key"), sku);
    }

    /**
     * 订单用于单生产者-》单消费者
     */
    public void sendOrderExpireMsg(Order order) {
        log.debug("rabbitmq用户下单成功后超时未支付-生产者");
        try {
            if (order != null) {
                rabbitTemplate.setExchange(env.getProperty("rabbitmq.basic.exchange"));
                rabbitTemplate.setRoutingKey(Objects.requireNonNull(env.getProperty("rabbitmq.order.pay.dead.prod.routing.key")));
                rabbitTemplate.convertAndSend(order, message -> {
                    MessageProperties mp = message.getMessageProperties();
                    mp.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    mp.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, Order.class);
                    mp.setExpiration(env.getProperty("pay.overtime"));
                    return message;
                });
            }
        } catch (Exception e) {
            log.error("户下单成功后-发送信息入死信队列，等待着一定时间失效超时未支付的订单-发生异常，消息为：{}", order, e.fillInStackTrace());
        }
    }

    /**
     * 优惠券
     */
    public void sendCoupon(Coupon coupon) {
        log.debug("rabbitmq发送优惠券消息");
        this.rabbitTemplate.convertAndSend(env.getProperty("rabbitmq.basic.exchange"), env.getProperty("rabbitmq.coupon.routing.key"), coupon);
    }

    /**
     * 以广播的形式发送订单支付通知
     */
    public void paymentNotice(Order order){
        log.debug("rabbitmq以广播的形式发送有用户支付订单的消息");
        this.rabbitTemplate.convertAndSend(env.getProperty("rabbitmq.payment.notice.exchange"), "", order);
    }
}
