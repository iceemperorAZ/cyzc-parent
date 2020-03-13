package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.Coupon;
import com.jingliang.mall.entity.Product;
import com.jingliang.mall.entity.ProductZone;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.service.CouponService;
import com.jingliang.mall.service.ProductService;
import com.jingliang.mall.service.ProductZoneService;
import com.jingliang.mall.common.*;
import com.jingliang.mall.req.ProductZoneReq;
import com.jingliang.mall.resp.ProductZoneResp;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品区表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-29 19:46:43
 */
@RequestMapping("/back/productZone")
@RestController
@Api(tags = "商品区")
@Slf4j
public class ProductZoneController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final ProductZoneService productZoneService;
    private final ProductService productService;
    private final CouponService couponService;

    public ProductZoneController(ProductZoneService productZoneService, ProductService productService, CouponService couponService) {
        this.productZoneService = productZoneService;
        this.productService = productService;
        this.couponService = couponService;
    }

    /**
     * 保存商品区
     */
    @ApiOperation(value = "添加商品区")
    @PostMapping("/save")
    public Result<ProductZoneResp> save(@RequestBody ProductZoneReq productZoneReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", productZoneReq);
        if (StringUtils.isBlank(productZoneReq.getProductZoneName())) {
            log.debug("返回结果：{}", Constant.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(productZoneReq, user);
        ProductZoneResp productZoneResp = BeanMapper.map(productZoneService.save(BeanMapper.map(productZoneReq, ProductZone.class)), ProductZoneResp.class);
        log.debug("返回结果：{}", productZoneResp);
        return Result.buildSaveOk(productZoneResp);
    }

    /**
     * 保存商品区目标列表
     */
    @ApiOperation(value = "保存商品区目标列表")
    @PostMapping("/save/details")
    public Result<ProductZoneResp> addDetails(@RequestBody ProductZoneReq productZoneReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", productZoneReq);
        if (Objects.isNull(productZoneReq.getId()) || Objects.isNull(productZoneReq.getType())) {
            log.debug("返回结果：{}", Constant.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        User user = (User) session.getAttribute(sessionUser);
        ProductZoneResp productZoneResp = BeanMapper.map(productZoneReq, ProductZoneResp.class);
        List<Long> targetIds = productZoneReq.getTargetIds();
        Date date = new Date();
        if (Objects.equals(productZoneReq.getType(), 100)) {
            List<Product> products = productService.findAllByProductZoneId(productZoneReq.getId());
            List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());
            List<Product> productList = products.stream().filter(product -> {
                if (!targetIds.contains(product.getId())) {
                    product.setProductZoneId(-1L);
                    product.setUpdateTime(date);
                    product.setUpdateUserId(user.getId());
                    product.setUpdateUserName(user.getUserName());
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
            for (Long targetId : targetIds) {
                if (!productIds.contains(targetId)) {
                    Product product = new Product();
                    product.setId(targetId);
                    product.setUpdateTime(date);
                    product.setUpdateUserId(user.getId());
                    product.setUpdateUserName(user.getUserName());
                    product.setProductZoneId(productZoneReq.getId());
                    productList.add(product);
                }
            }
            productService.saveAll(productList);
        } else if (Objects.equals(productZoneReq.getType(), 200)) {
            List<Coupon> coupons = couponService.findAllByProductZoneId(productZoneReq.getId());
            List<Long> couponIds = coupons.stream().map(Coupon::getId).collect(Collectors.toList());
            List<Coupon> couponList = coupons.stream().filter(coupon -> {
                if (!targetIds.contains(coupon.getId())) {
                    coupon.setProductZoneId(-1L);
                    coupon.setUpdateTime(date);
                    coupon.setUpdateUserId(user.getId());
                    coupon.setUpdateUserName(user.getUserName());
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
            for (Long targetId : targetIds) {
                if (!couponIds.contains(targetId)) {
                    Coupon coupon = new Coupon();
                    coupon.setId(targetId);
                    coupon.setProductZoneId(productZoneReq.getId());
                    coupon.setUpdateTime(date);
                    coupon.setUpdateUserId(user.getId());
                    coupon.setUpdateUserName(user.getUserName());
                    couponList.add(coupon);
                }
            }
           couponService.saveAll(couponList);
        }
        log.debug("返回结果：{}", productZoneResp);
        return Result.buildSaveOk(productZoneResp);
    }

    /**
     * 分页查询所有商品区
     */
    @ApiOperation(value = "分页查询所有商品区")
    @GetMapping("/page/all")
    public Result<MallPage<ProductZoneResp>> pageAll(ProductZoneReq productZoneReq) {
        log.debug("请求参数：{}", productZoneReq);
        List<Sort.Order> orders = new ArrayList<>();
        if (StringUtils.isNotBlank(productZoneReq.getClause())) {
            orders = MallUtils.separateOrder(productZoneReq.getClause());
        }
        orders.add(Sort.Order.asc("sort"));
        PageRequest pageRequest = PageRequest.of(productZoneReq.getPage(), productZoneReq.getPageSize(), Sort.by(orders));
        Specification<ProductZone> productZoneSpecification = (Specification<ProductZone>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(productZoneReq.getProductZoneName())) {
                predicateList.add(cb.like(root.get("productZoneName"), "%" + productZoneReq.getProductZoneName() + "%"));
            }
            if (Objects.nonNull(productZoneReq.getType())) {
                predicateList.add(cb.equal(root.get("type"), productZoneReq.getType()));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            return predicateList.isEmpty() ? null : cb.and(predicateList.toArray(new Predicate[0]));
        };
        Page<ProductZone> productZonePage = productZoneService.findAll(productZoneSpecification, pageRequest);
        MallPage<ProductZoneResp> productZoneRespMallPage = MallUtils.toMallPage(productZonePage, ProductZoneResp.class);
        log.debug("返回结果：{}", productZoneRespMallPage);
        return Result.buildQueryOk(productZoneRespMallPage);
    }

    /**
     * 批量删除商品区
     */
    @ApiOperation(value = "批量删除商品区")
    @PostMapping("/batch/delete")
    public Result<List<ProductZoneResp>> batchDelete(@RequestParam List<Long> ids, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", ids);
        List<ProductZone> productZones = new ArrayList<>();
        User user = (User) session.getAttribute(sessionUser);
        Date date = new Date();
        for (Long id : ids) {
            ProductZone productZone = new ProductZone();
            productZone.setId(id);
            productZone.setIsAvailable(false);
            productZone.setUpdateTime(date);
            productZone.setUpdateUserId(user.getId());
            productZone.setUpdateUserName(user.getUserName());
            productZones.add(productZone);
        }
        List<ProductZoneResp> productZoneRespList = BeanMapper.mapList(productZoneService.batchDelete(productZones), ProductZoneResp.class);
        log.debug("返回参数：{}", productZoneRespList);
        return Result.buildDeleteOk(productZoneRespList);
    }
}