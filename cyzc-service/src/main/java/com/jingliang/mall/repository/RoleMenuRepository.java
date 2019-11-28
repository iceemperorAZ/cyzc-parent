package com.jingliang.mall.repository;

import com.jingliang.mall.entity.RoleMenu;
import com.jingliang.mall.repository.base.BaseRepository;

import java.util.List;

/**
 * 角色资源表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
public interface RoleMenuRepository extends BaseRepository<RoleMenu, Long> {

    /**
     * 根据角色Id查询角色资源关系集合
     *
     * @param roleId      角色Id
     * @param isAvailable 是否可用
     * @return 返回查询到的决死资源关联集合
     */
    List<RoleMenu> findAllByRoleIdAndIsAvailable(Long roleId, Boolean isAvailable);
    /**
     * 根据角色Id和资源Id查询角色资源关系
     *
     * @param roleId 角色Id
     * @param menuId 资源Id
     * @return 返回保存后的角色资源关系
     */
    RoleMenu findAllByRoleIdAndMenuIdAndIsAvailable(Long roleId, Long menuId, Boolean isAvailable);
}