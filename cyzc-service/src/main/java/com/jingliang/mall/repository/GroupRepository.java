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

    public Group findByParentGroupId (Long parentGroupId);

    public Group findGroupByParentGroupIdAndIsAvailable(@Param("parentGroupId") Long parentGroupId,@Param("isAvailable") Boolean isAvailable);

    public List<Group> findGroupsByParentGroupIdAndIsAvailable(Long parentGroupId,Boolean isAvailable);
}