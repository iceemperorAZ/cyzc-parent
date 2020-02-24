package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.ManagerSale;
import com.jingliang.mall.repository.ManagerSaleRepository;
import com.jingliang.mall.service.ManagerSaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户销售绑定表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-02-17 14:21:26
 */
@Service
@Slf4j
public class ManagerSaleServiceImpl implements ManagerSaleService {

    private final ManagerSaleRepository managerSaleRepository;

    public ManagerSaleServiceImpl(ManagerSaleRepository managerSaleRepository) {
        this.managerSaleRepository = managerSaleRepository;
    }

    @Override
    public List<ManagerSale> findByManagerIdAndSaleId(Long managerId, Long saleId) {
        return managerSaleRepository.findAllByManagerIdAndSaleId(managerId,saleId);
    }

    @Override
    public List<ManagerSale> findAll(Specification<ManagerSale> orderSpecification) {
        return managerSaleRepository.findAll(orderSpecification);
    }
}