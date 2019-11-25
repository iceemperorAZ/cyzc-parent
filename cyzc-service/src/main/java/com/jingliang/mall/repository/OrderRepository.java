package com.jingliang.mall.repository;

import com.jingliang.mall.repository.base.BaseRepository;
import com.jingliang.mall.entity.Order;

/**
 * 订单表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
public interface OrderRepository extends BaseRepository<Order, Long> {
    /**
     * 根据订单Id和会员Id查询订单信息
     *
     * @param id      主键Id
     * @param buyerId 会员Id
     * @return 返回查询到的订单信息
     */
    Order findFirstByIdAndBuyerId(Long id, Long buyerId);

    /**
     * 根据订单编号查询订单信息
     *
     * @param orderNo 订单编号
     * @return 返回查询到的订单信息
     */
    Order findFirstByOrderNo(String orderNo);

}