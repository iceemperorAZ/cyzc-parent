package com.jingliang.mall.exception;

import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

/**
 * 统一异常处理
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 16:53:10
 */
@RestControllerAdvice
@Slf4j
public class MallControllerAdvice {

    /**
     * 全局异常处理
     *
     * @param e       异常
     * @param request HttpServletRequest
     */
    @ExceptionHandler(value = Exception.class)
    public Result<?> exceptionHandler(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        String uri = request.getRequestURI();
        log.error("服务器异常拦截，当前请求的uri：{}", uri);
        log.error("服务器异常拦截,错误信息=>{}", e.getMessage());
        return Result.build(Msg.SYSTEM_FAIL, Msg.TEXT_SYSTEM_FAIL);
    }

    /**
     * 请求方式错误处理
     *
     * @param e       异常
     * @param request HttpServletRequest
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Result<?> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        e.printStackTrace();
        String uri = request.getRequestURI();
        log.error("请求格式异常拦截，当前请求的uri：{}", uri);
        log.error("请求格式异常拦截,错误信息=>{}", e.getMessage());
        String method = e.getMethod();
        return Result.build(Msg.REQUEST_FAIL, Msg.TEXT_REQUEST_FAIL.replace("[#nowReq#]", "[" + method + "]")
                .replace("[#req#]", Arrays.toString(Objects.requireNonNull(e.getSupportedHttpMethods()).stream().map(Enum::name).toArray())));
    }
}
