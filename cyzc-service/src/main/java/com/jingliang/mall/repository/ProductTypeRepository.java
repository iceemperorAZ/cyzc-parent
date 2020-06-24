package com.jingliang.mall.repository;


import com.jingliang.mall.entity.ProductType;
import com.jingliang.mall.repository.base.BaseRepository;

import java.util.List;

/**
 * 商品分类表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 16:01:46
 */
public interface ProductTypeRepository extends BaseRepository<ProductType, Long> {

    /**
     * 根据商品分类Id集合查询
     *
     * @param ids         分类Id集合
     * @param isAvailable 是否可用
     * @return
     */
    List<ProductType> findAllByIdInAndIsAvailable(List<Long> ids, boolean isAvailable);

    /**
     * 查询分类集合的第一个
     *
     * @param isAvailable
     * @return
     */
    ProductType findFirstByIsAvailable(boolean isAvailable);

    /**
     * 根据id查询商品分类
     *
     * @param id
     * @param isAvailable
     * @return
     */
    ProductType findByIdAndIsAvailable(Long id, boolean isAvailable);
}