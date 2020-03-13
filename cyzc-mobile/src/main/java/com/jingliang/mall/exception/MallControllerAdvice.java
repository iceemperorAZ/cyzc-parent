package com.jingliang.mall.exception;

import com.jingliang.mall.common.Constant;
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
 * <br/>
 * Created in 2019-02-15 14:02
 *
 * @author Li Zhenfeng
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
        return Result.build(Constant.SYSTEM_FAIL, Constant.TEXT_SYSTEM_FAIL);
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
        return Result.build(Constant.REQUEST_FAIL, Constant.TEXT_REQUEST_FAIL.replace("[#nowReq#]", "[" + method + "]")
                .replace("[#req#]", Arrays.toString(Objects.requireNonNull(e.getSupportedHttpMethods()).stream().map(Enum::name).toArray())));
    }
}
