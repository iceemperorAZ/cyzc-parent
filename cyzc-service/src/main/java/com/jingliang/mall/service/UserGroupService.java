package com.jingliang.mall.service;


import com.jingliang.mall.entity.Group;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.entity.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * 员工与组关系映射表Service
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
public interface UserGroupService {

    public Page<User> findGroup(Group group, Specification<User> userSpecification, PageRequest pageRequest);

    public UserGroup delete(UserGroup userGroup);

    public UserGroup saveU(User user);
}