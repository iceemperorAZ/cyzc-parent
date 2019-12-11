package com.jingliang.mall.authority;

import com.alibaba.fastjson.JSON;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.exception.LoginLimitException;
import com.jingliang.mall.server.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * json-post登录过滤器
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@Slf4j
public class JsonLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final RedisService redisService;
    @Value("${login.limit.prefix}")
    private String loginLimitPrefix;

    public JsonLoginAuthenticationFilter(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE) || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            try (InputStream is = request.getInputStream()) {
                User authenticationBean = JSON.parseObject(is, User.class);
                //通过redis判断登录次数是否超过限制次数
                //超过三次获取剩余登录时间，如果大于0则不允许登录并返回剩余时间，如果小于0则放行
                String ip = MallUtils.getIpAddress(request);
                Long expire = redisService.getExpire(loginLimitPrefix + authenticationBean.getLoginName() + "-" + ip);
                request.setAttribute("loginUser", authenticationBean);
                if (Objects.nonNull(expire) && expire > 0) {
                    //不允许登录
                    throw new LoginLimitException("用户[" + authenticationBean.getLoginName() + "]登录超过重试次数");
                }
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(authenticationBean.getLoginName(), authenticationBean.getPassword());
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException ignored) {
            }
            log.debug("用户信息验证失败");
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("", "");
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } else {
            return super.attemptAuthentication(request, response);
        }
    }
}