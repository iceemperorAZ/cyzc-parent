package com.jingliang.mall.exception;

import org.springframework.security.authentication.BadCredentialsException;

/**
 * 登录失败异常
 *
 * @author Zhenfeng Li
 * @date 2019-12-11 13:42:08
 */
public class LoginFailException extends BadCredentialsException {
    public LoginFailException(String msg) {
        super(msg);
    }

    public LoginFailException(String msg, Throwable t) {
        super(msg, t);
    }
}
