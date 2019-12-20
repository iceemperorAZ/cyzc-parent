package com.jingliang.mall.amqp.producer;

import com.jingliang.mall.entity.Coupon;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.entity.Sku;
import com.jingliang.mall.server.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
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
    private final EmailService emailService;
    @Value("${server.domain}")
    private String serverDomain;


    public RabbitProducer(RabbitTemplate rabbitTemplate, ConfigurableEnvironment env, EmailService emailService) {
        this.rabbitTemplate = rabbitTemplate;
        this.emailService = emailService;
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
    public void paymentNotice(Order order) {
        log.debug("rabbitmq以广播的形式发送有用户支付订单的消息");
        this.rabbitTemplate.convertAndSend(env.getProperty("rabbitmq.payment.notice.exchange"), "", order);
        emailService.sendHtmlMail(env.getProperty("mail.to"), "订单支付成功", "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
                "        \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"zh\">\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                "    <title></title>\n" +
                "</head>\n" +
                "<body style=\"position: sticky\">\n" +
                "<div style=\"width:350px;margin: 0 auto\">\n" +
                "    <h3 style=\"color:green;text-align: center\">支付成功 √</h3>\n" +
                "    <span style=\"float: left;width: 130px\">订单号:</span><span style=\"color:red\">" + order.getOrderNo() + "</span>\n" +
                "    <br/>\n" +
                "    <br/>\n" +
                "    <span style=\"float: left;width: 130px\">总金额:</span><span style=\"color:red;\">￥" + order.getTotalPrice() * 1.0 / 100 + "</span>\n" +
                "    <br/>\n" +
                "    <span style=\"float: left;width: 130px\">优惠金额:</span><span style=\"color:red;\">￥-" + order.getPreferentialFee() * 1.0 / 100 + "</span>\n" +
                "    <br/>\n" +
                "    <span style=\"float: left;width: 130px\">支付金额:</span><span style=\"color:red;\">￥" + order.getPayableFee() * 1.0 / 100 + "</span>\n" +
                "    <br/>\n" +
                "    <br/>\n" +
                "    <span style=\"color: green;float: right;font-weight: bold\">请登录<a style=\"color: blue\" href=" + serverDomain + " target=\"_blank\">管理系统</a>安排发货</span>\n" +
                "    <br/>\n" +
                "    <br/>\n" +
                "    <span>系统邮件请勿回复</span>\n" +
                "    <br/>\n" +
                "    <br/>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>");
    }
}
