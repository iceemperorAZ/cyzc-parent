package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.BuyerCouponLimit;
import com.jingliang.mall.entity.ProductType;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.BuyerCouponLimitReq;
import com.jingliang.mall.resp.BuyerCouponLimitResp;
import com.jingliang.mall.service.BuyerCouponLimitService;
import com.jingliang.mall.service.ProductTypeService;
import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.citrsw.annatation.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户优惠券使用限制Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-19 09:17:25
 */
@RequestMapping(value = "/back/buyerCouponLimit")
@RestController
@Api(description = "用户优惠券使用限制")
@Slf4j
public class BuyerCouponLimitController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    @Value("${coupon.use.limit}")
    private Integer couponUseLimit;

    private final BuyerCouponLimitService buyerCouponLimitService;
    private final ProductTypeService productTypeService;

    public BuyerCouponLimitController(BuyerCouponLimitService buyerCouponLimitService, ProductTypeService productTypeService) {
        this.buyerCouponLimitService = buyerCouponLimitService;
        this.productTypeService = productTypeService;
    }

    /**
     * 保存/修改用户优惠券使用限制
     */
   @ApiOperation(description = "保存/修改用户优惠券使用限制")
    @PostMapping("/save")
    public Result<BuyerCouponLimitResp> save(@RequestBody BuyerCouponLimitReq buyerCouponLimitReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerCouponLimitReq);
        if (Objects.isNull(buyerCouponLimitReq.getBuyerId()) || Objects.isNull(buyerCouponLimitReq.getUseLimit()) || Objects.isNull(buyerCouponLimitReq.getProductTypeId())) {
            log.debug("返回结果：{}", Msg.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        BuyerCouponLimit buyerCouponLimit = buyerCouponLimitService.findByBuyerIdAndProductTypeId(buyerCouponLimitReq.getBuyerId(), buyerCouponLimitReq.getProductTypeId());
        if (Objects.nonNull(buyerCouponLimit) && Objects.isNull(buyerCouponLimitReq.getId())) {
            return Result.build(Msg.DATA_FAIL, Msg.TEXT_DATA_REPEAT_FAIL);
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(buyerCouponLimitReq, user);
        buyerCouponLimit = BeanMapper.map(buyerCouponLimitReq, BuyerCouponLimit.class);
        BuyerCouponLimitResp buyerCouponLimitResp = BeanMapper.map(buyerCouponLimitService.save(buyerCouponLimit), BuyerCouponLimitResp.class);
        log.debug("返回结果：{}", buyerCouponLimitResp);
        return Result.buildSaveOk(buyerCouponLimitResp);
    }

    /**
     * 根据商户Id查询全部用户优惠券使用限制
     */
   @ApiOperation(description = "根据商户Id查询全部用户优惠券使用限制")
    @GetMapping("/all")
//    @ApiImplicitParam(name = "商户Id", value = "buyerId", required = true, paramType = "Long")
    public Result<List<BuyerCouponLimitResp>> findAll(@ApiIgnore BuyerCouponLimitReq buyerCouponLimitReq) {
        log.debug("请求参数：{}", buyerCouponLimitReq.getBuyerId());
        if (Objects.isNull(buyerCouponLimitReq.getBuyerId())) {
            return Result.buildParamFail();
        }
        //查询所有商品分类
        List<ProductType> productTypeList = productTypeService.findAll();
        List<BuyerCouponLimit> buyerCouponLimits = buyerCouponLimitService.findAllByBuyerId(buyerCouponLimitReq.getBuyerId());
        List<Long> productTypeIds = buyerCouponLimits.stream().map(BuyerCouponLimit::getProductTypeId).collect(Collectors.toList());
        for (ProductType productType : productTypeList) {
            if (productTypeIds.contains(productType.getId())) {
                continue;
            }
            BuyerCouponLimit buyerCouponLimit = new BuyerCouponLimit();
            buyerCouponLimit.setBuyerId(buyerCouponLimitReq.getBuyerId());
            buyerCouponLimit.setProductTypeId(productType.getId());
            buyerCouponLimit.setProductType(productType);
            buyerCouponLimit.setUseLimit(couponUseLimit);
            buyerCouponLimits.add(buyerCouponLimit);
        }
        List<BuyerCouponLimitResp> buyerCouponLimitResps = BeanMapper.mapList(buyerCouponLimits, BuyerCouponLimitResp.class);
        log.debug("返回结果：{}", buyerCouponLimitResps);
        return Result.buildQueryOk(buyerCouponLimitResps);
    }
}