package com.jingliang.mall.authority;

import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.common.MallConstant;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.exception.LoginLimitException;
import com.jingliang.mall.server.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 登陆失败处理器
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@Component
@Slf4j
public class UrlLoginFailHandler implements AuthenticationFailureHandler {
    @Value("${login.fail.count.prefix}")
    private String loginFailCountPrefix;
    @Value("${login.limit.prefix}")
    private String loginLimitPrefix;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final RedisService redisService;

    public UrlLoginFailHandler(RedisService redisService) {
        this.redisService = redisService;
    }


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        //登录失败，获取用户登录的Ip和登录名
        //判断是否超过三次
        //判断超过三次的限制登录5分钟
        //判断超过四次的限制登录30分钟
        //判断超过五次的限制登录60分钟
        //判断超过六次的限制登录24小时
        //记录方式通过redis进行计数判定
        //redis中Key格式 LOGIN-FAIL-COUNT-[loginName]ip
        String ip = MallUtils.getIpAddress(request);
        User failUser = (User) request.getAttribute("loginUser");
        String loginName = failUser.getLoginName();
        if (exception.getClass() == LoginLimitException.class) {
            //登录限制
            log.debug("登录限制拦截 = {}", exception.getMessage());
            //重置response
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(Result.build(MallConstant.LOGIN_LIMIT_FAIL, MallConstant.TEXT_LIMIT_FAIL, dateFormat.format(new Date(redisService.getExpire(loginLimitPrefix + loginName+ "-" + ip) * 1000 - 28800000)))));
            return;
        }
        //登录验证失败
        log.debug("登录验证失败拦截 = {}", exception.getMessage());
        //登录验证失败
        //重置response
        response.reset();
        //设置编码格式
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        Long count = redisService.increment(loginFailCountPrefix + loginName + "-" + ip, 1);
        if (count == 3) {
            redisService.setExpire(loginLimitPrefix + loginName+ "-" + ip, 0, 300);
            response.getWriter().write(JSONObject.toJSONString(Result.build(MallConstant.LOGIN_LIMIT_FAIL, MallConstant.TEXT_LIMIT_FAIL, dateFormat.format(new Date(redisService.getExpire(loginLimitPrefix + loginName+ "-" + ip) * 1000 - 28800000)))));
        } else if (count == 4) {
            redisService.setExpire(loginLimitPrefix + loginName+ "-" + ip, 0,1800);
            response.getWriter().write(JSONObject.toJSONString(Result.build(MallConstant.LOGIN_LIMIT_FAIL, MallConstant.TEXT_LIMIT_FAIL, dateFormat.format(new Date(redisService.getExpire(loginLimitPrefix + loginName+ "-" + ip) * 1000 - 28800000)))));
        } else if (count == 5) {
            redisService.setExpire(loginLimitPrefix + loginName+ "-" + ip, 0,3600);
        } else if (count > 5) {
            redisService.setExpire(loginLimitPrefix + loginName+ "-" + ip, 0,86400);
            response.getWriter().write(JSONObject.toJSONString(Result.build(MallConstant.LOGIN_LIMIT_FAIL, MallConstant.TEXT_LIMIT_FAIL, dateFormat.format(new Date(redisService.getExpire(loginLimitPrefix + loginName+ "-" + ip) * 1000 - 28800000)))));
        } else {
            response.getWriter().write(JSONObject.toJSONString(Result.build(MallConstant.LOGIN_FAIL, MallConstant.TEXT_LOGIN_FAIL)));
        }
    }

}
