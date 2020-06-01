package com.jingliang.mall.service;

import com.jingliang.mall.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * 商品表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
public interface ProductService {

    /**
     * 保存 商品信息
     *
     * @param product 商品对象
     * @return 返回保存后的商品对象
     */
    Product save(Product product);

    /**
     * 分页查询全部商品
     *
     * @param productSpecification 查询条件
     * @param pageRequest          分页条件
     * @return 返回商品信息集合及分页信息
     */
    Page<Product> findAll(Specification<Product> productSpecification, PageRequest pageRequest);

    /**
     * 懒加载 根据主键Id查询可用商品
     *
     * @param id 主键Id
     * @return 返回查询到的商品
     */
    Product findAllById(Long id);

    /**
     * 懒加载 根据商品名称查询可用商品
     *
     * @param productName 商品名称
     * @return 返回查询到的商品
     */
    Product findAllByProductName(String productName);

    /**
     * 懒加载 根据主键Id查询上架的可用商品
     *
     * @param id 主键Id
     * @return 返回查询到的商品
     */
    Product findShowProductById(Long id);

    /**
     * 批量上架商品
     *
     * @param products 商品集合
     * @return 返回上架后的商品
     */
    List<Product> batchShow(List<Product> products);

    /**
     * 批量下架商品
     *
     * @param products 商品集合
     * @return 返回上架后的商品
     */
    List<Product> batchHide(List<Product> products);

    /**
     * 批量删除商品
     *
     * @param products 商品集合
     * @return 返回删除后的商品
     */
    List<Product> batchDelete(List<Product> products);

    /**
     * 根据商品名称模糊查询商品信息
     *
     * @param productName 商品名称
     * @return 返回查询到的商品信息
     */
    List<Product> findAllByProductNameLike(String productName);

    /**
     * 批量保存商品信息
     *
     * @param products 商品集合
     * @return 返回保存后的商品集合
     */
    List<Product> saveAll(List<Product> products);

    /**
     * 根据商品区Id查询商品列表
     *
     * @param productZoneId 商品区Id
     * @return 返回查询到的商品列表
     */
    List<Product> findAllByProductZoneId(Long productZoneId);

    /**
     * 根据商品分类和是否上架统计商品数量
     *
     * @param productTypeId 商品分类Id
     * @param isShow        是否上架
     * @return 返回统计到的商品数量
     */
    public Integer countByProductTypeIdAnnShow(Long productTypeId, Boolean isShow);

    /**
     * 根据商品分类和序号查询
     *
     * @param productTypeId
     * @param productSort
     * @return
     */
    Product findAllByProductTypeIdAndSort(Long productTypeId, Integer productSort);

    /**
     * 根据商品名称，规格，查询可用的商品
     *
     * @param productName
     * @param specs
     * @return
     */
    Product findByProductNameAndSpecs(String productName, String specs);
}