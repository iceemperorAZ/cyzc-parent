package com.jingliang.mall.service;

import com.jingliang.mall.entity.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * 商品分类表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
public interface ProductTypeService {

    /**
     * 保存商品分类
     *
     * @param productType 商品分类对象
     * @return 返回保存后的商品分类
     */
    ProductType save(ProductType productType);

    /**
     * 根据条件分页查询全部商品分类
     *
     * @param productSpecification 查询条件
     * @param pageRequest          分页条件
     * @return 返回商品分类信息集合及分页信息
     */
    Page<ProductType> findAll(Specification<ProductType> productSpecification, PageRequest pageRequest);
}