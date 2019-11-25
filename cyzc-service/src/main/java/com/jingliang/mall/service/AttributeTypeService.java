package com.jingliang.mall.service;

import com.jingliang.mall.entity.AttributeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * 属性分类表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 16:53:10
 */
public interface AttributeTypeService {
    /**
     * 保存属性分类
     *
     * @param attributeType 属性分类对象
     * @return 返回保存后的属性分类
     */
    AttributeType save(AttributeType attributeType);

    /**
     * 分页查询全部属性分类
     *
     * @param attributeTypeSpecification 查询条件
     * @param pageRequest                分页条件
     * @return 返回查询到的属性分类列表
     */
    Page<AttributeType> findAll(Specification<AttributeType> attributeTypeSpecification, PageRequest pageRequest);
    /**
     * 分页查询全部属性分类
     *
     * @param attributeTypeSpecification 查询条件
     * @param pageRequest                分页条件
     * @return 返回查询到的属性分类列表
     */
    Page<AttributeType> queryAll(Specification<AttributeType> attributeTypeSpecification, PageRequest pageRequest);


}