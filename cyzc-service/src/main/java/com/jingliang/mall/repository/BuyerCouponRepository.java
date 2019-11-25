package com.jingliang.mall.repository;

import com.jingliang.mall.repository.base.BaseRepository;
import com.jingliang.mall.entity.BuyerCoupon;

import java.util.Date;
import java.util.List;

/**
 * 用户优惠券Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 15:21:18
 */
public interface BuyerCouponRepository extends BaseRepository<BuyerCoupon, Long> {
    /**
     * 查询所有可使用的优惠券
     *
     * @param buyerId        会员Id
     * @param startTime      开始时间
     * @param expirationTime 结束时间
     * @param isAvailable    是否可用
     * @return 返回查询到的优惠券集合
     */
    List<BuyerCoupon> findAllByBuyerIdAndStartTimeLessThanEqualAndExpirationTimeGreaterThanEqualAndIsAvailable(Long buyerId, Date startTime, Date expirationTime, Boolean isAvailable);

    /**
     * 根据优惠券Id查询优惠券是否已经领取
     *
     * @param buyerId  会员Id
     * @param couponId 优惠券Id
     * @return 返回统计的数量
     */
    Integer countAllByBuyerIdAndCouponId(Long buyerId, Long couponId);

    /**
     * 根据优惠券Id查询优惠券是否已经领取
     *
     * @param id          主键Id
     * @param buyerId     会员Id
     * @param isAvailable 是否可用
     * @return 返回统计的数量
     */
    BuyerCoupon findFirstByIdAndBuyerIdAndIsAvailable(Long id, Long buyerId, Boolean isAvailable);

    /**
     * 根据优惠券Id集合统计数量
     *
     * @param buyerId 会员Id
     * @param ids     优惠券Id集合
     * @return 返回统计的数量
     */
    Integer countAllByBuyerIdAndCouponIdIn(Long buyerId, List<Long> ids);
}