package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.ProductType;
import com.jingliang.mall.repository.ProductTypeRepository;
import com.jingliang.mall.service.ProductTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品分类表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
@Service
@Slf4j
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public ProductType save(ProductType productType) {
        return productTypeRepository.save(productType);
    }

    @Override
    public Page<ProductType> findAll(Specification<ProductType> productSpecification, PageRequest pageRequest) {
        return productTypeRepository.findAll(productSpecification, pageRequest);
    }

    @Override
    public List<ProductType> findAll() {
        return productTypeRepository.findAllByIsAvailable(true);
    }

    @Override
    public List<ProductType> findAll(List<Long> productTypeIds) {
        return productTypeRepository.findAllByIdInAndIsAvailable(productTypeIds,true);
    }
}