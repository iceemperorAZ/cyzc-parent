package com.jingliang.mall.repository;

import com.jingliang.mall.entity.Group;
import com.jingliang.mall.repository.base.BaseRepository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 组Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
public interface GroupRepository extends BaseRepository<Group, Long> {

    public Group findByParentGroupId(Long parentGroupId);

    public Group findGroupByParentGroupIdAndIsAvailable(@Param("parentGroupId") Long parentGroupId, @Param("isAvailable") Boolean isAvailable);

    public List<Group> findGroupsByParentGroupIdAndIsAvailable(Long parentGroupId, Boolean isAvailable);

    /**
     * 统计子节点数量
     *
     * @param parentGroupId
     * @param isAvailable
     * @return
     */
    public Integer countAllByParentGroupIdAndIsAvailable(Long parentGroupId, Boolean isAvailable);

    /**
     * 根据父组ID编号查询可用组信息
     *
     * @param parentGroupId 父组ID
     * @return 返回查询到的组信息z
     */
    public Group findFirstGroupById(Long parentGroupId);

    /**
     * 查询所有可用的组
     *
     * @param isAvailable
     * @return
     */
    List<Group> findGroupsByIsAvailable(boolean isAvailable);

    /**
     * 根据组名和父组Id查询
     *
     * @param groupName
     * @param parentGroupId
     * @param isAvailable
     * @return
     */
    Group findFirstByGroupNameAndParentGroupIdAndIsAvailable(String groupName, Long parentGroupId, boolean isAvailable);

    /**
     * 根据组编号查询
     *
     * @param groupNo
     * @param isAvailable
     * @return
     */
    Group findFirstByGroupNoAndIsAvailable(String groupNo, boolean isAvailable);

    /**
     * 根据分组模糊查询
     *
     * @param groupNo
     * @param isAvailable
     * @return
     */
    List<Group> findAllByGroupNoLikeAndIsAvailable(String groupNo, boolean isAvailable);
}