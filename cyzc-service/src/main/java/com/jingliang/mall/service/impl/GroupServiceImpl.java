package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Group;
import com.jingliang.mall.repository.GroupRepository;
import com.jingliang.mall.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 组ServiceImpl
 *
 * @author Mengde Liu
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-24 11:18:08
 * @date 2020-04-23 10:14:57
 */
@Service
@Slf4j
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * 查询所有可用的组
     *
     * @return
     */
    @Override
    public List<Group> findGroupAll() {
        List<Group> list = groupRepository.findGroupsByIsAvailable(true);
        System.out.println(list);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Group group) {
        group = groupRepository.findFirstGroupById(group.getId());
        //逻辑删除
        group.setIsAvailable(false);
        groupRepository.save(group);
        //判断父节点是否还有子节点
        //没有则把父节点设置为叶子节点
        Integer count = groupRepository.countAllByParentGroupIdAndIsAvailable(group.getParentGroupId(), true);
        if (count == 0) {
            Group parentGroup = groupRepository.findAllByIdAndIsAvailable(group.getParentGroupId(), true);
            parentGroup.setChild(true);
            groupRepository.save(parentGroup);
        }
        return true;
    }

    @Override
    public Group findByGroupNameAndParentId(String groupName, Long parentGroupId) {
        groupRepository.findByGroupNameAndParentId(groupName,);
        return null;
    }

    @Override
    public Group getFatherGroup() {
        Group FatherGroup = groupRepository.findByParentGroupId(-1L);
        return groupRepository.findGroupByParentGroupIdAndIsAvailable(-1L, FatherGroup.getIsAvailable());
    }

    @Override
    public List<Group> getGroupWithFather(Long parentGroupId, Boolean isAvailable) {
        return groupRepository.findGroupsByParentGroupIdAndIsAvailable(parentGroupId, true);
    }

    /**
     * 根据父组ID编号查询可用组信息
     *
     * @param group 组对象
     * @return 返回查询到的组信息z
     */
    @Override
    public Group save(Group group) {
        //查询父组，修改为非叶子节点
        if (group.getId() == null) {
            //只在新增的时候进行设置
            Group parentGroup = groupRepository.findAllByIdAndIsAvailable(group.getParentGroupId(), true);
            parentGroup.setChild(false);
            groupRepository.save(parentGroup);
        }
        return groupRepository.save(group);
    }

    /**
     * 根据父组ID编号查询可用组信息
     *
     * @param parentGroupId 父组ID
     * @return 返回查询到的组信息z
     */
    @Override
    public Group findFartherGroup(Long parentGroupId) {
        return groupRepository.findFirstGroupById(parentGroupId);
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }


}