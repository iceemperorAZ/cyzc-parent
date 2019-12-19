package com.jingliang.mall.service;

import com.jingliang.mall.entity.BuyerCouponLimit;

import java.util.List;

/**
 * 用户优惠券使用限制Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-19 09:17:25
 */
public interface BuyerCouponLimitService {

    /**
     * 保存
     *
     * @param buyerCouponLimit
     * @return
     */
    BuyerCouponLimit save(BuyerCouponLimit buyerCouponLimit);

    /**
     * 根据商户Id查询
     *
     * @param buyerId
     * @return
     */
    List<BuyerCouponLimit> findAllByBuyerId(Long buyerId);

    /**
     * 根据用户Id和商品分类Id查询
     * @param buyerId
     * @param productTypeId
     * @return
     */
    BuyerCouponLimit findByBuyerIdAndProductTypeId(Long buyerId, Long productTypeId);

    /**
     * 根据用户Id和商品分类Id集合查询
     *
     * @param buyerId
     * @param productTypeIds
     * @return
     */
    List<BuyerCouponLimit> findAllByBuyerIdAndProductTypeIds(Long buyerId, List<Long> productTypeIds);
}