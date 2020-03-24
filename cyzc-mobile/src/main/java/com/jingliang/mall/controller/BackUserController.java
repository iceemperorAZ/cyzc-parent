package com.jingliang.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.UserReq;
import com.jingliang.mall.resp.UserResp;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 写点注释
 *
 * @author Zhenfeng Li
 * @date 2020-03-24 15:50:22
 */
@RestController
@Slf4j
public class BackUserController {

    @Value("${login.fail.count.prefix}")
    private String loginFailCountPrefix;
    @Value("${login.limit.prefix}")
    private String loginLimitPrefix;
    @Value("${token.user.redis.prefix}")
    private String tokenUserPrefix;
    private final UserService userService;
    private final RedisService redisService;
    /**
     * 用户session过期时间
     */
    @Value("${token.timeout}")
    private Integer tokenTimeOut;

    public BackUserController(UserService userService, RedisService redisService) {
        this.userService = userService;
        this.redisService = redisService;
    }

    /**
     * 后台登录
     */
    @ApiOperation(value = "后台登录")
    @PostMapping("/back/login")
    public Result<UserResp> backLogin(@RequestBody UserReq userReq, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        User user = userService.findByUserNo(userReq.getUserNo());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (user == null || !passwordEncoder.matches(user.getPassword(), userReq.getPassword())) {
            return Result.build(Constant.FAIL, "工号或密码错误！");
        }
        log.debug("登录成功，用户信息 = {}", user);
        Map<String, String> map = new HashMap<>(5);
        map.put("userId", user.getId() + "");
        map.put("userName", user.getUserName());
        map.put("loginName", user.getLoginName());
        //生成token
        String token = JwtUtil.genToken(map);
        user.setToken(token);
        //存入redis 有效时长为1800秒（半小时）
        redisService.setExpire(tokenUserPrefix + user.getId(), user, tokenTimeOut);
        response.setHeader("Authorization", token);
        response.getWriter().write(JSONObject.toJSONString(Result.build(Constant.OK, Constant.TEXT_LOGIN_OK, BeanMapper.map(user, UserResp.class))));
        return Result.build(Constant.OK, "登录成功", BeanMapper.map(user, UserResp.class));
    }
}
