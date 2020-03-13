package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Constant;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.RoleMenu;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.RoleMenuReq;
import com.jingliang.mall.resp.RoleMenuResp;
import com.jingliang.mall.service.RoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.Date;
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
	/**
	 * session用户Key
	 */
	@Value("${session.user.key}")
	private String sessionUser;
	private final RoleMenuService roleMenuService;

	public RoleMenuController (RoleMenuService roleMenuService) {
		this.roleMenuService = roleMenuService;
	}

	/**
	 * 保存/修改角色资源关系
	 */
	@PostMapping("/save")
	@ApiOperation(value = "保存/修改角色资源关系")
	public Result<RoleMenuResp> pageAllProduct(@RequestBody RoleMenuReq roleMenuReq, @ApiIgnore HttpSession session) {
		Boolean isAvailable = roleMenuReq.getIsAvailable();
		if (Objects.isNull(roleMenuReq.getRoleId()) || Objects.isNull(roleMenuReq.getMenuId()) || Objects.isNull(isAvailable)) {
			return Result.buildParamFail();
		}
		RoleMenu roleMenu = roleMenuService.findAllByRoleIdAndMenuId(roleMenuReq.getRoleId(), roleMenuReq.getMenuId());
		if (Objects.isNull(roleMenu) && !isAvailable) {
			return Result.build(Constant.FAIL, Constant.TEXT_DATA_FAIL);
		}
		User user = (User) session.getAttribute(sessionUser);
		Date date = new Date();
		if (Objects.nonNull(roleMenu)) {
			roleMenuReq.setId(roleMenu.getId());
		}else{
			roleMenuReq.setCreateTime(date);
			roleMenuReq.setCreateUserId(user.getId());
			roleMenuReq.setCreateUserName(user.getUserName());
		}
		roleMenuReq.setUpdateTime(date);
		roleMenuReq.setUpdateUserId(user.getId());
		roleMenuReq.setUpdateUserName(user.getUserName());
		roleMenu = BeanMapper.map(roleMenuReq, RoleMenu.class);
		assert roleMenu != null;
		roleMenu.setIsAvailable(isAvailable);
		//赋权时有就更新没有就新增
		//撤权时直接设置为不可用
		RoleMenuResp roleMenuResp = BeanMapper.map(roleMenuService.save(roleMenu), RoleMenuResp.class);
		if (isAvailable) {
			//授权成功
			return Result.build(Constant.OK, Constant.TEXT_AUTHORIZE_OK, roleMenuResp);
		}
		//撤权成功
		return Result.build(Constant.OK, Constant.TEXT_RECOVERY_AUTHORITY_OK, roleMenuResp);
	}



}