package com.jingliang.mall.service;

import com.jingliang.mall.entity.UserRole;

import java.util.List;

/**
 * 用户角色表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
public interface UserRoleService {

    /**
     * 根据用户Id查询用户关联的角色
     *
     * @param userId 用户Id
     * @return 返回关联角色的信息
     */
    List<UserRole> findAllByUserId(Long userId);

    /**
     * 根据用户Id和角色Id查询绑定关系
     *
     * @param userId 用户Id
     * @param roleId 角色Id
     * @return 返回查询到的绑定关系
     */
    UserRole findAllByUserIdAndRoleId(Long userId, Long roleId);

    /**
     * 保存用户角色关系
     * @param userRole 用户角色关系
     * @return 返回绑定后的用户关系
     */
    UserRole save(UserRole userRole);
}