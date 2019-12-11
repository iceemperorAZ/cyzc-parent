package com.jingliang.mall.service;

import com.jingliang.mall.entity.OfflinePayment;

/**
 * 线下支付Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-06 15:30:50
 */
public interface OfflinePaymentService {

    /**
     * 保存线下支付凭证
     *
     * @param offlinePayment 线下支付凭证
     * @return 返回保存后的线下支付凭证
     */
    OfflinePayment save(OfflinePayment offlinePayment);

    /**
     * 根据订单编号查询支付凭证
     *
     * @param orderId 订单编号
     * @return 返回查询到的支付凭证
     */
    OfflinePayment findByOrderId(Long orderId);

    /**
     * 根据Id查询支付凭证
     * @param id 主键Id
     * @return 返回查询到的支付凭证
     */
    OfflinePayment findAllById(Long id);
}