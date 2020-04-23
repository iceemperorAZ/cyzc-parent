package com.jingliang.mall.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.jingliang.mall.repository.UserGroupRepository;
import org.springframework.stereotype.Service;
import com.jingliang.mall.service.UserGroupService;

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

	public UserGroupServiceImpl (UserGroupRepository userGroupRepository) {
		this.userGroupRepository = userGroupRepository;
	}

}