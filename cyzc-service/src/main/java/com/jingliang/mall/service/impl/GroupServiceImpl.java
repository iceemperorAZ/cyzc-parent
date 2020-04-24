package com.jingliang.mall.service.impl;

import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Group;
import com.jingliang.mall.repository.GroupRepository;
import com.jingliang.mall.resp.GroupResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import com.jingliang.mall.service.GroupService;

import java.util.List;

/**
 * 组ServiceImpl
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
@Service
@Slf4j
public class GroupServiceImpl implements GroupService {

	private final GroupRepository groupRepository;

	public GroupServiceImpl (GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}

    @Override
    public Group getFatherGroup() {
        Group FatherGroup = groupRepository.findByParentGroupId(-1L);
        return groupRepository.findGroupByParentGroupIdAndIsAvailable(-1L,FatherGroup.getIsAvailable());
    }

    @Override
    public List<Group> getGroupWithFather(Long parentGroupId,Boolean isAvailable) {
        return groupRepository.findGroupsByParentGroupIdAndIsAvailable(parentGroupId,isAvailable);
    }
    /**
     * 根据父组ID编号查询可用组信息
     *
     * @param group      组对象
     * @return 返回查询到的组信息z
     */
    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }
    /**
     * 根据父组ID编号查询可用组信息
     *
     * @param parentGroupId      父组ID
     * @return 返回查询到的组信息z
     */
    @Override
    public Group findFartherGroup(Long parentGroupId) {
        return groupRepository.findGroupById(parentGroupId);
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }


}