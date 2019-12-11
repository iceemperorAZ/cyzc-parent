package com.jingliang.mall.authority;

import com.jingliang.mall.common.MallConstant;
import com.jingliang.mall.exception.LoginFailException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录验证类
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@Component
public class MallAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private final UserDetailsService userDetailsService;

    public MallAuthenticationProvider(@Qualifier("userDetailService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //表单输入的用户名
        String loginName = (String) authentication.getPrincipal();
        //表单输入的密码
        String password = (String) authentication.getCredentials();
        UserDetails userInfo = userDetailsService.loadUserByUsername(loginName);
        boolean matches = new BCryptPasswordEncoder().matches(password, userInfo.getPassword());
        if (!matches) {
            throw new LoginFailException(MallConstant.TEXT_LOGIN_FAIL);
        }
        return new UsernamePasswordAuthenticationToken(loginName, userInfo.getPassword(), userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
