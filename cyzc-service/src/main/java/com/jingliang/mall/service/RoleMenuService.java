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

    /**
     * 根据角色Id和资源Id查询角色资源关系
     *
     * @param roleId 角色Id
     * @param menuId 资源Id
     * @return 返回保存后的角色资源关系
     */
    RoleMenu findAllByRoleIdAndMenuId(Long roleId, Long menuId);

    /**
     * 保存角色资源绑定关系
     * @param roleMenu 角色资源关系
     * @return 返回保存后的角色资源关系
     */
    RoleMenu save(RoleMenu roleMenu);
}