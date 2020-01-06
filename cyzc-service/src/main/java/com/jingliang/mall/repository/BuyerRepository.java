package com.jingliang.mall.repository;

import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.BuyerSale;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 用户表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 09:12:45
 */
public interface BuyerRepository extends BaseRepository<Buyer, Long> {
    /**
     * 根据第三方唯一凭证和是否可用查询用户信息
     *
     * @param uniqueId    第三方唯一凭证
     * @param isAvailable 是否可用
     * @return 返回查询到的用户信息
     */
    Buyer findFirstByUniqueIdAndIsAvailable(String uniqueId, Boolean isAvailable);

    /**
     * 根据销售员Id查询绑定的会员列表
     *
     * @param saleUserId  销售员Id
     * @param isAvailable 是否可用
     * @return 返回查询到的会员列表
     */
    List<Buyer> findAllBySaleUserIdAndIsAvailable(Long saleUserId, Boolean isAvailable);

    /**
     * 分页查询商户信息
     *
     * @param saleUserId  绑定销售Id
     * @param isAvailable 是否可用
     * @param pageable    分页条件
     * @return 返回查询到的商户列表
     */
    Page<Buyer> findAllBySaleUserIdAndIsAvailable(Long saleUserId, Boolean isAvailable, Pageable pageable);

    /**
     * 根据销售Id分页查询
     *
     * @param saleUserId
     * @param isAvailable
     * @param pageRequest
     * @return
     */
    @Query("SELECT buyer FROM Buyer buyer  WHERE buyer.id in(SELECT DISTINCT buyerId  FROM BuyerSale WHERE saleId = :saleUserId and isAvailable=:isAvailable) and buyer.isAvailable=:isAvailable  ORDER BY buyer.id")
    Page<Buyer> findAllBySaleIdAndIsAvailable( @Param("saleUserId") Long saleUserId, @Param("isAvailable") Boolean isAvailable, Pageable pageRequest);

}