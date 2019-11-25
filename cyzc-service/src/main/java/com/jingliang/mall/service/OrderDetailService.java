package com.jingliang.mall.service;

import com.jingliang.mall.entity.OrderDetail;

import java.util.List;

/**
 * 订单详情表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 09:10:24
 */
public interface OrderDetailService {

    /**
     * 根据订单Id查询订单详情列表
     *
     * @param orderId 订单Id
     * @return 返回查询到的订单详情
     */
    List<OrderDetail> findByOrderId(Long orderId);
}