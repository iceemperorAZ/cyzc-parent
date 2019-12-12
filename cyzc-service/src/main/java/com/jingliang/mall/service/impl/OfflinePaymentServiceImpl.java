package com.jingliang.mall.service.impl;

import com.jingliang.mall.amqp.producer.RabbitProducer;
import com.jingliang.mall.entity.OfflinePayment;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.repository.OfflinePaymentRepository;
import com.jingliang.mall.repository.OrderRepository;
import com.jingliang.mall.service.OfflinePaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * 线下支付ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-06 15:30:50
 */
@Service
@Slf4j
public class OfflinePaymentServiceImpl implements OfflinePaymentService {

    private final OfflinePaymentRepository offlinePaymentRepository;
    private final OrderRepository orderRepository;

    public OfflinePaymentServiceImpl(OfflinePaymentRepository offlinePaymentRepository, OrderRepository orderRepository) {
        this.offlinePaymentRepository = offlinePaymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OfflinePayment save(OfflinePayment offlinePayment) {
        //如果是创建则修改订单否则不修改订单
        if (Objects.isNull(offlinePayment.getId())) {
            Order order = orderRepository.getOne(offlinePayment.getOrderId());
            order.setPayEndTime(new Date());
            order.setOrderStatus(300);
            order.setUpdateTime(new Date());
            orderRepository.save(order);
        }
        return offlinePaymentRepository.save(offlinePayment);
    }

    @Override
    public OfflinePayment findByOrderId(Long orderId) {
        return offlinePaymentRepository.findFirstByOrderIdAndIsAvailable(orderId, true);
    }

    @Override
    public OfflinePayment findAllById(Long id) {
        return offlinePaymentRepository.findAllByIdAndIsAvailable(id, true);
    }
}