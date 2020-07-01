package com.jingliang.mall.controller;

import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.Coupon;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.CouponReq;
import com.jingliang.mall.resp.CouponResp;
import com.jingliang.mall.service.CouponService;
import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import com.citrsw.annatation.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 优惠券Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-25 19:09:02
 */
@Api(description = "优惠券")
@Slf4j
@RestController("backCouponController")
@RequestMapping("/back/coupon")
public class CouponController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    /**
     * 保存优惠券
     */
    @ApiOperation(description = "保存优惠券")
    @PostMapping("/save")
    public Result<CouponResp> save(@RequestBody CouponReq couponReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", couponReq);
        if (Objects.isNull(couponReq.getTotalNumber()) || Objects.isNull(couponReq.getPercentage())
                || Objects.isNull(couponReq.getReceiveNum()) || Objects.isNull(couponReq.getCouponType())
                || Objects.isNull(couponReq.getStartTime()) || Objects.isNull(couponReq.getExpirationTime())
                || Objects.isNull(couponReq.getProvideStartTime()) || Objects.isNull(couponReq.getProvideEndTime())) {
            log.debug("返回结果：{}", Msg.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        //判断时间是否符合逻辑
        if (couponReq.getProvideEndTime().compareTo(new Date()) < 0 || couponReq.getExpirationTime().compareTo(new Date()) < 0 || couponReq.getExpirationTime().compareTo(couponReq.getProvideEndTime()) < 0) {
            return Result.build(Msg.DATA_FAIL, Msg.TEXT_COUPON_SAVE_FAIL);
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(couponReq, user);
        couponReq.setProductZoneId(-1L);
        couponReq.setResidueNumber(couponReq.getTotalNumber());
        couponReq.setIsRelease(false);
        CouponResp couponResp = BeanMapper.map(couponService.save(BeanMapper.map(couponReq, Coupon.class)), CouponResp.class);
        log.debug("返回结果：{}", couponResp);
        return Result.buildSaveOk(couponResp);
    }

    /**
     * 发布/撤销优惠券
     */
    @ApiOperation(description = "发布/撤销优惠券")
    @PostMapping("/release")
    public Result<CouponResp> release(@RequestBody CouponReq couponReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", couponReq);
        if (Objects.isNull(couponReq.getId()) || Objects.isNull(couponReq.getIsRelease())) {
            log.debug("返回结果：{}", Msg.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        Coupon coupon = couponService.findById(couponReq.getId());
        if (Objects.isNull(coupon)) {
            return Result.build(Msg.DATA_FAIL, Msg.TEXT_DATA_FAIL);
        }
        coupon.setIsRelease(couponReq.getIsRelease());
        if (coupon.getIsRelease() && coupon.getProvideEndTime().compareTo(new Date()) < 0) {
            return Result.build(Msg.DATA_FAIL, Msg.TEXT_COUPON_RELEASE_FAIL);
        }
        User user = (User) session.getAttribute(sessionUser);
        coupon.setUpdateUserId(user.getId());
        coupon.setUpdateUserName(user.getUserName());
        CouponResp couponResp = BeanMapper.map(couponService.save(coupon), CouponResp.class);
        log.debug("返回结果：{}", couponResp);
        if (coupon.getIsRelease()) {
            return Result.build(Msg.OK, Msg.TEXT_RELEASE_OK, couponResp);
        } else {
            return Result.build(Msg.OK, Msg.TEXT_RECALL_OK, couponResp);
        }
    }

    /**
     * 删除优惠券
     */
    @ApiOperation(description = "删除优惠券")
    @PostMapping("/delete")
    public Result<CouponResp> delete(@RequestBody CouponReq couponReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", couponReq);
        Long id = couponReq.getId();
        if (Objects.isNull(id)) {
            log.debug("返回结果：{}", Msg.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        couponReq = new CouponReq();
        User user = (User) session.getAttribute(sessionUser);
        couponReq.setId(id);
        MallUtils.addDateAndUser(couponReq, user);
        couponReq.setIsAvailable(false);
        CouponResp couponResp = BeanMapper.map(couponService.delete(BeanMapper.map(couponReq, Coupon.class)), CouponResp.class);
        log.debug("返回结果：{}", couponResp);
        return Result.buildDeleteOk(couponResp);
    }

    /**
     * 分页查询全部优惠券
     */
    @ApiOperation(description = "分页查询全部优惠券")
    @GetMapping("/page/all")
    public Result<MallPage<CouponResp>> pageAllCoupon(CouponReq couponReq) {
        log.debug("请求参数：{}", couponReq);
        PageRequest pageRequest = PageRequest.of(couponReq.getPage(), couponReq.getPageSize());
        if (StringUtils.isNotBlank(couponReq.getClause())) {
            pageRequest = PageRequest.of(couponReq.getPage(), couponReq.getPageSize(), Sort.by(MallUtils.separateOrder(couponReq.getClause())));
        }
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
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            return predicateList.isEmpty() ? null : cb.and(predicateList.toArray(new Predicate[0]));
        };
        Page<Coupon> couponPage = couponService.findAll(couponSpecification, pageRequest);
        MallPage<CouponResp> couponRespPage = MallUtils.toMallPage(couponPage, CouponResp.class);
        log.debug("返回结果：{}", couponRespPage);
        return Result.buildQueryOk(couponRespPage);
    }
}