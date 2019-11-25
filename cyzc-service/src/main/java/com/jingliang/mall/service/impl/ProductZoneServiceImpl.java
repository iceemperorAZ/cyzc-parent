package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.ProductZone;
import com.jingliang.mall.repository.ProductZoneRepository;
import com.jingliang.mall.service.ProductZoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品区表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-29 19:46:43
 */
@Service
@Slf4j
public class ProductZoneServiceImpl implements ProductZoneService {

    private final ProductZoneRepository productZoneRepository;

    public ProductZoneServiceImpl(ProductZoneRepository productZoneRepository) {
        this.productZoneRepository = productZoneRepository;
    }

    @Override
    public ProductZone save(ProductZone productZone) {
        return productZoneRepository.save(productZone);
    }

    @Override
    public Page<ProductZone> findAll(Specification<ProductZone> productZoneSpecification, PageRequest pageRequest) {
        return productZoneRepository.findAll(productZoneSpecification, pageRequest);
    }

    @Override
    public List<ProductZone> batchDelete(List<ProductZone> productZones) {
        return productZoneRepository.saveAll(productZones);
    }
}