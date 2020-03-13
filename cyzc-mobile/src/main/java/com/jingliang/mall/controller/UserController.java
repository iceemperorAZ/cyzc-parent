package com.jingliang.mall.controller;

import com.jingliang.mall.service.UserService;
import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.resp.UserResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
@Api(tags = "员工")
@RestController
@Slf4j
@RequestMapping("/front/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 根据员工编号查询员工信息
     */
    @ApiOperation(value = "根据员工编号查询员工信息")
    @GetMapping("/find/no")
    public Result<UserResp> findByNo(String userNo) {
        log.debug("请求参数：{}", userNo);
        if (StringUtils.isBlank(userNo)) {
            return Result.buildParamFail();
        }
        UserResp userResp = BeanMapper.map(userService.findByUserNo(userNo), UserResp.class);
        log.debug("返回结果：{}", userResp);
        return Result.buildQueryOk(userResp);
    }
}