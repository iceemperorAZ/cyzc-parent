package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Coupon;
import com.jingliang.mall.repository.CouponRepository;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 优惠券ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-25 19:09:02
 */
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final RedisService redisService;

    public CouponServiceImpl(CouponRepository couponRepository, RedisService redisService) {
        this.couponRepository = couponRepository;
        this.redisService = redisService;
    }

    @Override
    public Coupon save(Coupon coupon) {
        coupon = couponRepository.save(coupon);
        //如果为发布状态则放入redis中
        if (coupon.getIsRelease()) {
            redisService.couponIncrement(coupon.getId() + "", coupon.getResidueNumber());
        }
        return coupon;
    }

    @Override
    public Page<Coupon> findAll(Specification<Coupon> couponSpecification, PageRequest pageRequest) {
        return couponRepository.findAll(couponSpecification, pageRequest);
    }

    @Override
    public Coupon findById(Long couponId) {
        return couponRepository.findAllByIdAndIsAvailable(couponId,true);
    }

    @Override
    public List<Coupon> saveAll(List<Coupon> coupons) {
        return couponRepository.saveAll(coupons);
    }


    @Override
    public List<Coupon> findAllByProductZoneId(Long productZoneId) {
        return couponRepository.findAllByProductZoneIdAndIsAvailable(productZoneId, true);
    }

    @Override
    public List<Coupon> findAllByCouponType(Integer couponType) {
        Date date = new Date();
        return couponRepository.findAllByCouponTypeAndStartTimeGreaterThanEqualAndExpirationTimeLessThanEqualAndIsAvailable(couponType, date, date, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCouponById(Coupon coupon) {
        Integer residueNumber = coupon.getResidueNumber();
        coupon = couponRepository.findAllByIdAndIsAvailable(coupon.getId(), true);
        coupon.setResidueNumber(coupon.getResidueNumber() + residueNumber);
        couponRepository.save(coupon);
    }

    @Override
    public Coupon delete(Coupon coupon) {
        coupon = couponRepository.save(coupon);
        redisService.removeCoupon(coupon.getId() + "");
        return coupon;
    }

    @Override
    public List<Coupon> findAll(Specification<Coupon> couponSpecification) {
        return couponRepository.findAll(couponSpecification);
    }
}