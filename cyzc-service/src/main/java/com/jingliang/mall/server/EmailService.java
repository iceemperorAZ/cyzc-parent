package com.jingliang.mall.server;

import javax.mail.MessagingException;

/**
 * 邮件发送服务
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-10-05 18:00
 */
public interface EmailService {

    /**
     * 发送简单邮件
     *
     * @param sendTo  收件人地址
     * @param subject 邮件标题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String sendTo, String subject, String content);

    /**
     * 发送简单邮件
     *
     * @param sendTo  收件人地址
     * @param subject 邮件标题
     * @param content 邮件内容
     */
    public void sendHtmlMail(String sendTo, String subject, String content);
}
