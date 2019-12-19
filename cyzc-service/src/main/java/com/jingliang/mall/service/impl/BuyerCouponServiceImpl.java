package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.BuyerCoupon;
import com.jingliang.mall.repository.BuyerCouponRepository;
import com.jingliang.mall.service.BuyerCouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户优惠券ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 15:21:18
 */
@Service
@Slf4j
public class BuyerCouponServiceImpl implements BuyerCouponService {

    private final BuyerCouponRepository buyerCouponRepository;

    public BuyerCouponServiceImpl(BuyerCouponRepository buyerCouponRepository) {
        this.buyerCouponRepository = buyerCouponRepository;
    }

    @Override
    public BuyerCoupon save(BuyerCoupon buyerCoupon) {
        return buyerCouponRepository.save(buyerCoupon);
    }

    @Override
    public Page<BuyerCoupon> findAll(Specification<BuyerCoupon> buyerCouponSpecification, PageRequest pageRequest) {
        return buyerCouponRepository.findAll(buyerCouponSpecification, pageRequest);
    }

    @Override
    public List<BuyerCoupon> findAll(Long buyerId) {
        Date date = new Date();
        return buyerCouponRepository.findAllByBuyerIdAndStartTimeLessThanEqualAndExpirationTimeGreaterThanEqualAndIsAvailable(buyerId, date, date, true);
    }

    @Override
    public Integer countByCouponId(Long buyerId, Long couponId) {
        return buyerCouponRepository.countAllByBuyerIdAndCouponId(buyerId, couponId);
    }

    @Override
    public BuyerCoupon findByIdAndBuyerId(Long id, Long buyerId) {
        return buyerCouponRepository.findFirstByIdAndBuyerIdAndIsAvailable(id, buyerId, true);
    }

    @Override
    public Integer countAllByBuyerIdAndCouponIds(Long buyerId, List<Long> couponIds) {
        return buyerCouponRepository.countAllByBuyerIdAndCouponIdIn(buyerId, couponIds);
    }

    @Override
    public List<BuyerCoupon> findAll(Specification<BuyerCoupon> buyerCouponSpecification) {
        return buyerCouponRepository.findAll(buyerCouponSpecification);
    }
}