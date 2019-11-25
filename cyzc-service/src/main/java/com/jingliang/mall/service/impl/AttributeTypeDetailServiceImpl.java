package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.AttributeTypeDetail;
import com.jingliang.mall.repository.AttributeTypeDetailRepository;
import com.jingliang.mall.service.AttributeTypeDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * 属性表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 16:53:10
 */
@Service
@Slf4j
public class AttributeTypeDetailServiceImpl implements AttributeTypeDetailService {

    private final AttributeTypeDetailRepository attributeTypeDetailRepository;

    public AttributeTypeDetailServiceImpl(AttributeTypeDetailRepository attributeTypeDetailRepository) {
        this.attributeTypeDetailRepository = attributeTypeDetailRepository;
    }

    @Override
    public AttributeTypeDetail save(AttributeTypeDetail attributeTypeDetail) {
        return attributeTypeDetailRepository.save(attributeTypeDetail);
    }

    @Override
    public Page<AttributeTypeDetail> findAll(Specification<AttributeTypeDetail> attributeTypeDetailSpecification, PageRequest pageRequest) {
        return attributeTypeDetailRepository.findAll(attributeTypeDetailSpecification, pageRequest);
    }
}