package com.jingliang.mall.exception;

import org.springframework.security.authentication.BadCredentialsException;

/**
 * 登录限制异常
 *
 * @author Zhenfeng Li
 * @date 2019-12-11 13:42:08
 */
public class LoginLimitException extends BadCredentialsException {
    public LoginLimitException(String msg) {
        super(msg);
    }

    public LoginLimitException(String msg, Throwable t) {
        super(msg, t);
    }
}
