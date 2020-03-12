package com.jingliang.mall.service;

import com.jingliang.mall.entity.BuyerSale;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * 商户销售绑定表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-01-03 13:10:34
 */
public interface BuyerSaleService {

    /**
     * 保存
     *
     * @param buyerSale
     * @return
     */
    BuyerSale save(BuyerSale buyerSale);

    /**
     * 根据销售Id查询
     *
     * @param saleUserId
     * @param buyerId
     * @return
     */
    BuyerSale findBySaleIdAndBuyerId(Long saleUserId, Long buyerId);

    /**
     * 根据条件查询绑定信息
     *
     * @param buyerSaleSpecification
     */
    List<BuyerSale> finAll(Specification<BuyerSale> buyerSaleSpecification);
}