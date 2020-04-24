package com.jingliang.mall.repository;

import com.jingliang.mall.common.Result;
import com.jingliang.mall.repository.base.BaseRepository;
import com.jingliang.mall.entity.Group;
import com.jingliang.mall.resp.GroupResp;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 组Repository
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
public interface GroupRepository extends BaseRepository<Group, Long> {

    /**
     * 组关系表Repository
     *
     * @author Xiaobing Li
     * @version 1.0.0
     * @date 2020-04-24 09:00:00
     */
    public Group findByParentGroupId (Long parentGroupId);
    /**
     * 根据父组ID编号查询可用组信息
     *
     * @param parentGroupId      父组ID
     * @param isAvailable 是否可用
     * @return 返回查询到的组信息z
     */
    public Group findGroupByParentGroupIdAndIsAvailable(@Param("parentGroupId") Long parentGroupId,@Param("isAvailable") Boolean isAvailable);
    /**
     * 根据父组ID编号查询可用组信息
     *
     * @param parentGroupId      父组ID
     * @param isAvailable 是否可用
     * @return 返回查询到的组信息z
     */
    public List<Group> findGroupsByParentGroupIdAndIsAvailable(Long parentGroupId,Boolean isAvailable);
    /**
     * 根据父组ID编号查询可用组信息
     *
     * @param parentGroupId      父组ID
     * @return 返回查询到的组信息z
     */
    public Group findGroupById(Long parentGroupId);
}