package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.OrderDetail;
import com.jingliang.mall.repository.OrderDetailRepository;
import com.jingliang.mall.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单详情表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 09:10:24
 */
@Service
@Slf4j
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findAllByOrderIdAndIsAvailable(orderId, true);
    }
}