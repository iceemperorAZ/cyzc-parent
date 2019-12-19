package com.jingliang.mall.repository;

import com.jingliang.mall.entity.BuyerCouponLimit;
import com.jingliang.mall.repository.base.BaseRepository;

import java.util.List;

/**
 * 用户优惠券使用限制Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-19 09:17:25
 */
public interface BuyerCouponLimitRepository extends BaseRepository<BuyerCouponLimit, Object> {

    /**
     * 根据商户Id查询
     *
     * @param buyerId     商户id
     * @param isAvailable 是否可用
     * @return
     */
    List<BuyerCouponLimit> findAllByBuyerIdAndIsAvailable(Long buyerId, Boolean isAvailable);

    /**
     * 根据商户Id和商品分类Id查询
     *
     * @param buyerId       商户id
     * @param productTypeId 商品分类Id
     * @param isAvailable   是否可用
     * @return
     */
    BuyerCouponLimit findFirstByBuyerIdAndProductTypeIdAndIsAvailable(Long buyerId, Long productTypeId, boolean isAvailable);

    /**
     * 根据商户Id和商品分类Id查询
     *
     * @param buyerId        商户id
     * @param productTypeIds 商品分类Id集合
     * @param isAvailable    是否可用
     * @return
     */
    List<BuyerCouponLimit> findAllByBuyerIdAndProductTypeIdInAndIsAvailable(Long buyerId, List<Long> productTypeIds, boolean isAvailable);
}