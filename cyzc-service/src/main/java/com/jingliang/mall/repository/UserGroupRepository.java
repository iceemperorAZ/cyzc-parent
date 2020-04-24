package com.jingliang.mall.repository;

import com.jingliang.mall.entity.UserGroup;
import com.jingliang.mall.repository.base.BaseRepository;

/**
 * 员工与组关系映射表Repository
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
public interface UserGroupRepository extends BaseRepository<UserGroup, Long> {

    /**
     * 根据UserId查询用户
     *
     * @return
     */
    public UserGroup findUserGroupByUserId();

}