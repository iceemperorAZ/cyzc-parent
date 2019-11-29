package com.jingliang.mall.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.common.JwtUtil;
import com.jingliang.mall.common.MallConstant;
import com.jingliang.mall.common.MallResult;
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
public class WechatManageInterceptor implements HandlerInterceptor {

    @Value("${session.buyer.key}")
    private String buyerSession;
    @Value("${session.user.key}")
    private String userSession;
    /**
     * 用户session过期时间
     */
    @Value("${token.timeout}")
    private Integer tokenTimeOut;
    private final RedisService redisService;

    public WechatManageInterceptor(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        log.debug("进入前台管理员权限拦截器,请求URI={}", request.getRequestURI());
        if (StringUtils.isBlank(token)) {
            //重置response
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(MallResult.build(MallConstant.TOKEN_FAIL, MallConstant.TEXT_TOKEN_INVALID_FAIL)));
            log.debug("用户token失效");
            return false;
        }
        Map<String, String> map = JwtUtil.verifyToken(token);
        HttpSession session = request.getSession();
        if (Objects.isNull(map) || Objects.isNull(map.get("userId")) || Objects.isNull(redisService.get(map.get("userId") + "Front", User.class))) {
            //重置response
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(MallResult.build(MallConstant.TOKEN_FAIL, MallConstant.TEXT_TOKEN_INVALID_FAIL)));
            log.debug("用户token失效");
            return false;
        }
        User user = redisService.get(map.get("userId") + "Front", User.class);
        if (Objects.isNull(user) || Objects.isNull(user.getLevel()) || user.getLevel() < 200) {
            //重置response
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(MallResult.build(MallConstant.AUTHORITY_FAIL, MallConstant.TEXT_AUTHORITY_FAIL)));
            log.debug("权限不足");
            return false;
        }
        //token 验证通过则延长token过期时间
        redisService.setExpire(map.get("userId") + "Front", tokenTimeOut);
        session.setAttribute(userSession, user);
        return true;
    }

}
