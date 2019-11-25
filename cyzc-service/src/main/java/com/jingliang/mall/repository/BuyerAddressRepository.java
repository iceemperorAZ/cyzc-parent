package com.jingliang.mall.repository;

import com.jingliang.mall.repository.base.BaseRepository;
import com.jingliang.mall.entity.BuyerAddress;

/**
 * 会员收货地址表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-23 09:33:48
 */
public interface BuyerAddressRepository extends BaseRepository<BuyerAddress, Long> {

    /**
     * 根据会员Id查询默认收货地址
     *
     * @param buyerId     会员Id
     * @param isDefault   是否默认
     * @param isAvailable 是否可用
     * @return 返回查询到的地址信息
     */
    BuyerAddress findFirstByBuyerIdAndIsDefaultAndIsAvailable(Long buyerId, Boolean isDefault, Boolean isAvailable);

    /**
     * 根据会员Id和主键Id查询收货地址是否存在
     *
     * @param id          主键Id
     * @param buyerId     会员Id
     * @param isAvailable 是否可用
     * @return 返回查询到的地址信息
     */
    Integer countByIdAndBuyerIdAndIsAvailable(Long id, Long buyerId, Boolean isAvailable);
}