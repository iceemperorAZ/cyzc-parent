package com.jingliang.mall.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.common.JwtUtil;
import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.server.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;

/**
 * 微信管理拦截器
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-11-01 10:43
 */
@Slf4j
@Component
public class WechatInterceptor implements HandlerInterceptor {

    @Value("${session.user.key}")
    private String userSession;
    /**
     * 用户token过期时间
     */
    @Value("${token.timeout}")
    private Integer tokenTimeOut;
    @Value("${token.user.redis.prefix}")
    private String tokenUserPrefix;
    private final RedisService redisService;

    public WechatInterceptor(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        log.debug("进入前台销售权限拦截器,请求URI={}", request.getRequestURI());
        if (StringUtils.isBlank(token)) {
            //重置response
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(Result.build(Msg.TOKEN_FAIL, Msg.TEXT_TOKEN_INVALID_FAIL)));
            log.debug("用户token失效");
            return false;
        }
        Map<String, String> map = JwtUtil.verifyToken(token);
        HttpSession session = request.getSession();
        if (Objects.isNull(map) || Objects.isNull(map.get("userId"))) {
            //重置response
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(Result.build(Msg.TOKEN_FAIL, Msg.TEXT_TOKEN_INVALID_FAIL)));
            log.debug("用户token失效");
            return false;
        }
        User user = redisService.get(tokenUserPrefix + "FRONT-" + map.get("userId"), User.class);
        if (Objects.isNull(user)) {
            //重置response
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(Result.build(Msg.TOKEN_FAIL, Msg.TEXT_TOKEN_INVALID_FAIL)));
            log.debug("用户token失效");
            return false;
        }
        //token 验证通过则延长token过期时间
        redisService.setExpire(tokenUserPrefix + "FRONT-" + map.get("userId"), tokenTimeOut);
        session.setAttribute(userSession, user);
        return true;
    }

}
