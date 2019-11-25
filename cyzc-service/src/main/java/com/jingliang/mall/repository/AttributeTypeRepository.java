package com.jingliang.mall.repository;

import com.jingliang.mall.entity.AttributeType;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;

/**
 * 属性分类表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 16:53:10
 */
public interface AttributeTypeRepository extends BaseRepository<AttributeType, Long> {

    /**
     * 非懒加载全部 分页查询全部属性分类
     *
     * @param attributeTypeSpecification 查询条件
     * @param pageRequest                分页条件
     * @return 返回查询到的属性分类列表
     */
    @Override
    @EntityGraph(value = "AttributeType.attributeTypeDetails", type = EntityGraph.EntityGraphType.FETCH)
    Page<AttributeType> findAll(Specification<AttributeType> attributeTypeSpecification, Pageable pageRequest);

}