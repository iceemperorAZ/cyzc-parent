package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Role;
import com.jingliang.mall.entity.RoleMenu;
import com.jingliang.mall.entity.UserRole;
import com.jingliang.mall.repository.RoleMenuRepository;
import com.jingliang.mall.repository.RoleRepository;
import com.jingliang.mall.repository.UserRoleRepository;
import com.jingliang.mall.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMenuRepository roleMenuRepository;
    private final UserRoleRepository userRoleRepository;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMenuRepository roleMenuRepository, UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.roleMenuRepository = roleMenuRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Page<Role> findAll(Specification<Role> roleSpecification, PageRequest pageRequest) {
        return roleRepository.findAll(roleSpecification, pageRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role save(Role role) {
        if (!role.getIsAvailable()) {
            //删除角色的同时，解除与角色绑定的用户，解除角色下绑定的所有资源
            List<UserRole> userRoles = userRoleRepository.findAllByRoleIdAndIsAvailable(role.getId(), true);
            userRoles.forEach(userRole -> {
                userRole.setUpdateTime(role.getUpdateTime());
                userRole.setUpdateUserName(role.getUpdateUserName());
                userRole.setUpdateUserId(role.getUpdateUserId());
                userRole.setIsAvailable(false);
            });
            userRoleRepository.saveAll(userRoles);
            List<RoleMenu> roleMenus = roleMenuRepository.findAllByRoleIdAndIsAvailable(role.getId(), true);
            roleMenus.forEach(roleMenu -> {
                roleMenu.setUpdateTime(role.getUpdateTime());
                roleMenu.setUpdateUserName(role.getUpdateUserName());
                roleMenu.setUpdateUserId(role.getUpdateUserId());
                roleMenu.setIsAvailable(false);
            });
            roleMenuRepository.saveAll(roleMenus);
        }
        return roleRepository.save(role);
    }
}