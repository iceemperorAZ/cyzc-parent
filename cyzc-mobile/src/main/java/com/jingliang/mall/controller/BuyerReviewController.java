package com.jingliang.mall.controller;

import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.BuyerReq;
import com.jingliang.mall.service.BuyerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.*;
import java.util.Date;

/**
 * @author lmd
 * @date 2020/6/14
 * @Company 晶粮
 */
@Api(tags = "商户审核")
@RequestMapping("/wx/buyer/review")
@RestController("buyerReviewController")
@Slf4j
public class BuyerReviewController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;

    private final BuyerService buyerService;

    public BuyerReviewController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    /**
     * 商户审核通过
     */
    @PostMapping("/success")
    @ApiOperation(value = "商户审核通过")
    public Result<Boolean> success(@RequestBody BuyerReq buyerReq, HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        Buyer buyer = buyerService.findById(buyerReq.getId());
        if (buyerReq.getBuyerStatus() > 100) {
            return Result.build(Msg.FAIL, "商户已经审核");
        }
        //获取当前日期时间，毫秒时间
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date date = Date.from(instant);
        //审核通过，商户状态为300
        buyer.setBuyerStatus(300);
        buyer.setReviewUserId(user.getId());
        buyer.setReviewTime(date);
        buyer.setReviewMsg(buyerReq.getReviewMsg());
        buyerService.save(buyer);
        return Result.build(Msg.OK, "审核已通过");
    }

    /**
     * 商户审核驳回
     */
    @PostMapping("/overrule")
    @ApiOperation(value = "商户审核驳回")
    public Result<Boolean> overrule(@RequestBody BuyerReq buyerReq, HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        Buyer buyer = buyerService.findById(buyerReq.getId());
        if (buyerReq.getBuyerStatus() > 100) {
            return Result.build(Msg.FAIL, "商户审核已驳回");
        }
        if (StringUtils.isBlank(buyerReq.getReviewMsg())) {
            return Result.build(Msg.FAIL, "审核意见不能为空");
        }
        //获取当前日期时间，毫秒时间
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date date = Date.from(instant);
        //审核驳回，商户状态为100
        buyer.setBuyerStatus(100);
        buyer.setReviewUserId(user.getId());
        buyer.setReviewTime(date);
        buyer.setReviewMsg(buyerReq.getReviewMsg());
        buyerService.save(buyer);
        return Result.build(Msg.OK, "审核已驳回");
    }

}
