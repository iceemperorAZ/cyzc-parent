package com.jingliang.mall.service;

import com.jingliang.mall.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * 优惠券Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-25 19:09:02
 */
public interface CouponService {
    /**
     * 保存优惠券
     *
     * @param coupon 优惠券对象
     * @return 返回保存后的优惠券
     */
    Coupon save(Coupon coupon);

    /**
     * 分页查询全部优惠券
     *
     * @param couponSpecification 查询条件
     * @param pageRequest         分页条件
     * @return 返回查询到的优惠券集合
     */
    Page<Coupon> findAll(Specification<Coupon> couponSpecification, PageRequest pageRequest);

    /**
     * 根据主键获取优惠券信息
     *
     * @param couponId 主键Id
     * @return 返回查询到的优惠券信息
     */
    Coupon findById(Long couponId);

    /**
     * 保存优惠券列表
     *
     * @param coupons 优惠券列表
     * @return 返回保存后的优惠券
     */
    List<Coupon> saveAll(List<Coupon> coupons);

    /**
     * 查询在使用时间内的优惠券
     *
     * @param couponId 优惠券Id
     * @return 返回查询到的优惠券信息
     */
    Coupon findAllById(Long couponId);

    /**
     * 根据商品区Id查询优惠券列表
     *
     * @param productZoneId 商品区
     * @return 返回查询到的优惠券列表
     */
    List<Coupon> findAllByProductZoneId(Long productZoneId);

    /**
     * 根据优惠券类型查询优惠券列表
     *
     * @param couponType 优惠券类型
     * @return 返回查询到的优惠券列表
     */
    List<Coupon> findAllByCouponType(Integer couponType);

    /**
     * 更新优惠券数量
     *
     * @param coupon 优惠券对象
     */
    void updateCouponById(Coupon coupon);

    /**
     * 删除优惠券
     *
     * @param coupon
     * @return
     */
    Coupon delete(Coupon coupon);
}