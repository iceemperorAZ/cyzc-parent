package com.jingliang.mall.ali.service;

import com.alipay.api.AlipayApiException;
import com.jingliang.mall.common.ResultMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AliPayService {
    /**
     * @param orderNo: 订单编号
     * @param amount:  实际支付金额
     * @return
     * @Description: 创建支付宝订单
     * @Author:
     */
    String createOrder(String orderNo, double amount) throws AlipayApiException;

    /**
     * @return
     * @Description:
     * @Author:
     */
    boolean notify(Map<String,String> params);

    /**
     * @param request
     * @return
     * @Description: 校验签名
     * @Author:
     */
    boolean rsaCheckV1(HttpServletRequest request);

    /**
     * @param orderNo:      订单编号
     * @param amount:       实际支付金额
     * @param refundReason: 退款原因
     * @return
     * @Description: 退款
     * @Author:
     */
    ResultMap refund(String orderNo, double amount, String refundReason);
}
