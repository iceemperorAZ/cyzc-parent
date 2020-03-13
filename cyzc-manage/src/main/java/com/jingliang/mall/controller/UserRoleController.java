package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.MallConstant;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.entity.UserRole;
import com.jingliang.mall.req.UserRoleReq;
import com.jingliang.mall.resp.UserRoleResp;
import com.jingliang.mall.service.UserRoleService;
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
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    /**
     * 保存/修改用户角色关系
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存/修改用户角色关系")
    public Result<UserRoleResp> pageAllProduct(@RequestBody UserRoleReq userRoleReq, @ApiIgnore HttpSession session) {
        Boolean isAvailable = userRoleReq.getIsAvailable();
        if (Objects.isNull(userRoleReq.getUserId()) || Objects.isNull(userRoleReq.getRoleId()) || Objects.isNull(isAvailable)) {
            return Result.buildParamFail();
        }

        UserRole userRole = userRoleService.findAllByUserIdAndRoleId(userRoleReq.getUserId(), userRoleReq.getRoleId());
        if (Objects.isNull(userRole) && !isAvailable) {
            return Result.build(MallConstant.FAIL, MallConstant.TEXT_DATA_FAIL);
        }
        Date date = new Date();
        User user = (User) session.getAttribute(sessionUser);
        if (Objects.nonNull(userRole)) {
            userRoleReq.setId(userRole.getId());
        }else{
            userRoleReq.setCreateTime(date);
            userRoleReq.setCreateUserId(user.getId());
            userRoleReq.setCreateUserName(user.getUserName());
        }
        userRoleReq.setUpdateTime(date);
        userRoleReq.setUpdateUserId(user.getId());
        userRoleReq.setUpdateUserName(user.getUserName());
        userRole = BeanMapper.map(userRoleReq, UserRole.class);
        assert userRole != null;
        userRole.setIsAvailable(isAvailable);
        //赋权时有就更新没有就新增
        //撤权时直接设置为不可用
        UserRoleResp userRoleResp = BeanMapper.map(userRoleService.save(userRole), UserRoleResp.class);
        if (isAvailable) {
            //授权成功
            return Result.build(MallConstant.OK, MallConstant.TEXT_AUTHORIZE_OK, userRoleResp);
        }
        //撤权成功
        return Result.build(MallConstant.OK, MallConstant.TEXT_RECOVERY_AUTHORITY_OK, userRoleResp);
    }

}