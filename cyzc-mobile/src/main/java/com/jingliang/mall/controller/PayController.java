package com.jingliang.mall.controller;

import com.jingliang.mall.amqp.producer.RabbitProducer;
import com.jingliang.mall.common.Constant;
import com.jingliang.mall.common.MUtils;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.*;
import com.jingliang.mall.req.OrderReq;
import com.jingliang.mall.service.*;
import com.jingliang.mall.wx.service.WechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 支付Controller
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-10-18 14:08
 */
@Api(tags = "支付")
@RestController
@Slf4j
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

    public PayController(OrderService orderService, OrderDetailService orderDetailService, WechatService wechatService,
                         CartService cartService, RabbitProducer rabbitProducer, BuyerService buyerService, GoldLogService goldLogService,
                         TechargeService techargeService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.wechatService = wechatService;
        this.cartService = cartService;
        this.rabbitProducer = rabbitProducer;
        this.buyerService = buyerService;
        this.goldLogService = goldLogService;
        this.techargeService = techargeService;
    }

    /**
     * 微信支付回调接口
     */
    @ApiIgnore
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
    @ApiOperation("订单继续微信支付")
    @PostMapping("/wechat/pay")
    public Result<Map<String, String>> wechatPay(@RequestBody OrderReq orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq.getId());
        if (Objects.isNull(orderReq.getId())) {
            return Result.buildParamFail();
        }
        Buyer buyer = (Buyer) session.getAttribute(buyerSessionKey);
        Order order = orderService.findByIdAndBuyerId(orderReq.getId(), buyer.getId());
        if (Objects.isNull(order)) {
            return Result.build(Constant.PAY_FAIL, Constant.TEXT_PAY_NOTHINGNESS_FAIL);
        }
        //判断订单状态
        if (order.getOrderStatus() == 100) {
            String prepayId = order.getPayNo();
            if (StringUtils.isNotBlank(prepayId)) {
                Map<String, String> map = wechatService.payUnifiedOrderSign(prepayId);
                return Result.build(Constant.OK, Constant.TEXT_OK, map);
            }
            return Result.build(Constant.PAY_FAIL, Constant.TEXT_PAY_OVERTIME_FAIL);
        }
        return Result.build(Constant.PAY_FAIL, Constant.TEXT_PAY_PAID);
    }

    /**
     * 微信充值支付回调接口
     */
    @ApiIgnore
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

}
