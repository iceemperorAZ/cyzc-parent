package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Constant;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.Cart;
import com.jingliang.mall.req.CartReq;
import com.jingliang.mall.resp.CartResp;
import com.jingliang.mall.service.CartService;
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
 * 用户购物车Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
@RequestMapping("/front/cart")
@RestController
@Slf4j
@Api(tags = "用户购物车")
public class CartController {
    @Value("${session.buyer.key}")
    private String sessionBuyer;
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * 添加购物项
     */
    @ApiOperation(value = "添加购物项")
    @PostMapping("/save")
    public Result<CartResp> save(@RequestBody CartReq cartReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", cartReq);
        if (Objects.isNull(cartReq.getProductId()) || Objects.isNull(cartReq.getProductNum())) {
            return Result.buildParamFail();
        }
        if (Objects.isNull(cartReq.getId()) && Objects.equals(cartReq.getProductNum(), 0)) {
            return Result.build(Constant.SAVE_FAIL, Constant.TEXT_CART_ITEM_NUM_FAIL);
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        MallUtils.addDateAndBuyer(cartReq, buyer);
        CartResp cartResp = BeanMapper.map(cartService.save(BeanMapper.map(cartReq, Cart.class)), CartResp.class);
        log.debug("返回结果：{}", cartResp);
        return Result.buildSaveOk(cartResp);
    }

    /**
     * 清空购物车
     */
    @ApiOperation(value = "清空购物车")
    @PostMapping("/empty")
    public Result<CartResp> emptyCart(@ApiIgnore HttpSession session) {
        log.debug("请求清空购物车");
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        cartService.emptyCart(buyer.getId());
        log.debug("清空购物车完成");
        return Result.buildOk();
    }

    /**
     * 分页查询所有购物项
     */
    @ApiOperation(value = "分页查询所有购物项")
    @GetMapping("/page/all")
    public Result<MallPage<CartResp>> pageAllCart(CartReq cartReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", cartReq);
        PageRequest pageRequest = PageRequest.of(cartReq.getPage(), cartReq.getPageSize());
        if (StringUtils.isNotBlank(cartReq.getClause())) {
            pageRequest = PageRequest.of(cartReq.getPage(), cartReq.getPageSize(), Sort.by(MallUtils.separateOrder(cartReq.getClause())));
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Specification<Cart> cartSpecification = (Specification<Cart>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            return predicateList.isEmpty() ? null : cb.and(predicateList.toArray(new Predicate[0]));
        };
        Page<Cart> cartPage = cartService.findAll(cartSpecification, pageRequest);
        MallPage<CartResp> cartResPage = MallUtils.toMallPage(cartPage, CartResp.class);
        log.debug("返回结果：{}", cartResPage);
        return Result.buildQueryOk(cartResPage);
    }
}