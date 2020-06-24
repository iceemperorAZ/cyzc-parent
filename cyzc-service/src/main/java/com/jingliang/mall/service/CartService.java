package com.jingliang.mall.service;

import com.jingliang.mall.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * 用户购物车Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
public interface CartService {

    /**
     * 保存购物车
     *
     * @param cart 购物车对象
     * @return 返回保存后的购物车
     */
    Cart save(Cart cart);

    /**
     * 分页查询所有购物项
     *
     * @param cartSpecification 查询条件
     * @param pageRequest       分页条件
     * @return 返回查询到的购物项列表
     */
    Page<Cart> findAll(Specification<Cart> cartSpecification, PageRequest pageRequest);

    /**
     * 清空用户购物车
     *
     * @param buyerId 用户Id
     */
    void emptyCart(Long buyerId);

    /**
     * 清空部分购物项
     *
     * @param buyerId    用户Id
     * @param productIds 商品Id集合
     */
    void emptyCartItem(Long buyerId, List<Long> productIds);

    /**
     * 根据商户id查询购物车数量
     *
     * @param buyerId
     * @return
     */
    Integer countAllByBuyerId(Long buyerId);
}