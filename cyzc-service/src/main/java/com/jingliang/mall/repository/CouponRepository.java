package com.jingliang.mall.repository;

import com.jingliang.mall.entity.Coupon;
import com.jingliang.mall.repository.base.BaseRepository;

import java.util.Date;
import java.util.List;

/**
 * 优惠券Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-25 19:09:02
 */
public interface CouponRepository extends BaseRepository<Coupon, Long> {

    /**
     * 根据主键Id、开始时间、过期时间、可用性查询优惠券
     *
     * @param id             主键Id
     * @param startTime      开始时间
     * @param expirationTime 过期时间
     * @param isAvailable    可用性
     * @return 返回查询到的优惠券信息
     */
    Coupon findByIdAndStartTimeGreaterThanEqualAndExpirationTimeLessThanEqualAndIsAvailable(Long id, Date startTime, Date expirationTime, Boolean isAvailable);

    /**
     * 根据优惠券区Id查询优惠券列表
     *
     * @param productZoneId 优惠券区Id
     * @param isAvailable   是否可用
     * @return 返回查询到优惠券列表
     */
    List<Coupon> findAllByProductZoneIdAndIsAvailable(Long productZoneId, Boolean isAvailable);

    /**
     * 根据优惠券类型查询优惠券列表
     *
     * @param couponType     优惠券类型
     * @param startTime      开始时间
     * @param expirationTime 过期时间
     * @param isAvailable    是否可用
     * @return 返回查询到的优惠券列表
     */
    List<Coupon> findAllByCouponTypeAndStartTimeGreaterThanEqualAndExpirationTimeLessThanEqualAndIsAvailable(Integer couponType, Date startTime, Date expirationTime, Boolean isAvailable);
}