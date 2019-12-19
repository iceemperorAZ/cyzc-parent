package com.jingliang.mall.common;

/**
 * excel异常定义
 *
 * @author Zhenfeng Li
 * @date 2019-12-16 09:46:10
 */
public class ExcelException extends RuntimeException {
    public ExcelException() {
    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }

    public ExcelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
