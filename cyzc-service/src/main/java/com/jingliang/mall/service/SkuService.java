package com.jingliang.mall.service;

import com.jingliang.mall.entity.Sku;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * 商品库存表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
public interface SkuService {

    /**
     * 根据商品Id查询库存数量
     *
     * @param productId 商品Id
     * @return 返回查询到的库存信息
     */
    Sku findByProductId(Long productId);

    /**
     * 分页查询所有库存
     *
     * @param skuSpecification 查询条件
     * @param pageRequest      分页条件
     * @return 返回查询到的库存列表
     */
    Page<Sku> findAll(Specification<Sku> skuSpecification, PageRequest pageRequest);

    /**
     * 根据库存Id减少线上库存
     *
     * @param sku 库存对象
     */
    void updateLineSkuByProductId(Sku sku);

    /**
     * 根据库存Id减少/增加实际库存
     *
     * @param sku 库存对象
     */
    public void updateRealitySkuByProductId(Sku sku);
}