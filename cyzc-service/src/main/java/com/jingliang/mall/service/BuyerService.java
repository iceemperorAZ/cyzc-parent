package com.jingliang.mall.service;

import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * 用户表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 09:12:45
 */
public interface BuyerService {

    /**
     * 根据第三方唯一凭证查询用户信息
     *
     * @param uniqueId 第三方唯一凭证
     * @return 返回查询到的用户信息
     */
    Buyer findByUniqueId(String uniqueId);

    /**
     * 保存用户信息
     *
     * @param buyer 用户信息
     * @return 返回保存后的用户信息
     */
    Buyer save(Buyer buyer);

    /**
     * 分页查询全部会员
     *
     * @param buyerSpecification 查询条件
     * @param pageRequest        分页条件
     * @return 返回查询到的会员列表
     */
    Page<Buyer> findAll(Specification<Buyer> buyerSpecification, PageRequest pageRequest);

    /**
     * 根据Id查询会员信息
     *
     * @param id 主键Id
     * @return 返回查询到的会员信息
     */
    Buyer findById(Long id);

    /**
     * 根据销售员Id查询绑定的所有商户列表
     *
     * @param saleUserId 销售员Id
     * @return 返回查询到的所有商户列表
     */
    List<Buyer> findAllBySaleUserId(Long saleUserId);

    /**
     * 分页查询商户信息
     *
     * @param saleUserId  绑定销售Id
     * @param pageRequest 分页条件
     * @return 返回查询到的商户列表
     */
    Page<Buyer> findAllBySaleUserId(Long saleUserId, PageRequest pageRequest);

    /**
     * 分页查询商户信息
     *
     * @param saleUserId  绑定销售Id
     * @param pageRequest 分页条件
     * @return 返回查询到的商户列表
     */
    public Page<Buyer> findAllBySaleId(Long saleUserId, PageRequest pageRequest);

    /**
     * 根据条件查询
     * @param buyerSpecification
     * @return
     */
    List<Buyer> findAll(Specification<Buyer> buyerSpecification);
}