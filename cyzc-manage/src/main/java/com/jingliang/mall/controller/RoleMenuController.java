package com.jingliang.mall.controller;

import com.jingliang.mall.service.RoleMenuService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色资源表Controller
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@RequestMapping(value = "/back/roleMenu")
@RestController
@Slf4j
@Api(tags = "角色资源表")
public class RoleMenuController {

	private final RoleMenuService roleMenuService;

	public RoleMenuController (RoleMenuService roleMenuService) {
		this.roleMenuService = roleMenuService;
	}

	/**
	 * 批量保存角色资源关系
	 */
//	public MallResult<>


}