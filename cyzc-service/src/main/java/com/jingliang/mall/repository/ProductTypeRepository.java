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
}