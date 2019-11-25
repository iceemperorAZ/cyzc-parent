package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Sku;
import com.jingliang.mall.entity.SkuDetail;
import com.jingliang.mall.repository.SkuDetailRepository;
import com.jingliang.mall.repository.SkuRepository;
import com.jingliang.mall.service.SkuDetailService;
import com.jingliang.mall.server.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存详情ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 19:00:48
 */
@Service
@Slf4j
public class SkuDetailServiceImpl implements SkuDetailService {

    private final SkuDetailRepository skuDetailRepository;
    private final SkuRepository skuRepository;
    private final RedisService redisService;

    public SkuDetailServiceImpl(SkuDetailRepository skuDetailRepository, SkuRepository skuRepository, RedisService redisService) {
        this.skuDetailRepository = skuDetailRepository;
        this.skuRepository = skuRepository;
        this.redisService = redisService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SkuDetail save(SkuDetail skuDetail) {
        skuDetail.setSkuResidueNum(skuDetail.getSkuAppendNum());
        Long skuId = skuDetail.getSkuId();
        Sku sku = skuRepository.findAllByIdAndIsAvailable(skuId, true);
        skuDetail.setProductId(sku.getProductId());
        skuDetail.setProductName(sku.getProductName());
        skuDetail.setSkuNo(sku.getSkuNo());
        Sku newSku = new Sku();
        newSku.setId(skuId);
        newSku.setSkuRealityNum(skuDetail.getSkuAppendNum());
        newSku.setSkuLineNum(skuDetail.getSkuAppendNum());
        newSku.setSkuHistoryTotalNum(skuDetail.getSkuAppendNum());
        newSku.setUpdateTime(skuDetail.getUpdateTime());
        newSku.setUpdateUserId(skuDetail.getUpdateUserId());
        newSku.setUpdateUserName(skuDetail.getUpdateUserName());
        skuRepository.appendSkuById(newSku);
        //追加redis中的线上库存
        log.debug("追加redis中的线上库存，商品[{}],redis线上剩余库存为{}", sku.getProductName(), redisService.skuLineIncrement(String.valueOf(sku.getProductId()), skuDetail.getSkuAppendNum()));
        return skuDetailRepository.save(skuDetail);
    }

    @Override
    public Page<SkuDetail> findAll(Specification<SkuDetail> skuDetailSpecification, PageRequest pageRequest) {
        return skuDetailRepository.findAll(skuDetailSpecification, pageRequest);
    }

    @Override
    public SkuDetail findById(Long id) {
        return skuDetailRepository.findAllByIdAndIsAvailable(id, true);
    }
}