package com.jingliang.mall.service;


import com.jingliang.mall.entity.Group;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.entity.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * 员工与组关系映射表Service
 * 
 * @author XiaoBing Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
public interface UserGroupService {

    /**
     * 根据组编码分页查询用户
     * @param groupNo 组编号
     * @return
     */
    public Page<User> findUserByGroupNo(String groupNo);

    /**
     * 对用户组数据进行逻辑删除
     * @param userGroup 用户组
     * @return
     */
    public UserGroup delete(UserGroup userGroup);

    /**
     * 更新/保存用户数据
     * @param user 用户
     * @return
     */
    public UserGroup saveU(User user);
}