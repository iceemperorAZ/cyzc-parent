package com.jingliang.mall.service;

import com.jingliang.mall.entity.RoleMenu;

import java.util.List;

/**
 * 角色资源表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
public interface RoleMenuService {

    /**
     * 根据角色Id查询角色资源关系集合
     *
     * @param roleId 角色Id
     * @return 返回角色资源管理集合
     */
    List<RoleMenu> findAllByRoleId(Long roleId);
}