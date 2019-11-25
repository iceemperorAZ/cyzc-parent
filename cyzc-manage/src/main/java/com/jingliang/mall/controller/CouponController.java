package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.Coupon;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.service.CouponService;
import com.jingliang.mall.common.*;
import com.jingliang.mall.req.CouponReq;
import com.jingliang.mall.resp.CouponResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 优惠券Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-25 19:09:02
 */
@Api(tags = "优惠券")
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
    @ApiOperation(value = "保存优惠券")
    @PostMapping("/save")
    public MallResult<CouponResp> save(@RequestBody CouponReq couponReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", couponReq);
        if (Objects.isNull(couponReq.getTotalNumber()) || Objects.isNull(couponReq.getMoney())
                || Objects.isNull(couponReq.getUseCondition()) || Objects.isNull(couponReq.getCouponType())) {
            log.debug("返回结果：{}", MallConstant.TEXT_PARAM_FAIL);
            return MallResult.buildParamFail();
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(couponReq, user);
        couponReq.setProductZoneId(-1L);
        couponReq.setResidueNumber(couponReq.getTotalNumber());
        CouponResp couponResp = MallBeanMapper.map(couponService.save(MallBeanMapper.map(couponReq, Coupon.class)), CouponResp.class);
        log.debug("返回结果：{}", couponResp);
        return MallResult.buildSaveOk(couponResp);
    }

    /**
     * 删除优惠券
     */
    @ApiOperation(value = "删除优惠券")
    @PostMapping("/delete")
    public MallResult<CouponResp> delete(@RequestBody CouponReq couponReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", couponReq);
        Long id = couponReq.getId();
        if (Objects.isNull(id)) {
            log.debug("返回结果：{}", MallConstant.TEXT_PARAM_FAIL);
            return MallResult.buildParamFail();
        }
        couponReq = new CouponReq();
        User user = (User) session.getAttribute(sessionUser);
        couponReq.setId(id);
        MallUtils.addDateAndUser(couponReq, user);
        couponReq.setIsAvailable(false);
        CouponResp couponResp = MallBeanMapper.map(couponService.delete(MallBeanMapper.map(couponReq, Coupon.class)), CouponResp.class);
        log.debug("返回结果：{}", couponResp);
        return MallResult.buildDeleteOk(couponResp);
    }

    /**
     * 分页查询全部优惠券
     */
    @ApiOperation(value = "分页查询全部优惠券")
    @GetMapping("/page/all")
    public MallResult<MallPage<CouponResp>> pageAllCoupon(CouponReq couponReq) {
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
        return MallResult.buildQueryOk(couponRespPage);
    }

}