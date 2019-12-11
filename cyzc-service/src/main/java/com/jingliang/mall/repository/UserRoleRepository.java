package com.jingliang.mall.repository;

import com.jingliang.mall.entity.UserRole;
import com.jingliang.mall.repository.base.BaseRepository;

import java.util.List;

/**
 * 用户角色表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
public interface UserRoleRepository extends BaseRepository<UserRole, Long> {
    /**
     * 根据用户Id查询用户关联的角色
     *
     * @param userId 用户Id
     * @param isAvailable 是否可用
     * @return 返回关联角色的信息
     */
    List<UserRole> findAllByUserIdAndIsAvailable(Long userId,Boolean isAvailable);
    /**
     * 根据角色Id查询用户关联的角色
     *
     * @param roleId 角色Id
     * @param isAvailable 是否可用
     * @return 返回关联角色的信息
     */
    List<UserRole> findAllByRoleIdAndIsAvailable(Long roleId,Boolean isAvailable);

    /**
     * 根据用户Id和角色Id查询绑定关系
     *
     * @param userId 用户Id
     * @param roleId 角色Id
     * @return 返回查询到的绑定关系
     */
    UserRole findFirstByUserIdAndRoleId(Long userId, Long roleId);
}