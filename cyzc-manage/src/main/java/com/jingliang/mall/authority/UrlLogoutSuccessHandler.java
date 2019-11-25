package com.jingliang.mall.authority;

import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.common.JwtUtil;
import com.jingliang.mall.common.MallConstant;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.server.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * 退出成功处理器
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@Component
@Slf4j
public class UrlLogoutSuccessHandler implements LogoutSuccessHandler {
    private final RedisService redisService;

    public UrlLogoutSuccessHandler(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        //重置response
        response.reset();
        //设置编码格式
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            log.debug("用户注销失败");
            response.getWriter().write(JSONObject.toJSONString(MallResult.build(MallConstant.FAIL, MallConstant.TEXT_LOGOUT_FAIL)));
            return;
        }
        Map<String, String> map = JwtUtil.verifyToken(token);
        if (Objects.isNull(map)) {
            log.debug("用户注销失败");
            response.getWriter().write(JSONObject.toJSONString(MallResult.build(MallConstant.FAIL, MallConstant.TEXT_LOGOUT_FAIL)));
            return;
        }
        redisService.remove(map.get("userId") + "");
        log.debug("Id=[{}]的用户注销成功", map.get("userId"));
        response.getWriter().write(JSONObject.toJSONString(MallResult.build(MallConstant.OK, MallConstant.TEXT_LOGOUT_OK)));
    }
}
