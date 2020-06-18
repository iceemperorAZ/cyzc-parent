package com.jingliang.mall.service;

/**
 * 短信发送
 *
 * @author Zhenfeng Li
 * @date 2020-02-26 13:12:09
 */
public interface MessageService {

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    Boolean sendMessage(String phone, String code);
}
