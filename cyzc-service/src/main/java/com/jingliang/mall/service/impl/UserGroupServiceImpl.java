package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Group;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.entity.UserGroup;
import com.jingliang.mall.repository.GroupRepository;
import com.jingliang.mall.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import com.jingliang.mall.repository.UserGroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.jingliang.mall.service.UserGroupService;

import java.util.Date;

/**
 * 员工与组关系映射表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
@Service
@Slf4j
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public UserGroupServiceImpl(UserGroupRepository userGroupRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.userGroupRepository = userGroupRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserGroup delete(UserGroup userGroup) {
        //找到需要删除用户组数据的相关用户表数据，进行删除操作
        User user = userRepository.findFirstByIdAndIsAvailable(userGroup.getUserId(), true);
        user.setGroupNo("1000000000");
        userRepository.save(user);
        return userGroupRepository.save(userGroup);
    }

    @Override
    public UserGroup saveU(User user) {
        UserGroup userGroup = new UserGroup();
        Date date = new Date();
        //先根据用户中的组编码查出所在组
        Group group = groupRepository.findFirstByGroupNoAndIsAvailable(user.getGroupNo(), true);
        //判断之前是否有操作数据，将之前的操作数据状态设置为不可用
        UserGroup userGroup1 = userGroupRepository.findUserGroupByUserId(user.getId());
        if (!userGroup1.getIsAvailable()) {
            userGroup1.setIsAvailable(false);
            userGroupRepository.save(userGroup1);
        }
        //对更新的用户数据进行保存操作
        userRepository.save(user);
        //接下来存储到用户组表中
        userGroup.setUserId(user.getId());
        userGroup.setGroupId(group.getId());
        userGroup.setCreateTime(date);
        userGroup.setIsAvailable(true);
        return userGroupRepository.save(userGroup);
    }

    /**
     * 根据用户id查询员工与组关系映射表
     *
     * @param userId
     * @return
     */
    @Override
    public UserGroup findUserByUserId(Long userId) {
        return userGroupRepository.findUserGroupByUserId(userId);
    }


    @Override
    public Page<User> findUserByGroupNo(String groupNo) {
        //创建分页数据
        Pageable pageable = PageRequest.of(0, 10, Sort.by("level"));
        Page<User> userPage = userRepository.findAllByGroupNoAndIsAvailable(groupNo, pageable, true);
        return userPage;
    }

}