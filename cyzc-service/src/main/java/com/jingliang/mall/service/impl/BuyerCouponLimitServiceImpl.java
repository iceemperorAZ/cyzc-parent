package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.BuyerCouponLimit;
import com.jingliang.mall.repository.BuyerCouponLimitRepository;
import com.jingliang.mall.service.BuyerCouponLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户优惠券使用限制ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-19 09:17:25
 */
@Service
@Slf4j
public class BuyerCouponLimitServiceImpl implements BuyerCouponLimitService {

    private final BuyerCouponLimitRepository buyerCouponLimitRepository;

    public BuyerCouponLimitServiceImpl(BuyerCouponLimitRepository buyerCouponLimitRepository) {
        this.buyerCouponLimitRepository = buyerCouponLimitRepository;
    }

    @Override
    public BuyerCouponLimit save(BuyerCouponLimit buyerCouponLimit) {
        return buyerCouponLimitRepository.save(buyerCouponLimit);
    }

    @Override
    public List<BuyerCouponLimit> findAllByBuyerId(Long buyerId) {
        return buyerCouponLimitRepository.findAllByBuyerIdAndIsAvailable(buyerId, true);
    }

    @Override
    public BuyerCouponLimit findByBuyerIdAndProductTypeId(Long buyerId, Long productTypeId) {
        return buyerCouponLimitRepository.findFirstByBuyerIdAndProductTypeIdAndIsAvailable(buyerId,productTypeId,true);
    }

    @Override
    public List<BuyerCouponLimit> findAllByBuyerIdAndProductTypeIds(Long buyerId, List<Long> productTypeIds) {
        return buyerCouponLimitRepository.findAllByBuyerIdAndProductTypeIdInAndIsAvailable(buyerId,productTypeIds,true);
    }
}