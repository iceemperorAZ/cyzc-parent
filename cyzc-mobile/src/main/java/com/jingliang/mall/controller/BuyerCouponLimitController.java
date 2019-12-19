package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallBeanMapper;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.BuyerCouponLimit;
import com.jingliang.mall.entity.ProductType;
import com.jingliang.mall.req.BuyerCouponLimitReq;
import com.jingliang.mall.resp.BuyerCouponLimitResp;
import com.jingliang.mall.service.BuyerCouponLimitService;
import com.jingliang.mall.service.ProductTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

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
@RequestMapping(value = "/front/buyerCouponLimit")
@RestController
@Api(tags = "用户优惠券使用限制")
@Slf4j
public class BuyerCouponLimitController {
    @Value("${coupon.use.limit}")
    private Integer couponUseLimit;
    /**
     * session用户Key
     */
    @Value("${session.buyer.key}")
    private String sessionBuyer;

    private final BuyerCouponLimitService buyerCouponLimitService;
    private final ProductTypeService productTypeService;

    public BuyerCouponLimitController(BuyerCouponLimitService buyerCouponLimitService, ProductTypeService productTypeService) {
        this.buyerCouponLimitService = buyerCouponLimitService;
        this.productTypeService = productTypeService;
    }

    /**
     * 根据商品分类Id集合查询全部用户优惠券使用限制
     */
    @ApiOperation(value = "根据商品分类Id集合查询全部用户优惠券使用限制")
    @GetMapping("/all")
    @ApiImplicitParam(name = "商品分类Id集合", value = "productTypeIds", required = true, paramType = "list")
    public MallResult<List<BuyerCouponLimitResp>> findAll(@ApiIgnore BuyerCouponLimitReq buyerCouponLimitReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerCouponLimitReq.getBuyerId());
        if (Objects.isNull(buyerCouponLimitReq.getProductTypeIds()) || buyerCouponLimitReq.getProductTypeIds().isEmpty()) {
            return MallResult.buildParamFail();
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        //查询所有商品分类
        List<ProductType> productTypeList = productTypeService.findAll(buyerCouponLimitReq.getProductTypeIds());
        List<BuyerCouponLimit> buyerCouponLimits = buyerCouponLimitService.findAllByBuyerIdAndProductTypeIds(buyer.getId(), buyerCouponLimitReq.getProductTypeIds());
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
        List<BuyerCouponLimitResp> buyerCouponLimitResps = MallBeanMapper.mapList(buyerCouponLimits, BuyerCouponLimitResp.class);
        log.debug("返回结果：{}", buyerCouponLimitResps);
        return MallResult.buildQueryOk(buyerCouponLimitResps);
    }
}