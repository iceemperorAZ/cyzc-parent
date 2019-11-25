package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.AttributeType;
import com.jingliang.mall.repository.AttributeTypeRepository;
import com.jingliang.mall.repository.lazy.LazyAttributeTypeRepository;
import com.jingliang.mall.service.AttributeTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * 属性分类表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 16:53:10
 */
@Service
@Slf4j
public class AttributeTypeServiceImpl implements AttributeTypeService {

    private final LazyAttributeTypeRepository lazyAttributeTypeRepository;
    private final AttributeTypeRepository attributeTypeRepository;

    public AttributeTypeServiceImpl(LazyAttributeTypeRepository lazyAttributeTypeRepository, AttributeTypeRepository attributeTypeRepository) {
        this.lazyAttributeTypeRepository = lazyAttributeTypeRepository;
        this.attributeTypeRepository = attributeTypeRepository;
    }

    @Override
    public AttributeType save(AttributeType attributeType) {
        return lazyAttributeTypeRepository.save(attributeType);
    }

    @Override
    public Page<AttributeType> findAll(Specification<AttributeType> attributeTypeSpecification, PageRequest pageRequest) {
        Page<AttributeType> attributeTypePage = lazyAttributeTypeRepository.findAll(attributeTypeSpecification, pageRequest);
        for (AttributeType attributeType : attributeTypePage.getContent()) {
            attributeType.setAttributeTypeDetails(null);
        }
        return attributeTypePage;
    }

    @Override
    public Page<AttributeType> queryAll(Specification<AttributeType> attributeTypeSpecification, PageRequest pageRequest) {
        return attributeTypeRepository.findAll(attributeTypeSpecification, pageRequest);
    }
}