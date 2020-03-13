package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Constant;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.BuyerAddress;
import com.jingliang.mall.req.BuyerAddressReq;
import com.jingliang.mall.resp.BuyerAddressResp;
import com.jingliang.mall.service.BuyerAddressService;
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
 * 会员收货地址表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-23 09:33:48
 */
@RequestMapping(value = "/front/buyerAddress")
@RestController
@Slf4j
@Api(tags = "会员收货地址表")
public class BuyerAddressController {
    /**
     * session用户Key
     */
    @Value("${session.buyer.key}")
    private String sessionBuyer;
    private final BuyerAddressService buyerAddressService;

    public BuyerAddressController(BuyerAddressService buyerAddressService) {
        this.buyerAddressService = buyerAddressService;
    }

    /**
     * 保存用户收货地址
     */
    @ApiOperation("保存用户收货地址")
    @PostMapping("/save")
    public Result<BuyerAddressResp> save(@RequestBody BuyerAddressReq buyerAddressReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerAddressReq);
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        if (Objects.nonNull(buyerAddressReq.getId()) && buyerAddressService.countByIdAndBuyerId(buyerAddressReq.getId(), buyer.getId()) == 0) {
            return Result.build(Constant.ADDRESS_FAIL, Constant.TEXT_ADDRESS_NOT_EXIST_FAIL);
        }
        MallUtils.addDateAndBuyer(buyerAddressReq, buyer);
        buyerAddressReq.setBuyerId(buyer.getId());
        BuyerAddressResp buyerAddressResp = BeanMapper.map(buyerAddressService.save(BeanMapper.map(buyerAddressReq, BuyerAddress.class)), BuyerAddressResp.class);
        log.debug("返回结果：{}", buyerAddressResp);
        return Result.buildSaveOk(buyerAddressResp);
    }

    /**
     * 分页查询所有用户收货地址
     */
    @ApiOperation(value = "分页查询所有用户收货地址")
    @GetMapping("/page/all")
    public Result<MallPage<BuyerAddressResp>> pageAllCart(BuyerAddressReq buyerAddressReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerAddressReq);
        PageRequest pageRequest = PageRequest.of(buyerAddressReq.getPage(), buyerAddressReq.getPageSize());
        if (StringUtils.isNotBlank(buyerAddressReq.getClause())) {
            pageRequest = PageRequest.of(buyerAddressReq.getPage(), buyerAddressReq.getPageSize(), Sort.by(MallUtils.separateOrder(buyerAddressReq.getClause())));
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Specification<BuyerAddress> buyerAddressSpecification = (Specification<BuyerAddress>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("updateTime")));
            return query.getRestriction();
        };
        Page<BuyerAddress> buyerAddressPage = buyerAddressService.findAll(buyerAddressSpecification, pageRequest);
        MallPage<BuyerAddressResp> buyerAddressRespMallPage = MallUtils.toMallPage(buyerAddressPage, BuyerAddressResp.class);
        log.debug("返回结果：{}", buyerAddressRespMallPage);
        return Result.buildQueryOk(buyerAddressRespMallPage);
    }

    /**
     * 根据地址Id删除用户收货地址
     */
    @ApiOperation("根据地址Id删除用户收货地址")
    @PostMapping("/delete")
    public Result<BuyerAddressResp> delete(@RequestBody BuyerAddressReq buyerAddressReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerAddressReq.getId());
        if (Objects.isNull(buyerAddressReq.getId())) {
            return Result.buildParamFail();
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        MallUtils.addDateAndBuyer(buyerAddressReq, buyer);
        buyerAddressReq.setIsAvailable(false);
        BuyerAddressResp buyerAddressResp = BeanMapper.map(buyerAddressService.save(BeanMapper.map(buyerAddressReq, BuyerAddress.class)), BuyerAddressResp.class);
        log.debug("返回结果：{}", buyerAddressResp);
        return Result.buildDeleteOk(buyerAddressResp);
    }

}