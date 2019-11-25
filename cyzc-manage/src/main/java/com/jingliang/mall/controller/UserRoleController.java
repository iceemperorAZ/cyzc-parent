package com.jingliang.mall.controller;

import com.jingliang.mall.service.UserRoleService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户角色表Controller
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@RestController
@Api(tags = "用户角色表")
@Slf4j
@RequestMapping(value = "/back/userRole")
public class UserRoleController {

	private final UserRoleService userRoleService;

	public UserRoleController (UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}

}