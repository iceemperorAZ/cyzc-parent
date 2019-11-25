package com.jingliang.mall.service;

import com.jingliang.mall.entity.ProductZone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * 商品区表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-29 19:46:43
 */
public interface ProductZoneService {

    /**
     * 保存商品区
     *
     * @param productZone 商品区对象
     * @return 返回保存后的商品区
     */
    ProductZone save(ProductZone productZone);

    /**
     * 分页查询全部商品区
     *
     * @param productZoneSpecification 查询条件
     * @param pageRequest              分页条件
     * @return 返回查询到的商品区列表
     */
    Page<ProductZone> findAll(Specification<ProductZone> productZoneSpecification, PageRequest pageRequest);

    /**
     * 批量删除商品区
     *
     * @param productZones 商品区集合
     * @return 返回查询到的商品区集合
     */
    List<ProductZone> batchDelete(List<ProductZone> productZones);
}