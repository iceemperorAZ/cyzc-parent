package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.RoleMenu;
import com.jingliang.mall.repository.RoleMenuRepository;
import com.jingliang.mall.service.RoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色资源表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@Service
@Slf4j
public class RoleMenuServiceImpl implements RoleMenuService {

    private final RoleMenuRepository roleMenuRepository;

    public RoleMenuServiceImpl(RoleMenuRepository roleMenuRepository) {
        this.roleMenuRepository = roleMenuRepository;
    }

    @Override
    public List<RoleMenu> findAllByRoleId(Long roleId) {
        return roleMenuRepository.findAllByRoleIdAndIsAvailable(roleId, true);
    }

    @Override
    public RoleMenu findAllByRoleIdAndMenuId(Long roleId, Long menuId) {
        return roleMenuRepository.findAllByRoleIdAndMenuIdAndIsAvailable(roleId, menuId,true);
    }

    @Override
    public RoleMenu save(RoleMenu roleMenu) {
        return roleMenuRepository.save(roleMenu);
    }
}