package com.jingliang.mall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.jingliang.mall.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 短信服务
 *
 * @author Zhenfeng Li
 * @date 2020-02-26 13:17:04
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.sms.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.sms.sign_name}")
    private String signName;
    @Value("${aliyun.sms.template_code}")
    private String templateCode;

    @Override
    public Boolean sendMessage(String phone, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject jsonObject = JSON.parseObject(response.getData());
            if ("OK".equals(jsonObject.get("Code"))) {
                log.info("手机号[{}],发送验证码[{}],成功", phone, code);
                return true;
            }
            log.error("手机号[{}]发送注册验证码失败,Error Code:{},Error Message:", jsonObject.get("Code"), jsonObject.get("Message"));
        } catch (ClientException e) {
            log.error("手机号[{}]发送注册验证码失败\r\n{}", phone, e.getErrMsg());
        }
        return false;
    }
}
