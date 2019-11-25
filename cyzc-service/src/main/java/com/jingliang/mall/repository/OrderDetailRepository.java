package com.jingliang.mall.repository;

import com.jingliang.mall.repository.base.BaseRepository;
import com.jingliang.mall.entity.OrderDetail;

import java.util.List;

/**
 * 订单详情表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 09:10:24
 */
public interface OrderDetailRepository extends BaseRepository<OrderDetail, Long> {
    /**
     * 根据订单Id查询订单详情列表
     *
     * @param orderId     订单Id
     * @param isAvailable 是否可用
     * @return 返回查询到的订单详情
     */
    List<OrderDetail> findAllByOrderIdAndIsAvailable(Long orderId, Boolean isAvailable);

}