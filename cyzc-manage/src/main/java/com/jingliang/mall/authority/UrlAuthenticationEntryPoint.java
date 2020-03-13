package com.jingliang.mall.authority;

import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.common.MallConstant;
import com.jingliang.mall.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户未登录处理类
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@Component
@Slf4j
public class UrlAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e) throws IOException {
        log.debug("未登录拦截 = {}", e.getMessage());
        //重置response
        response.reset();
        //设置编码格式
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSONObject.toJSONString(Result.build(MallConstant.LOGIN_FAIL, MallConstant.TEXT_NOT_LOGIN_FAIL)));
    }
}
