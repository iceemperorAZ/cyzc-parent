package com.jingliang.mall.service;

import com.jingliang.mall.entity.BuyerAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * 会员收货地址表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-23 09:33:48
 */
public interface BuyerAddressService {

    /**
     * 保存用户收货地址
     *
     * @param buyerAddress 用户收货地址对象
     * @return 返回保存后的用户收货地址信息
     */
    BuyerAddress save(BuyerAddress buyerAddress);

    /**
     * 分页查询所有用户收货地址
     *
     * @param buyerAddressSpecification 查询条件
     * @param pageRequest               分页条件
     * @return 返回查询到的用户地址列表
     */
    Page<BuyerAddress> findAll(Specification<BuyerAddress> buyerAddressSpecification, PageRequest pageRequest);

    /**
     * 根据主键Id查询地址是否存在
     *
     * @param id      主键Id
     * @param buyerId 用户Id
     * @return 返回统计到的数量
     */
    Integer countByIdAndBuyerId(Long id, Long buyerId);

    /**
     * 根据主键Id查询默认收货地址
     *
     * @param id
     * @return
     */
    BuyerAddress findDefaultAddrByBuyerId(Long id);

    /**
     * 根据商户Id查询所有地址
     *
     * @param buyerId
     * @return
     */
    List<BuyerAddress> findByBuyerId(Long buyerId);
}