package com.jingliang.mall.service;

import com.jingliang.mall.entity.Group;

import java.util.List;

/**
 * 组Service
 *
 * @author XiaoBing Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
public interface GroupService {

    public Group getFatherGroup();

    public List<Group> getGroupWithFather(Long parentGroupId, Boolean isAvailable);

    public Group save(Group group);

    public Group findFartherGroup(Long parentGroupId);

    public List<Group> findAll();

    /**
     * 查询所有可用的组
     *
     * @return
     */
    List<Group> findGroupAll();

    /**
     * 删除分组
     *
     * @param group
     * @return
     */
    Boolean delete(Group group);

    /**
     * 根据组名和父组Id查询
     *
     * @param groupName
     * @param parentGroupId
     * @return
     */
    Group findByGroupNameAndParentId(String groupName, Long parentGroupId);
}