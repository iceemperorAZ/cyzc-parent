package com.jingliang.mall.service;

import com.jingliang.mall.entity.AttributeTypeDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * 属性表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 16:53:10
 */
public interface AttributeTypeDetailService {
    /**
     * 保存属性
     *
     * @param attributeTypeDetail 属性对象
     * @return 返回保存后的属性对象
     */
    AttributeTypeDetail save(AttributeTypeDetail attributeTypeDetail);

    /**
     * 分页查询全部属性
     *
     * @param attributeTypeDetailSpecification 查询条件
     * @param pageRequest                      分页条件
     * @return 返回查询到的属性列表
     */
    Page<AttributeTypeDetail> findAll(Specification<AttributeTypeDetail> attributeTypeDetailSpecification, PageRequest pageRequest);
}