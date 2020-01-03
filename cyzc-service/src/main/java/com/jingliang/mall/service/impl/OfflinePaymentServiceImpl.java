package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.OfflinePayment;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.repository.BuyerRepository;
import com.jingliang.mall.repository.OfflinePaymentRepository;
import com.jingliang.mall.repository.OrderRepository;
import com.jingliang.mall.service.OfflinePaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
    private final BuyerRepository buyerRepository;

    public OfflinePaymentServiceImpl(OfflinePaymentRepository offlinePaymentRepository, OrderRepository orderRepository, BuyerRepository buyerRepository) {
        this.offlinePaymentRepository = offlinePaymentRepository;
        this.orderRepository = orderRepository;
        this.buyerRepository = buyerRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OfflinePayment save(OfflinePayment offlinePayment) {
        //如果是创建则修改订单否则不修改订单
        Order order = orderRepository.getOne(offlinePayment.getOrderId());
        Date date = new Date();
        order.setPayEndTime(date);
        order.setOrderStatus(300);
        order.setUpdateTime(date);
        order.setUpdateUserId(offlinePayment.getUpdateUserId());
        order.setUpdateUserName(offlinePayment.getUpdateUserName());
        orderRepository.save(order);
        if (offlinePayment.getId() == null) {
            //更新用户最后一次下单时间
            Buyer buyer = buyerRepository.findAllByIdAndIsAvailable(order.getBuyerId(), true);
            buyer.setLastOrderTime(date);
            buyerRepository.save(buyer);
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