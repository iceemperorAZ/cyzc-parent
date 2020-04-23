package com.jingliang.mall.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import com.jingliang.mall.service.UserGroupService;

/**
 * 员工与组关系映射表Controller
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
@RestController
@Slf4j
@RequestMapping(value = "/back/userGroup")
@Api(tags = "员工与组关系映射表")
public class UserGroupController {

	private final UserGroupService userGroupService;

	public UserGroupController (UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}

}