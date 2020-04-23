package com.jingliang.mall.service;

import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Group;
import com.jingliang.mall.resp.GroupResp;

import java.util.List;

/**
 * 组Service
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
public interface GroupService {

    public Group getFatherGroup();

    public List<Group> getGroupWithFather(Long parentGroupId,Boolean isAvailable);

    public Group save(Group group);

    public Group findFartherGroup(Long parentGroupId);

}