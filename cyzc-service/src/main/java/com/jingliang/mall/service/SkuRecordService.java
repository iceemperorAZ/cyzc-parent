package com.jingliang.mall.service;

import com.jingliang.mall.entity.SkuRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * 库存记录单Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-11 11:56:20
 */
public interface SkuRecordService {

    /**
     * 保存库存出入记录单
     *
     * @param skuRecord 库存记录单
     * @return 返回保存后的库存记录单
     */
    SkuRecord save(SkuRecord skuRecord);

    /**
     * 分页查询库存出入记录列表
     *
     * @param skuRecordSpecification 查询条件
     * @param pageRequest            分页条件
     * @return 返回查询到的库存出入记录列表
     */
    Page<SkuRecord> findAll(Specification<SkuRecord> skuRecordSpecification, PageRequest pageRequest);
}