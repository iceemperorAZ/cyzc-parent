package com.jingliang.mall.ali.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.jingliang.mall.ali.service.AliPayService;
import com.jingliang.mall.common.ResultMap;
import com.jingliang.mall.configuration.AliPayConfig;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
public class AliPayServiceImpl implements AliPayService {
    private final Logger logger = LoggerFactory.getLogger(AliPayServiceImpl.class);

    private final AliPayConfig alipayConfig;
    private final AlipayClient alipayClient;
    private final OrderService orderService;

    public AliPayServiceImpl(AliPayConfig alipayConfig, AlipayClient alipayClient, OrderService orderService) {
        this.alipayConfig = alipayConfig;
        this.alipayClient = alipayClient;
        this.orderService = orderService;
    }

    @Override
    public String createOrder(String orderNo, double amount) throws AlipayApiException {
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(orderNo);
        model.setTotalAmount(String.valueOf(amount));
        model.setProductCode("QUICK_MSECURITY_PAY");
        model.setPassbackParams("订单生成，编号为：{}".concat(orderNo));

        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest ali_request = new AlipayTradeAppPayRequest();
        ali_request.setBizModel(model);
        ali_request.setNotifyUrl(alipayConfig.getNotifyUrl());// 回调地址
        AlipayTradeAppPayResponse ali_response = alipayClient.sdkExecute(ali_request);
        //就是orderString 可以直接给客户端请求，无需再做处理。
        return ali_response.getBody();
    }

    @Override
    public boolean notify(Map<String, String> params) {
        try {
            //调用SDK验证签名
            boolean flag = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType());
            logger.info("验签结果:{}", flag);
            if (flag) {
                logger.info("支付状态为:{}", params.get("trade_status"));
                //签名通过
                if ("TRADE_SUCCESS".equals(params.get("trade_status"))) {//支付成功
                    //付款金额
                    String amount = params.get("buyer_pay_amount");
                    //商户订单号
                    String out_trade_no = params.get("out_trade_no");
                    //支付宝交易号
                    String trade_no = params.get("trade_no");
                    //公用参数（附加数据）
                    String passback_params = params.get("passback_params");
                    //@TODO 数据库操作   推送操作
                    if (StringUtils.isEmpty(passback_params)) {
                        logger.info("附加数据类型为:{}", passback_params);
                        return true;
                    } else {
                        //@TODO 预支付下单后回调的逻辑
                        return true;
                    }
                }
            }
        } catch (AlipayApiException e) {
            logger.info("==================验签失败 ！");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean rsaCheckV1(HttpServletRequest request) {
        try {
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }
            boolean verifyResult = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType());
            return verifyResult;
        } catch (AlipayApiException e) {
            logger.debug("verify sigin error, exception is:{}", e);
            return false;
        }
    }

    @Override
    public ResultMap refund(String orderNo, double amount, String refundReason) {
        if (StringUtils.isBlank(orderNo)) {
            return ResultMap.error("订单编号不能为空");
        }
        if (amount <= 0) {
            return ResultMap.error("退款金额必须大于0");
        }

        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        // 商户订单号
        model.setOutTradeNo(orderNo);
        // 退款金额
        model.setRefundAmount(String.valueOf(amount));
        // 退款原因
        model.setRefundReason(refundReason);
        // 退款订单号(同一个订单可以分多次部分退款，当分多次时必传)
        // model.setOutRequestNo(UUID.randomUUID().toString());
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
        alipayRequest.setBizModel(model);
        AlipayTradeRefundResponse alipayResponse = null;
        try {
            alipayResponse = alipayClient.execute(alipayRequest);
        } catch (AlipayApiException e) {
            logger.error("订单退款失败，异常原因:{}", e);
        }
        if (alipayResponse != null) {
            String code = alipayResponse.getCode();
            String subCode = alipayResponse.getSubCode();
            String subMsg = alipayResponse.getSubMsg();
            if ("10000".equals(code)
                    && StringUtils.isBlank(subCode)
                    && StringUtils.isBlank(subMsg)) {
                // 表示退款申请接受成功，结果通过退款查询接口查询
                // 修改用户订单状态为退款
                return ResultMap.ok("订单退款成功");
            }
            return ResultMap.error(subCode + ":" + subMsg);
        }
        return ResultMap.error("订单退款失败");
    }
}
