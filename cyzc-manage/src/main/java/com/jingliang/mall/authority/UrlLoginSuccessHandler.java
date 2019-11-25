package com.jingliang.mall.authority;

import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.common.JwtUtil;
import com.jingliang.mall.common.MallBeanMapper;
import com.jingliang.mall.common.MallConstant;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.resp.UserResp;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登陆失败处理器
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@Component
@Slf4j
public class UrlLoginSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * 用户session过期时间
     */
    @Value("${token.timeout}")
    private Integer tokenTimeOut;
    private final UserService userService;
    private final RedisService redisService;

    public UrlLoginSuccessHandler(UserService userService, RedisService redisService) {
        this.userService = userService;
        this.redisService = redisService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        User user = userService.findUserByLoginName(authentication.getName());
        log.debug("登录成功，用户信息 = {}", user);
        Map<String, String> map = new HashMap<>(5);
        map.put("userId", user.getId() + "");
        map.put("userName", user.getUserName());
        map.put("loginName", user.getLoginName());
        //生成token
        String token = JwtUtil.genToken(map);
        user.setToken(token);
        //存入redis 有效时长为1800秒（半小时）
        redisService.setExpire(user.getId() + "", user, tokenTimeOut);
        response.setHeader("Authorization", token);
        log.debug("token={}", token);
        response.getWriter().write(JSONObject.toJSONString(MallResult.build(MallConstant.OK, MallConstant.TEXT_LOGIN_OK, MallBeanMapper.map(user, UserResp.class))));
    }

}
