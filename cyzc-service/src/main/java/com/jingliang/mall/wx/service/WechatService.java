package com.jingliang.mall.wx.service;

import com.jingliang.mall.entity.Order;

import java.util.Map;

/**
 * 写点注释
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-10-16 17:31
 */
public interface WechatService {
    /**
     * 获取openid
     *
     * @param code 登录时获取的 code
     * @return 返回获取到的openid和sessionKey
     */
    public Map getOpenId(String code);

    /**
     * 获取access_token
     *
     * @return 返回access_token
     */
    public String getAccessToken();

    /**
     * 统一下单
     *
     * @param order  订单号
     * @param openId 微信唯一标识
     * @return 返回下单后的参数
     */
    public Map<String, String> payUnifiedOrder(Order order, String openId);

    /**
     * 查询订单
     *
     * @param orderNo 订单号
     * @return 返回订单信息
     */
    public Map<String, String> payOrderQuery(String orderNo);

    /**
     * 对预支付订单进行二次签名
     *
     * @param payNo 预支付订单号
     * @return 返回订单信息
     */
    public Map<String, String> payUnifiedOrderSign(String payNo);

    /**
     * 对支付通知进行签名验证
     *
     * @param map 通知参数
     * @return 返回签名后的sign
     */
    public String payNotifySign(Map<String, String> map);
}
