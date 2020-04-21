package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.BuyerCoupon;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.BuyerCouponReq;
import com.jingliang.mall.resp.BuyerCouponResp;
import com.jingliang.mall.service.BuyerCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

/**
 * 用户优惠券Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 15:21:18
 */
@RestController
@Slf4j
@RequestMapping("/back/buyerCoupon")
@Api(tags = "用户优惠券")
public class BuyerCouponController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final BuyerCouponService buyerCouponService;

    public BuyerCouponController(BuyerCouponService buyerCouponService) {
        this.buyerCouponService = buyerCouponService;
    }

    /**
     * 发放优惠券
     */
    @ApiOperation(value = "发放优惠券")
    @PostMapping("/save")
    public Result<BuyerCouponResp> add(@RequestBody BuyerCouponReq buyerCouponReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerCouponReq);
        buyerCouponReq.setBuyerId(buyerCouponReq.getBuyerId());
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(buyerCouponReq, user);
        BuyerCoupon buyerCoupon = BeanMapper.map(buyerCouponReq, BuyerCoupon.class);
        buyerCoupon = buyerCouponService.save(buyerCoupon);
        BuyerCouponResp buyerCouponResp = BeanMapper.map(buyerCoupon, BuyerCouponResp.class);
        log.debug("返回结果：{}", buyerCouponResp);
        return Result.buildSaveOk(buyerCouponResp);
    }
}