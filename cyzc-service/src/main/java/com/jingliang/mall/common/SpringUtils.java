package com.jingliang.mall.common;

import org.springframework.context.ApplicationContext;

/**
 * spring工具类
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-21 12:16
 */
public class SpringUtils {
    private static ApplicationContext applicationContext = null;

    /**
     * 获取spring上下文
     *
     * @return 返回spring上下文
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 设置spring上下文
     *
     * @param webAppCtx spring上下文
     */
    public static void setApplicationContext(ApplicationContext webAppCtx) {
        if (webAppCtx != null) {
            applicationContext = webAppCtx;
        }
    }
}