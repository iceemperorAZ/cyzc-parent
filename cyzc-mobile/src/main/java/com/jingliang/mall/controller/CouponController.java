package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.BuyerCoupon;
import com.jingliang.mall.entity.Coupon;
import com.jingliang.mall.req.CouponReq;
import com.jingliang.mall.resp.CouponResp;
import com.jingliang.mall.service.BuyerCouponService;
import com.jingliang.mall.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 优惠券Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-25 19:09:02
 */
@RestController
@Api(tags = "优惠券")
@Slf4j
@RequestMapping("/front/coupon")
public class CouponController {
    /**
     * session用户Key
     */
    @Value("${session.buyer.key}")
    private String sessionBuyer;
    private final CouponService couponService;
    private final BuyerCouponService buyerCouponService;

    public CouponController(CouponService couponService, BuyerCouponService buyerCouponService) {
        this.couponService = couponService;
        this.buyerCouponService = buyerCouponService;
    }

    /**
     * 查询全部优惠券
     */
    @ApiOperation(value = "查询全部优惠券")
    @GetMapping("/all")
    public Result<List<CouponResp>> pageAllCoupon(CouponReq couponReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", couponReq);
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Specification<Coupon> couponSpecification = (Specification<Coupon>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (Objects.nonNull(couponReq.getProductTypeId())) {
                predicateList.add(cb.equal(root.get("productTypeId"), couponReq.getProductTypeId()));
            }
            if (Objects.nonNull(couponReq.getCouponType())) {
                predicateList.add(cb.equal(root.get("couponType"), couponReq.getCouponType()));
            }
            if (Objects.nonNull(couponReq.getProductZoneId())) {
                predicateList.add(cb.equal(root.get("productZoneId"), couponReq.getProductZoneId()));
            }
            if (StringUtils.isNotBlank(couponReq.getCouponDescribe())) {
                predicateList.add(cb.like(root.get("couponDescribe"), "%" + couponReq.getCouponDescribe() + "%"));
            }
            Date date = new Date();
            //可用范围内
            predicateList.add(cb.lessThanOrEqualTo(root.get("provideStartTime"), date));
            predicateList.add(cb.greaterThanOrEqualTo(root.get("provideEndTime"), date));
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            predicateList.add(cb.equal(root.get("isRelease"), true));
            predicateList.add(cb.greaterThan(root.get("residueNumber"), 0));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("residueNumber")), cb.asc(root.get("expirationTime")));
            return query.getRestriction();
        };
        List<Coupon> couponList = couponService.findAll(couponSpecification);
        List<BuyerCoupon> buyerCoupons = buyerCouponService.findAll(buyer.getId());
        List<Long> buyerCouponIds = buyerCoupons.stream().map(BuyerCoupon::getCouponId).collect(Collectors.toList());
        List<CouponResp> couponRespList = BeanMapper.mapList(couponList, CouponResp.class);
        //处理领取优惠券
        couponRespList = couponRespList.stream().filter(couponResp -> {
            if (buyerCouponIds.contains(couponResp.getId())) {
                couponResp.setIsReceive(true);
            } else {
                couponResp.setIsReceive(false);
            }
            return true;
        }).sorted(Comparator.comparing(CouponResp::getIsReceive)).collect(Collectors.toList());
        log.debug("返回结果：{}", couponRespList);
        return Result.buildQueryOk(couponRespList);
    }

}