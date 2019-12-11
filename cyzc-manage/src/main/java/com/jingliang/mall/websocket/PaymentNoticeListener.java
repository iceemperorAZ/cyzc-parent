package com.jingliang.mall.websocket;

import com.jingliang.mall.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * 订单支付通知消息监听器
 *
 * @author Zhenfeng Li
 * @date 2019-12-09 20:30:58
 */
@Component
@Slf4j
public class PaymentNoticeListener {
    /**
     * 订单支付通知消费者
     */
    @RabbitListener(queues = {"${rabbitmq.payment.notice.queue}"}, containerFactory = "singleListenerContainer")
    public void orderProcess(@Payload Order order) {
        log.debug("订单支付通知消费者-消费消息,{}", order);
        WebSocketServer.broadCastInfo(order);
    }
}
