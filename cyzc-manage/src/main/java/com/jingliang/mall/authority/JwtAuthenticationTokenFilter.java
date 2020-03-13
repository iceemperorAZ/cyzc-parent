package com.jingliang.mall.authority;

import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.common.JwtUtil;
import com.jingliang.mall.common.MallConstant;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.server.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * token过滤器
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Value("${token.user.redis.prefix}")
    private String tokenUserPrefix;
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    /**
     * 用户session过期时间
     */
    @Value("${token.timeout}")
    private Integer tokenTimeOut;
    private final RedisService redisService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationTokenFilter(RedisService redisService, @Qualifier("userDetailService") UserDetailsService userDetailsService) {
        this.redisService = redisService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        Map<String, String> map = JwtUtil.verifyToken(token);
        if (Objects.nonNull(map) && Objects.nonNull(map.get("userId"))) {
            User user = redisService.get(tokenUserPrefix + map.get("userId"), User.class);
            if (Objects.nonNull(user) && StringUtils.equals(user.getToken(), token)) {
                //token 验证通过则延长token过期时间
                redisService.setExpire(tokenUserPrefix + map.get("userId"), tokenTimeOut);
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getLoginName());
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                request.getSession().setAttribute(sessionUser, user);
                super.doFilter(request, response, filterChain);
                return;
            }
        }
        //token失效处理
        //重置response
        response.reset();
        //设置编码格式
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSONObject.toJSONString(Result.build(MallConstant.TOKEN_FAIL, MallConstant.TEXT_TOKEN_INVALID_FAIL)));
    }
}
