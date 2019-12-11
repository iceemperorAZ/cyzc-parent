package com.jingliang.mall.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.common.MallConstant;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 密码检查拦截器
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-19 09:02:33
 */
@Slf4j
@Component
public class PassWordCheckInterceptor implements HandlerInterceptor {
    @Value("${session.user.key}")
    private String userSession;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute(userSession);
        log.debug("进入密码检查拦截器,请求URI={}", request.getRequestURI());
        if (user.getIsInitPassword()) {
            //重置response
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(MallResult.build(MallConstant.PASSWORD_FAIL, MallConstant.TEXT_PASSWORD_INIT_FAIL)));
            log.debug("用户密码为初始密码");
            return false;
        }
        return true;
    }
}
