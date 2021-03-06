package com.jingliang.mall.repository;


import com.jingliang.mall.entity.Product;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * 商品表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 16:01:46
 */
public interface ProductRepository extends BaseRepository<Product, Long> {
    /**
     * 根据Id集合查询商品集合
     *
     * @param isAvailable 是否可用
     * @param ids         主键Id集合
     * @return 查询到的商品集合信息
     */
    List<Product> findAllByIsAvailableAndIdIn(Boolean isAvailable, List<Long> ids);

    /**
     * 根据Id、是否上架，是否可用查询商品信息
     *
     * @param id          商品Id
     * @param isShow      是否上架
     * @param isAvailable 是否可用
     * @return 返回查询到的商品信息
     */
    Product findByIdAndIsShowAndIsAvailable(Long id, Boolean isShow, Boolean isAvailable);

    /**
     * 根据商品名称，是否可用查询商品信息
     *
     * @param productName 商品名称
     * @param isAvailable 是否可用
     * @return 返回查询到的商品信息
     */
    Product findFirstByProductNameAndIsAvailable(String productName, Boolean isAvailable);

    /**
     * 根据是否可用和商品名称查询商品信息
     *
     * @param isAvailable 是否可用
     * @param productName 商品名称
     * @return 返回查询到的商品信息
     */
    List<Product> findAllByIsAvailableAndProductNameLike(Boolean isAvailable, String productName);

    /**
     * 根据商品区Id查询商品列表
     *
     * @param productZoneId 商品区Id
     * @param isAvailable   是否可用
     * @return 返回查询到商品列表
     */
    List<Product> findAllByProductZoneIdAndIsAvailable(Long productZoneId, Boolean isAvailable);

    /**
     * 根据商品分类Id和是否上架查询商品列表
     *
     * @param productTypeId 商品分类Id
     * @param isShow        是否上架
     * @return 返回查询到商品列表
     */
    Integer countAllByProductTypeIdAndIsShow(Long productTypeId, Boolean isShow);

    /**
     * 是否上架查询商品列表
     *
     * @param productTypeId 商品分类Id
     * @param productSort   商品序号
     * @param isAvailable   是否可用
     * @return 返回查询到商品
     */
    Product findFirstByProductTypeIdAndProductSortAndIsAvailable(Long productTypeId, Integer productSort, Boolean isAvailable);

    /**
     * 根据商品名称，规格，查询可用的商品
     *
     * @param productName
     * @param specs
     * @param isAvailable
     * @return
     */
    Product findFirstByProductNameAndSpecsAndIsAvailable(String productName, String specs, Boolean isAvailable);

    /**
     * 根据商品分类和序号查询
     *
     * @param productTypeId
     * @param productSort
     * @return
     */
    Product findAllByProductTypeIdAndProductSortAndIsAvailable(Long productTypeId, Integer productSort, Boolean isAvailable);

    @Query(value = " SELECT p.product_no as '商品编号',p.product_name as '商品名称',p.specs as '商品规格' from tb_product p WHERE p.is_available = 1; ", nativeQuery = true)
    List<Map<String, Object>> getAllProduct();

    /**
     * 导出excel-商品名和规格合并
     *
     * @return
     */
    @Query(value = " SELECT CONCAT_WS(\" \",ANY_VALUE(p.product_name),ANY_VALUE(p.specs)) AS productName,ANY_VALUE(p.product_no) AS productNo " +
            "FROM tb_product p WHERE p.is_available = 1 ", nativeQuery = true)
    List<Map<String, Object>> downExcel();
}