package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Sku;
import com.jingliang.mall.entity.SkuDetail;
import com.jingliang.mall.entity.SkuRecord;
import com.jingliang.mall.repository.SkuDetailRepository;
import com.jingliang.mall.repository.SkuRecordRepository;
import com.jingliang.mall.repository.SkuRepository;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.SkuRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存记录单ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-11 11:56:20
 */
@Service
@Slf4j
public class SkuRecordServiceImpl implements SkuRecordService {

    private final SkuRecordRepository skuRecordRepository;
    private final SkuRepository skuRepository;
    private final SkuDetailRepository skuDetailRepository;
    private final RedisService redisService;

    public SkuRecordServiceImpl(SkuRecordRepository skuRecordRepository, SkuRepository skuRepository, SkuDetailRepository skuDetailRepository, RedisService redisService) {
        this.skuRecordRepository = skuRecordRepository;
        this.skuRepository = skuRepository;
        this.skuDetailRepository = skuDetailRepository;
        this.redisService = redisService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SkuRecord save(SkuRecord skuRecord) {
        if (skuRecord.getStatus() == 200) {
            SkuRecord newSkuRecord = skuRecordRepository.findAllByIdAndIsAvailable(skuRecord.getId(), true);
            newSkuRecord.setStatus(200);
            if (StringUtils.isBlank(skuRecord.getApproveOpinion())) {
                newSkuRecord.setApproveOpinion("审核通过");
            } else {
                newSkuRecord.setApproveOpinion(skuRecord.getApproveOpinion());
            }
            newSkuRecord.setApproveUserId(skuRecord.getUpdateUserId());
            newSkuRecord.setApproveUserName(skuRecord.getUpdateUserName());
            newSkuRecord.setApproveTime(skuRecord.getUpdateTime());
            skuRecord = newSkuRecord;
            //审核通过,进行加/减库存操作
            //更新库存
            Sku sku = new Sku();
            sku.setId(newSkuRecord.getSkuId());
            sku.setUpdateTime(newSkuRecord.getUpdateTime());
            sku.setUpdateUserId(newSkuRecord.getUpdateUserId());
            sku.setUpdateUserName(newSkuRecord.getUpdateUserName());
            sku.setSkuLineNum(newSkuRecord.getNum());
            sku.setSkuRealityNum(newSkuRecord.getNum());
            skuRepository.updateSkuById(sku);
            //更新库存详情
            SkuDetail skuDetail = new SkuDetail();
            skuDetail.setId(newSkuRecord.getSkuDetailId());
            skuDetail.setUpdateTime(newSkuRecord.getUpdateTime());
            skuDetail.setUpdateUserId(newSkuRecord.getUpdateUserId());
            skuDetail.setUpdateUserName(newSkuRecord.getUpdateUserName());
            skuDetail.setSkuResidueNum(newSkuRecord.getNum());
            skuDetailRepository.updateSkuDetailById(skuDetail);
            //减少redis中的线上库存
            log.debug("减少redis中的线上库存，商品[{}],redis线上剩余库存为{}", sku.getProductName(), redisService.skuLineDecrement(String.valueOf(newSkuRecord.getProductId()), newSkuRecord.getNum()));
        }
        return skuRecordRepository.save(skuRecord);
    }

    @Override
    public Page<SkuRecord> findAll(Specification<SkuRecord> skuRecordSpecification, PageRequest pageRequest) {
        return skuRecordRepository.findAll(skuRecordSpecification, pageRequest);
    }

    @Override
    public SkuRecord findById(Long id) {
        return skuRecordRepository.findAllByIdAndIsAvailable(id, true);
    }
}