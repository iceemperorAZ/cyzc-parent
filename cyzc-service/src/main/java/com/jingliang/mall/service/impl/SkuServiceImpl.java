package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Sku;
import com.jingliang.mall.repository.SkuRepository;
import com.jingliang.mall.service.SkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品库存表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
@Service
@Slf4j
public class SkuServiceImpl implements SkuService {

    private final SkuRepository skuRepository;

    public SkuServiceImpl(SkuRepository skuRepository) {
        this.skuRepository = skuRepository;
    }

    @Override
    public Sku findByProductId(Long productId) {
        return skuRepository.findFirstByProductIdAndIsAvailable(productId, true);
    }

    @Override
    public Page<Sku> findAll(Specification<Sku> skuSpecification, PageRequest pageRequest) {
        return skuRepository.findAll(skuSpecification, pageRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLineSkuByProductId(Sku sku) {
        skuRepository.updateLineSkuByProductId(sku);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRealitySkuByProductId(Sku sku) {
        skuRepository.updateRealitySkuByProductId(sku);
    }
}