package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallBeanMapper;
import com.jingliang.mall.common.MallConstant;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.entity.UserRole;
import com.jingliang.mall.req.UserRoleReq;
import com.jingliang.mall.resp.UserRoleResp;
import com.jingliang.mall.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

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

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    /**
     * 保存/修改用户角色关系
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存/修改用户角色关系")
    public MallResult<UserRoleResp> pageAllProduct(@RequestBody UserRoleReq userRoleReq) {
        if (Objects.isNull(userRoleReq.getUserId()) || Objects.isNull(userRoleReq.getRoleId()) || Objects.isNull(userRoleReq.getIsAvailable())) {
            return MallResult.buildParamFail();
        }

        UserRole userRole = userRoleService.findAllByUserIdAndRoleId(userRoleReq.getUserId(), userRoleReq.getRoleId());
        if (Objects.isNull(userRole) && !userRoleReq.getIsAvailable()) {
            return MallResult.build(MallConstant.FAIL, MallConstant.TEXT_DATA_FAIL);
        }
        if (Objects.nonNull(userRole)) {
            userRoleReq.setId(userRole.getId());
        }
        userRole = MallBeanMapper.map(userRoleReq, UserRole.class);
        //赋权时有就更新没有就新增
        //撤权时直接设置为不可用
        UserRoleResp userRoleResp = MallBeanMapper.map(userRoleService.save(userRole), UserRoleResp.class);
        if (userRoleReq.getIsAvailable()) {
            //授权成功
            return MallResult.build(MallConstant.OK, MallConstant.TEXT_AUTHORIZE_OK, userRoleResp);
        }
        //撤权成功
        return MallResult.build(MallConstant.OK, MallConstant.TEXT_RECOVERY_AUTHORITY_OK, userRoleResp);
    }

}