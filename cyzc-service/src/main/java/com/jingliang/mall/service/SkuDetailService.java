package com.jingliang.mall.service;

import com.jingliang.mall.entity.SkuDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * 库存详情Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 19:00:48
 */
public interface SkuDetailService {

    /**
     * 保存库存详情
     *
     * @param skuDetail 库存详情对象
     * @return 返回保存后的库存详情
     */
    SkuDetail save(SkuDetail skuDetail);

    /**
     * 分页查询全部库存详情列表
     *
     * @param skuDetailSpecification 查询条件
     * @param pageRequest            分页条件
     * @return 返回查询到的库存详情列表
     */
    Page<SkuDetail> findAll(Specification<SkuDetail> skuDetailSpecification, PageRequest pageRequest);

    /**
     * 根据库存详情主键查询库存详情
     * @param id 主键Id
     * @return 返回查询到的库存详情
     */
    SkuDetail findById(Long id);
}