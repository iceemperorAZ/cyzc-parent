package com.jingliang.mall.common;

/**
 * @auther lmd
 * @date 2020/5/25
 * @Company 晶粮
 */
public class NullToString {
    /**
     * null转空字符串
     *
     * @param nullString
     * @return
     */
    public static String nullString(String nullString) {
        return null == nullString ? "" : nullString;
    }
}
