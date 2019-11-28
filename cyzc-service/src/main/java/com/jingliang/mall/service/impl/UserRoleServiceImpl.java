package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.UserRole;
import com.jingliang.mall.repository.UserRoleRepository;
import com.jingliang.mall.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户角色表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@Service
@Slf4j
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public List<UserRole> findAllByUserId(Long userId) {
        return userRoleRepository.findAllByUserIdAndIsAvailable(userId, true);
    }

    @Override
    public UserRole findAllByUserIdAndRoleId(Long userId, Long roleId) {
        return userRoleRepository.findAllByUserIdAndRoleIdAndIsAvailable(userId, roleId, true);
    }

    @Override
    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }
}