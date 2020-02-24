package com.jingliang.mall.service;

import com.jingliang.mall.entity.ManagerSale;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * 商户销售绑定表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-02-17 14:21:26
 */
public interface ManagerSaleService {

    /**
     * 根据销售Id查询绑定信息
     *
     *
     * @param managerId
     * @param saleId
     * @return
     */
    List<ManagerSale> findByManagerIdAndSaleId(Long managerId, Long saleId);

    /**
     * 查询区域经理下的销售
     * @param orderSpecification
     * @return
     */
    List<ManagerSale> findAll(Specification<ManagerSale> orderSpecification);
}