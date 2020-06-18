package com.jingliang.mall.controller;

import com.jingliang.mall.common.*;
import com.jingliang.mall.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 验证码Controller
 *
 * @author Zhenfeng Li
 * @date 2020-03-19 09:07:14
 */
@RestController
@Slf4j
public class VerificationController {


    private final RedisTemplate<String, Object> redisTemplate;
    private final MessageService messageService;

    public VerificationController(RedisTemplate<String, Object> redisTemplate, MessageService messageService) {
        this.redisTemplate = redisTemplate;
        this.messageService = messageService;
    }

    /**
     * 发送短信
     */
    @GetMapping("/verification/msg")
    public Result<Boolean> verification(String phone, String key, HttpServletRequest request) {
        String address = MUtils.getIpAddress(request);
        log.info("短信验证码请求ip:{}", address);
        log.info("获取到手机号" + phone);
        if (!MUtils.phoneCheck(phone)) {
            return Result.build(Msg.FAIL, "手机号格式错误");
        }
        //时间间隔计时60秒
        Long expire = redisTemplate.opsForValue().getOperations().getExpire(Constant.PHONE_C0DE_INTERVAL_PREFIX + phone);
        if (expire != null && expire > 0) {
            log.error("[{}]发送过于频繁" + phone);
            return Result.build(Msg.FAIL, "操作过于频繁，请稍后重试");
        }
        Object value = redisTemplate.opsForValue().get(Constant.VERIFICATION_IMAGE_TOKEN + phone);
        if (StringUtils.isBlank(key)) {
            log.error("图片验证失败,key为空" + phone);
            return Result.build(Msg.FAIL, "请先进行验证");
        }
        if (!key.equals(value)) {
            return Result.build(Msg.FAIL, "请先进行验证");
        }
        Object limit = redisTemplate.opsForValue().get(Constant.LIMIT_PREFIX + phone);
        if (limit != null) {
            if ((int) limit > Constant.PHONE_LIMIT) {
                log.info("[{}]今日注册次数[{}],超过今天注册次数", phone, limit);
                return Result.build(Msg.FAIL, "超过今天注册次数", false);
            }
        } else {
            // 86400=24小时
            redisTemplate.opsForValue().set(Constant.LIMIT_PREFIX + phone, 1, 86400, TimeUnit.SECONDS);
        }
        log.info("[{}]今日注册次数[{}]", phone, limit == null ? 1 : limit);
        redisTemplate.opsForValue().increment(Constant.LIMIT_PREFIX + phone, 1);
        //验证码
        String code = MUtils.generateString(6);
        log.info("[{}]的验证码：[{}]", phone, code);
        //发送短信验证码
        if (!messageService.sendMessage(phone, code)) {
            return Result.build(Msg.FAIL, "短信验证码发送失败，请稍后重试", false);
        }
        //每个图片验证码只能使用一次，用完删除
        redisTemplate.delete(Constant.VERIFICATION_IMAGE_TOKEN + phone);
        //时间间隔计时60秒
        redisTemplate.opsForValue().set(Constant.PHONE_C0DE_INTERVAL_PREFIX + phone, phone, 60, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(Constant.CODE_PREFIX + phone, code, Constant.DURATION, TimeUnit.SECONDS);
        return Result.build(Msg.OK, "短信发送成功", true);
    }

    /**
     * 图片验证码
     */
    @GetMapping("/verification/img")
    public Result<Map<String, String>> verificationImage(String phone) {
        if (StringUtils.isBlank(phone)) {
            return Result.build(Msg.FAIL, "手机号为空");
        }
        if (!MUtils.phoneCheck(phone)) {
            return Result.build(Msg.FAIL, "手机号格式错误");
        }
        int y = 50;
        Map<String, String> resultMap = VerificationImage.createImage(y);
        resultMap.put("y", "" + y);
        String x = resultMap.get("x");
        redisTemplate.opsForValue().set(Constant.VERIFICATION_IMAGE + phone, x, 300, TimeUnit.SECONDS);
        log.info("图片验证距离：{}", x);
        resultMap.remove("x");
        return Result.buildQueryOk(resultMap);
    }

    /**
     * 校验图片验证码
     */
    @GetMapping("/verification/img/check")
    public Result<String> verificationImageChicken(String phone, Integer x) {
        log.debug("获取到手机号" + phone);
        if (!MUtils.phoneCheck(phone)) {
            return Result.build(Msg.FAIL, "手机号格式错误");
        }
        Object value = redisTemplate.opsForValue().get(Constant.VERIFICATION_IMAGE + phone);
        //验证完清除
        redisTemplate.delete(Constant.VERIFICATION_IMAGE + phone);
        if (value == null) {
            return Result.build(Msg.FAIL, "图片验证失败");
        }
        int l = Integer.parseInt((String) value);
        if (Math.abs(x - l) > 3) {
            return Result.build(Msg.FAIL, "图片验证失败");
        }
        if (StringUtils.isBlank(phone)) {
            return Result.build(Msg.FAIL, "手机号为空");
        }
        String key = MUtils.generateString(32);
        redisTemplate.opsForValue().set(Constant.VERIFICATION_IMAGE_TOKEN + phone, key, 300, TimeUnit.SECONDS);
        log.info("图片验证Token：{}", key);
        return Result.build(Msg.OK, "图片验证成功", key);
    }
}
