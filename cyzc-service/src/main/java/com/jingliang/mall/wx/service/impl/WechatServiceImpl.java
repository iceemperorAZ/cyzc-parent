package com.jingliang.mall.wx.service.impl;

import com.jingliang.mall.common.BaseMallUtils;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.wx.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * 写点注释
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-10-16 17:31
 */
@Service
@Slf4j
public class WechatServiceImpl implements WechatService {
    @Value("${app.login.url}")
    private String appLoginUrl;
    @Value("${app.access_token.url}")
    private String appAccessTokenUrl;
    @Value("${app.id}")
    private String appId;
    @Value("${app.secret}")
    private String secret;
    @Value("${app.pay.mch_id}")
    private String payMchId;
    @Value("${app.pay.key}")
    private String payKey;
    @Value("${app.pay.url}")
    private String payUrl;
    @Value("${pay.overtime}")
    Integer payOvertime;

    private final RestTemplate restTemplate;

    public WechatServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public Map getOpenId(String code) {
        Map forEntity = restTemplate.getForObject(appLoginUrl + "?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code", Map.class);
        log.debug("获取openId和sessionKey：{}", forEntity);
        return forEntity;
    }

    @Override
    public String getAccessToken() {
        Map forEntity = restTemplate.getForObject(appAccessTokenUrl + "?appid=" + appId + "&secret=" + secret + "&grant_type=client_credential", Map.class);
        return Objects.isNull(forEntity) ? null : (String) forEntity.get("access_token");
    }

    @Override
    public Map<String, String> payUnifiedOrder(Order order, String openId) {
        Map<String, String> map = new TreeMap<>();
        map.put("appid", appId);
        map.put("body", "晶粮商城");
        map.put("mch_id", payMchId);
        map.put("sign_type", "MD5");
        map.put("fee_type", "CNY");
        //价格元转为分
        map.put("total_fee", ((int) (order.getPayableFee() * 100)) + "");
        map.put("spbill_create_ip", BaseMallUtils.getLocalIp());
        long currentTimeMillis = System.currentTimeMillis();
        long currentTime = currentTimeMillis + payOvertime * 1000;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        map.put("time_start", dateFormat.format(new Date(currentTimeMillis)));
        map.put("time_expire", dateFormat.format(new Date(currentTime)));
        map.put("notify_url", "https://shanghaijingliang.com/cy/mobile/front/pay/wechat/notify");
        map.put("trade_type", "JSAPI");
        map.put("openid", openId);
        map.put("receipt", "Y");
        map.put("out_trade_no", order.getOrderNo());
        map.put("nonce_str", BaseMallUtils.generateString(32));
        map.put("sign", BaseMallUtils.sign(map, payKey));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        headers.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString());
        HttpEntity<String> request = new HttpEntity<>(BaseMallUtils.mapToXml(map), headers);
        String xml = restTemplate.postForObject(payUrl, request, String.class);
        Map<String, String> resultMap = BaseMallUtils.xmlToMap(xml);
        log.debug("{}", resultMap);
        if (Objects.isNull(resultMap) || Objects.isNull(resultMap.get("prepay_id"))) {
            return null;
        }
        String prepayId = resultMap.get("prepay_id");
        order.setPayNo(prepayId);
        //二次签名
        Map<String, String> newMap = new TreeMap<>();
        newMap.put("appId", appId);
        newMap.put("nonceStr", BaseMallUtils.generateString(32));
        newMap.put("package", "prepay_id=" + prepayId);
        newMap.put("signType", "MD5");
        newMap.put("timeStamp", System.currentTimeMillis() + "");
        newMap.put("paySign", BaseMallUtils.sign(newMap, payKey));
        //appId不进行传输
        newMap.remove("appId");
        return newMap;
    }

    @Override
    public Map<String, String> payOrderQuery(String orderNo) {
        Map<String, String> map = new TreeMap<>();
        map.put("appid", appId);
        map.put("mch_id", payMchId);
        map.put("out_trade_no", orderNo);
        map.put("sign_type", "MD5");
        map.put("nonce_str", BaseMallUtils.generateString(32));
        String sign = BaseMallUtils.sign(map, payKey);
        map.put("sign", sign);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        headers.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString());
        HttpEntity<String> request = new HttpEntity<>(BaseMallUtils.mapToXml(map), headers);
        String xml = restTemplate.postForObject(payUrl, request, String.class);
        Map<String, String> resultMap = BaseMallUtils.xmlToMap(xml);
        log.debug("{}", resultMap);
        if (Objects.isNull(resultMap) || Objects.isNull(resultMap.get("return_code"))) {
            return null;
        }
        return BaseMallUtils.xmlToMap(xml);
    }

    @Override
    public Map<String, String> payUnifiedOrderSign(String payNo) {
        //二次签名
        Map<String, String> newMap = new TreeMap<>();
        newMap.put("appId", appId);
        newMap.put("nonceStr", BaseMallUtils.generateString(32));
        newMap.put("package", "prepay_id=" + payNo);
        newMap.put("signType", "MD5");
        newMap.put("timeStamp", System.currentTimeMillis() + "");
        newMap.put("paySign", BaseMallUtils.sign(newMap, payKey));
        //appId不进行传输
        newMap.remove("appId");
        return newMap;
    }

    @Override
    public String payNotifySign(Map<String, String> map) {
        //sign不参与
        map.remove("sign");
        return BaseMallUtils.sign(map, payKey);
    }
}
