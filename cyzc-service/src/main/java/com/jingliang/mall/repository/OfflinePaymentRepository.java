package com.jingliang.mall.repository;


import com.jingliang.mall.entity.OfflinePayment;
import com.jingliang.mall.repository.base.BaseRepository;

/**
 * 线下支付Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-06 15:30:50
 */
public interface OfflinePaymentRepository extends BaseRepository<OfflinePayment, Long> {
    /**
     * 根据订单编号和可用性查询支付凭证
     *
     * @param orderId     订单编号
     * @param isAvailable 是否可用
     * @return 返回查询到的支付凭证
     */
    OfflinePayment findFirstByOrderIdAndIsAvailable(Long orderId, Boolean isAvailable);
}