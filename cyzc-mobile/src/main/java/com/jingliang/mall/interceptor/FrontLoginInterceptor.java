package com.jingliang.mall.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.common.JwtUtil;
import com.jingliang.mall.common.MallConstant;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Buyer;
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
 * 后端登录拦截器
 * <br/>
 * Created in 2019-03-04 23:11:47
 * <br/>
 *
 * @author Li Zhenfeng
 */
@Slf4j
@Component
public class FrontLoginInterceptor implements HandlerInterceptor {
    @Value("${session.buyer.key}")
    private String buyerSession;
    @Value("${token.buyer.redis.prefix}")
    private String tokenBuyerPrefix;
    /**
     * 用户session过期时间
     */
    @Value("${token.timeout}")
    private Integer tokenTimeOut;
    private final RedisService redisService;

    public FrontLoginInterceptor(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        log.debug("进入前台登录拦截器,请求URI={}", request.getRequestURI());
        if (StringUtils.isBlank(token)) {
            //重置response
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(Result.build(MallConstant.TOKEN_FAIL, MallConstant.TEXT_TOKEN_INVALID_FAIL)));
            log.debug("用户token失效");
            return false;
        }
        Map<String, String> map = JwtUtil.verifyToken(token);
        if (Objects.isNull(map)) {
            //重置response
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(Result.build(MallConstant.TOKEN_FAIL, MallConstant.TEXT_TOKEN_INVALID_FAIL)));
            log.debug("用户token失效");
            return false;
        }
        Buyer buyer = redisService.get(tokenBuyerPrefix + map.get("id"), Buyer.class);
        if (Objects.isNull(buyer) || !StringUtils.equals(buyer.getToken(), token)) {
            //重置response
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(Result.build(MallConstant.TOKEN_FAIL, MallConstant.TEXT_TOKEN_INVALID_FAIL)));
            log.debug("用户token失效");
            return false;
        }
        //判断封停
        if (buyer.getIsSealUp()) {
            //重置response
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(Result.build(MallConstant.LOGIN_FAIL, MallConstant.TEXT_IS_SEAL_UP_FAIL)));
            log.debug("用户token失效");
            return false;
        }
        //token 验证通过则延长token过期时间
        redisService.setExpire(tokenBuyerPrefix + map.get("id"), tokenTimeOut);
        HttpSession session = request.getSession();
        log.debug("token验证通过用户信息为：{}", buyer);
        session.setAttribute(buyerSession, buyer);
        return true;
    }
}
