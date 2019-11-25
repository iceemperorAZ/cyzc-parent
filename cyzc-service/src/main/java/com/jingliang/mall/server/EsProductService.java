package com.jingliang.mall.server;

import com.jingliang.mall.esdocument.EsProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Es商品Service
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-27 22:58
 */
public interface EsProductService {

    /**
     * 根据搜索词分页查询所有上架商品
     *
     * @param keyword  搜索词
     * @param pageable 分页条件
     * @return 返回搜索到的商品列表
     */
    Page<EsProduct> findAll(String keyword, Pageable pageable);
}
