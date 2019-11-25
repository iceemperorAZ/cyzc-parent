package com.jingliang.mall.service;

import com.jingliang.mall.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * 角色表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
public interface RoleService {

    /**
     * 分页查询全部角色
     *
     * @param roleSpecification 查询条件
     * @param pageRequest       分页条件
     * @return 返回查询到的角色集合
     */
    Page<Role> findAll(Specification<Role> roleSpecification, PageRequest pageRequest);
}