package com.jingliang.mall.service;

import com.jingliang.mall.entity.BuyerCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * 用户优惠券Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 15:21:18
 */
public interface BuyerCouponService {

    /**
     * 保存用户优惠券
     *
     * @param buyerCoupon 永辉优惠券
     * @return 返回保存后的用户优惠券
     */
    BuyerCoupon save(BuyerCoupon buyerCoupon);

    /**
     * 分页查询所有领取优惠券
     *
     * @param buyerCouponSpecification 查询条件
     * @param pageRequest              分页条件
     * @return 返回查询到的优惠券信息
     */
    Page<BuyerCoupon> findAll(Specification<BuyerCoupon> buyerCouponSpecification, PageRequest pageRequest);

    /**
     * 查询所有可使用的优惠券
     *
     * @param buyerId 会员Id
     * @return 返回查询到的优惠券信息
     */
    List<BuyerCoupon> findAll(Long buyerId);

    /**
     * 根据优惠券Id查询优惠券是否已经领取
     *
     * @param buyerId  会员Id
     * @param couponId 优惠券Id
     * @return 返回查询到的优惠券数量
     */
    Integer countByCouponId(Long buyerId, Long couponId);

    /**
     * 根据用户优惠券Id查询优惠券
     *
     * @param id      主键Id
     * @param buyerId 用户Id
     * @return 返回查询到的优惠券
     */
    BuyerCoupon findByIdAndBuyerId(Long id, Long buyerId);

    /**
     * 根据优惠券Id集合统计数量
     *
     * @param buyerId   会员Id
     * @param couponIds 优惠券Id集合
     * @return 返回统计数量
     */
    Integer countAllByBuyerIdAndCouponIds(Long buyerId, List<Long> couponIds);
}