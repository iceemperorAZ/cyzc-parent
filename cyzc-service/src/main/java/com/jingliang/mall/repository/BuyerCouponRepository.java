package com.jingliang.mall.repository;

import com.jingliang.mall.entity.BuyerCoupon;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;

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
     * @param buyerId     会员Id
     * @param isAvailable 是否可用
     * @return 返回查询到的优惠券集合
     */
    List<BuyerCoupon> findAllByBuyerIdAndIsAvailable(Long buyerId, Boolean isAvailable);

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

    /**
     * 根据过期时间查询商户拥有的优惠券
     *
     * @param buyerId
     * @param dateTime
     * @return
     */
    @Query(value = "SELECT * FROM tb_buyer_coupon WHERE is_available=1 AND buyer_id=:buyerId " +
            "AND DATE_FORMAT(expiration_time,'%Y-%m')=DATE_FORMAT(:dateTime,'%Y-%m')", nativeQuery = true)
    List<BuyerCoupon> findAllbyBuyerIdAndTime(Long buyerId, Date dateTime);

    /**
     * 根据商户id，优惠券id，时间查询优惠券是否过期
     *
     * @param buyerId
     * @param couponId
     * @param dateTime
     * @return
     */
    @Query(value = "SELECT * FROM tb_buyer_coupon WHERE is_available=1 AND buyer_id=:buyerId AND coupon_id=:couponId  " +
            "AND DATE_FORMAT(expiration_time,'%Y-%m')=DATE_FORMAT(:dateTime,'%Y-%m')", nativeQuery = true)
    BuyerCoupon findbyBuyerIdAndCouponIdAndTime(Long buyerId,Long couponId, Date dateTime);

    /**
     * 根据商品分类id，满减金额，过期时间查询商户已有的优惠券
     *
     * @param productTypeId
     * @param buyerId
     * @param dateTime
     * @return
     */
    @Query(value = "SELECT * FROM tb_buyer_coupon WHERE is_available=1 AND product_type_id=:productTypeId AND buyer_id=:buyerId " +
            "AND DATE_FORMAT(expiration_time,'%Y-%m')=DATE_FORMAT(:dateTime,'%Y-%m')", nativeQuery = true)
    List<BuyerCoupon> findAllToUseCoupon(Long productTypeId, Long buyerId, Date dateTime);
}