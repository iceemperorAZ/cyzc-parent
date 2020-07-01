package com.jingliang.mall.controller;

import com.jingliang.mall.ali.service.AliPayService;
import com.jingliang.mall.amqp.producer.RabbitProducer;
import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.MUtils;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.common.ResultMap;
import com.jingliang.mall.entity.*;
import com.jingliang.mall.req.OrderReq;
import com.jingliang.mall.service.*;
import com.jingliang.mall.wx.service.WechatService;
import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.citrsw.annatation.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 支付Controller
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-10-18 14:08
 */
@Slf4j
@Api(description = "支付")
@RestController
@RequestMapping("/front/pay")
public class PayController {
    @Value("${session.buyer.key}")
    private String buyerSessionKey;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final WechatService wechatService;
    private final CartService cartService;
    private final RabbitProducer rabbitProducer;
    private final BuyerService buyerService;
    private final GoldLogService goldLogService;
    private final TechargeService techargeService;
    private final AliPayService aliPayService;

    public PayController(OrderService orderService, OrderDetailService orderDetailService, WechatService wechatService,
                         CartService cartService, RabbitProducer rabbitProducer, BuyerService buyerService, GoldLogService goldLogService,
                         TechargeService techargeService, AliPayService aliPayService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.wechatService = wechatService;
        this.cartService = cartService;
        this.rabbitProducer = rabbitProducer;
        this.buyerService = buyerService;
        this.goldLogService = goldLogService;
        this.techargeService = techargeService;
        this.aliPayService = aliPayService;
    }

    /**
     * 微信支付回调接口
     */
    @ApiOperation(description = "微信支付回调接口")
    @RequestMapping(value = "/wechat/notify", produces = MediaType.TEXT_XML_VALUE + ";charset=UTF-8", consumes = MediaType.TEXT_XML_VALUE + ";charset=UTF-8")
    public String wechatNotify(@RequestBody String xml) {
        log.info("微信支付异步通知返回参数：{}", xml);
        Map<String, String> map = MUtils.xmlToMap(xml);
        log.info("微信支付异步通知返回参数map：{}", map);
        assert map != null;
        String oldSign = map.get("sign");
        //对结果进行签名验证
        String sign = wechatService.payNotifySign(map);
        if (!StringUtils.equals(oldSign, sign)) {
            log.error("签名验证失败");
            //返回给微信失败的消息
            log.error("支付通知签名验证失败，返回结果：<xml><return_code><![CDATA[签名验证失败]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名验证失败]]></return_msg></xml>";
        }
        log.debug("签名验证成功");
        //查询订单
        String orderNo = map.get("out_trade_no");
        String payNo = map.get("transaction_id");
        String timeEnd = map.get("time_end");
        Order order = orderService.findByOrderNo(orderNo);
        Long totalFee = Long.parseLong(map.get("total_fee"));
        //判断支付金额和订单金额(单位：分)是否一致
        if (!Objects.equals(order.getPayableFee(), totalFee)) {
            log.error("编号为：[{}]的订单应付金额[{}]和微信支付的金额[{}]不一致", orderNo, order.getPayableFee(), totalFee);
            //返回给微信失败的消息
            log.error("支付通知金额验证失败，返回结果：<xml><return_code><![CDATA[金额验证失败]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
            return "<xml><return_code><![CDATA[FAIL]]></return_code><![CDATA[金额验证失败]]></return_msg></xml>";
        }
        //修改订单状态为已支付 ，将微信支付订单号存入订单，保存支付结束时间
        order.setOrderStatus(300);
        order.setPayNo(payNo);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            order.setPayEndTime(dateFormat.parse(timeEnd));
        } catch (ParseException e) {
            order.setPayEndTime(date);
        }
        order.setUpdateTime(date);
        orderService.update(order);
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(order.getId());
        //更新用户最后一次下单时间
        Buyer buyer = buyerService.findById(order.getBuyerId());
        buyer.setLastOrderTime(date);
        buyerService.save(buyer);

        //清空订单中的购物项
        Long buyerId = order.getBuyerId();
        List<Long> productIds = orderDetails.stream().map(OrderDetail::getProductId).collect(Collectors.toList());
        cartService.emptyCartItem(buyerId, productIds);
        //推送订单支付通知
        rabbitProducer.paymentNotice(order);
        //返回给微信成功的消息
        log.info("支付通知签名验证成功，返回结果：<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }

    /**
     * 订单继续微信支付
     */
    @ApiOperation(description = "订单继续微信支付")
    @PostMapping("/wechat/pay")
    public Result<Map<String, String>> wechatPay(@RequestBody OrderReq orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq.getId());
        if (Objects.isNull(orderReq.getId())) {
            return Result.buildParamFail();
        }
        Buyer buyer = (Buyer) session.getAttribute(buyerSessionKey);
        Order order = orderService.findByIdAndBuyerId(orderReq.getId(), buyer.getId());
        if (Objects.isNull(order)) {
            return Result.build(Msg.PAY_FAIL, Msg.TEXT_PAY_NOTHINGNESS_FAIL);
        }
        //判断订单状态
        if (order.getOrderStatus() == 100) {
            String prepayId = order.getPayNo();
            if (StringUtils.isNotBlank(prepayId)) {
                Map<String, String> map = wechatService.payUnifiedOrderSign(prepayId);
                return Result.build(Msg.OK, Msg.TEXT_OK, map);
            }
            return Result.build(Msg.PAY_FAIL, Msg.TEXT_PAY_OVERTIME_FAIL);
        }
        return Result.build(Msg.PAY_FAIL, Msg.TEXT_PAY_PAID);
    }

    /**
     * 微信充值支付回调接口
     */
    @ApiOperation(description = "微信充值支付回调接口")
    @RequestMapping(value = "/wechat/recharge", produces = MediaType.TEXT_XML_VALUE + ";charset=UTF-8", consumes = MediaType.TEXT_XML_VALUE + ";charset=UTF-8")
    public String recharge(@RequestBody String xml) {
        log.info("微信充值支付异步通知返回参数：{}", xml);
        Map<String, String> map = MUtils.xmlToMap(xml);
        log.info("微信充值支付异步通知返回参数map：{}", map);
        assert map != null;
        String oldSign = map.get("sign");
        //对结果进行签名验证
        String sign = wechatService.payNotifySign(map);
        if (!StringUtils.equals(oldSign, sign)) {
            log.error("充值支付签名验证失败");
            //返回给微信失败的消息
            log.error("充值支付通知签名验证失败，返回结果：<xml><return_code><![CDATA[签名验证失败]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名验证失败]]></return_msg></xml>";
        }
        log.debug("充值支付签名验证成功");
        //查询订单
        String orderNo = map.get("out_trade_no");
        String timeEnd = map.get("time_end");
        //将微信订单号临时存入msg字段中
        GoldLog goldLog = goldLogService.findByPayNo(orderNo);
        Integer totalFee = Integer.parseInt(map.get("total_fee"));
        //判断支付金额和订单金额(单位：分)是否一致
        if (!Objects.equals(goldLog.getMoney(), totalFee)) {
            log.error("编号为：[{}]的订单应付金额[{}]和微信支付的金额[{}]不一致", orderNo, goldLog.getMoney(), totalFee);
            //返回给微信失败的消息
            log.error("充值支付通知金额验证失败，返回结果：<xml><return_code><![CDATA[金额验证失败]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
            return "<xml><return_code><![CDATA[FAIL]]></return_code><![CDATA[金额验证失败]]></return_msg></xml>";
        }
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            goldLog.setCreateTime(dateFormat.parse(timeEnd));
        } catch (ParseException e) {
            goldLog.setCreateTime(date);
        }
        goldLog.setIsAvailable(true);
        //额外赠送
        goldLog.setMsg("充值￥" + ((totalFee * 1.00) / 100.00) + "元，获得" + goldLog.getGold() + "金币");
        goldLogService.save(goldLog);
        //返回给微信成功的消息
        log.info("充值支付通知签名验证成功，返回结果：<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }

    /**
     * 支付异步通知
     * 接收到异步通知并验签通过后，一定要检查通知内容，
     * 包括通知中的app_id、out_trade_no、total_amount是否与请求中的一致，并根据trade_status进行后续业务处理。
     * https://docs.open.alipay.com/194/103296
     */
    @ApiOperation(description = "支付异步通知")
    @RequestMapping(value = "/ali/notify", produces = MediaType.TEXT_XML_VALUE + ";charset=UTF-8", consumes = MediaType.TEXT_XML_VALUE + ";charset=UTF-8")
    public String notify(HttpServletRequest request) {
        log.debug("支付宝回调开始");
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //@TODO 业务（调用业务层的回调，那里面处理业务逻辑）
        aliPayService.notify(params);
        log.info("支付通知签名验证成功，返回结果：<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }

    @ApiOperation
    @PostMapping("/ali/refund")
    public ResultMap refund(@RequestParam String orderNo,
                            @RequestParam double amount,
                            @RequestParam(required = false) String refundReason) {
        return aliPayService.refund(orderNo, amount, refundReason);
    }

}
