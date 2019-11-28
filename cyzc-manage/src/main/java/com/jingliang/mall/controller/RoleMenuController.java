package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallBeanMapper;
import com.jingliang.mall.common.MallConstant;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.entity.RoleMenu;
import com.jingliang.mall.entity.UserRole;
import com.jingliang.mall.req.RoleMenuReq;
import com.jingliang.mall.req.UserRoleReq;
import com.jingliang.mall.resp.RoleMenuResp;
import com.jingliang.mall.resp.RoleResp;
import com.jingliang.mall.resp.UserRoleResp;
import com.jingliang.mall.service.RoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

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
	 * 保存/修改角色资源关系
	 */
	@PostMapping("/save")
	@ApiOperation(value = "保存/修改角色资源关系")
	public MallResult<RoleMenuResp> pageAllProduct(@RequestBody RoleMenuReq roleMenuReq) {
		if (Objects.isNull(roleMenuReq.getRoleId()) || Objects.isNull(roleMenuReq.getMenuId()) || Objects.isNull(roleMenuReq.getIsAvailable())) {
			return MallResult.buildParamFail();
		}
		RoleMenu roleMenu = roleMenuService.findAllByRoleIdAndMenuId(roleMenuReq.getRoleId(), roleMenuReq.getMenuId());
		if (Objects.isNull(roleMenu) && !roleMenuReq.getIsAvailable()) {
			return MallResult.build(MallConstant.FAIL, MallConstant.TEXT_DATA_FAIL);
		}
		if (Objects.nonNull(roleMenu)) {
			roleMenuReq.setId(roleMenu.getId());
		}
		roleMenu = MallBeanMapper.map(roleMenuReq, RoleMenu.class);
		//赋权时有就更新没有就新增
		//撤权时直接设置为不可用
		RoleMenuResp roleMenuResp = MallBeanMapper.map(roleMenuService.save(roleMenu), RoleMenuResp.class);
		if (roleMenuReq.getIsAvailable()) {
			//授权成功
			return MallResult.build(MallConstant.OK, MallConstant.TEXT_AUTHORIZE_OK, roleMenuResp);
		}
		//撤权成功
		return MallResult.build(MallConstant.OK, MallConstant.TEXT_RECOVERY_AUTHORITY_OK, roleMenuResp);
	}



}