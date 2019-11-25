package com.jingliang.mall.repository;

import com.jingliang.mall.repository.base.BaseRepository;
import com.jingliang.mall.entity.Cart;

import java.util.List;

/**
 * 用户购物车Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
public interface CartRepository extends BaseRepository<Cart, Long> {
    /**
     * 查询用户下的
     *
     * @param buyerId     用户Id
     * @param isAvailable 是否可用（删除）
     * @return 返回查询到的购物项
     */
    List<Cart> findAllByBuyerIdAndIsAvailable(Long buyerId, Boolean isAvailable);

    /**
     * 查询用户下的
     *
     * @param buyerId     用户Id
     * @param isAvailable 是否可用（删除）
     * @param productIds  商品Id集合
     * @return 返回查询到的购物项
     */
    List<Cart> findAllByBuyerIdAndIsAvailableAndProductIdIsIn(Long buyerId, Boolean isAvailable, List<Long> productIds);
    /**
     * 查询用户下的
     *
     * @param buyerId     用户Id
     * @param isAvailable 是否可用（删除）
     * @param productId  商品Id
     * @return 返回查询到的购物项
     */
    Cart findFirstByBuyerIdAndIsAvailableAndProductId(Long buyerId, Boolean isAvailable, Long productId);
}