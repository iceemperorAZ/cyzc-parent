package com.jingliang.mall.common;

/**
 * 常量
 *
 * @author Zhenfeng Li
 * @date 2020-02-20 16:10:54
 */
public interface Constant {

    /**
     * 默认日期时间格式
     */
    String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认日期格式
     */
    String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    /**
     * 图片验证吗前缀
     */
    String VERIFICATION_IMAGE = "CYZC-VERIFICATION_IMAGE-";
    /**
     * 图片验证吗token前缀
     */
    String VERIFICATION_IMAGE_TOKEN = "CYZC-VERIFICATION_IMAGE-TOKEN-";
    /**
     * 短信发送时间间隔前缀
     */
    String PHONE_C0DE_INTERVAL_PREFIX = "CYZC-PHONE-C0DE-INTERVAL-PREFIX-";
    /**
     * redis中每天注册验证码发送次数限制前缀
     */
    String LIMIT_PREFIX = "CYZC-LIMIT-";
    /**
     * redis前台用户超时自动离线前缀
     */
    String ON_LINE_USER_PREFIX = "CYZC-ON-LINE-USER-";
    /**
     * 前台用户登录长时间无操作，自动离线时长（秒）2小时
     */
    Integer ON_LINE_TIME_USER = 7200;
    /**
     * redis后台用户超时自动离线前缀
     */
    String ON_LINE_BACK_USER_PREFIX = "CYZC-ON-LINE-BACK-USER-";
    /**
     * 后台用户登录长时间无操作，自动离线时长（秒）30分钟
     */
    Integer ON_LINE_TIME_BACK_USER = 1800;
    /**
     * redis中注册验证码前缀
     */
    String CODE_PREFIX = "CYZC-CODE-";
    /**
     * ip前缀
     */
    String IP_PREFIX = "CYZC-IP-";
    /**
     * 手机号每日注册上限
     */
    Integer PHONE_LIMIT = 5;
    /**
     * 手机号验证码有效时长（秒）10分钟
     */
    Integer DURATION = 600;
    /**
     * session中前台用户的Key
     */
    String USER_KEY = "CYZC-USER-KEY";
    /**
     * session中后台用户的Key
     */
    String BACK_USER_KEY = "CYZC-BACK-USER-KEY";
}
