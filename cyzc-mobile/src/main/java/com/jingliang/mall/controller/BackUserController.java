package com.jingliang.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.UserReq;
import com.jingliang.mall.resp.UserResp;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.UserService;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
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
    @ApiOperation(description = "后台登录")
    @PostMapping("/back/login")
    public void backLogin(@RequestBody UserReq userReq, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        User user = userService.findByUserNo(userReq.getUserNo());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (user == null || !passwordEncoder.matches(userReq.getPassword(), user.getPassword())) {
            response.getWriter().write(JSONObject.toJSONString(Result.build(Msg.FAIL, "工号或密码错误！")));
            return;
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
        response.getWriter().write(JSONObject.toJSONString(Result.build(Msg.OK, Msg.TEXT_LOGIN_OK, BeanMapper.map(user, UserResp.class))));
    }

    /**
     * 修改密码
     */
    @PostMapping("/back/user/password")
    public Result<Boolean> modifyPassword(@RequestBody UserReq userReq, HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        String password = userReq.getPassword();
        String oldPassword = userReq.getOldPassword();
        //获取最新的用户信息
        user = userService.findById(user.getId());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return Result.build(Msg.FAIL, "原密码不正确", false);
        }
        return Result.build(Msg.OK, "修改成功", userService.modifyPassword(user.getId(), passwordEncoder.encode(password)));
    }
}
