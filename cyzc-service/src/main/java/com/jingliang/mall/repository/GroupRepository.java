package com.jingliang.mall.repository;

import com.jingliang.mall.entity.Group;
import com.jingliang.mall.repository.base.BaseRepository;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * 组Repository
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
public interface GroupRepository extends BaseRepository<Group, Long> {

    public Group findByParentGroupId (Long parentGroupId);

    public Group findGroupByParentGroupIdAndIsAvailable(@Param("parentGroupId") Long parentGroupId, @Param("isAvailable") Boolean isAvailable);

    public List<Group> findGroupsByParentGroupIdAndIsAvailable(Long parentGroupId,Boolean isAvailable);
    /**
     * 根据父组ID编号查询可用组信息
     *
     * @param parentGroupId      父组ID
     * @return 返回查询到的组信息z
     */
    public Group findGroupById(Long parentGroupId);
    /**
     * 根据组编号查询所在分组
     *
     * @param groupNo      组编号
     * @return 返回查询到的组信息z
     */
    public Group findGroupByGroupNoLike(String groupNo);

    /**
     * 查询所有可用的组
     * @return
     */
    List<Group> findGroupsByIsAvailable(boolean isAvailable);

}