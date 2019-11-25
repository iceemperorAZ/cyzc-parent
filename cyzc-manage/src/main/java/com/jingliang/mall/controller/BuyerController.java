package com.jingliang.mall.controller;

import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.service.BuyerService;
import com.jingliang.mall.req.BuyerReq;
import com.jingliang.mall.resp.BuyerResp;
import com.jingliang.mall.server.FastdfsService;
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
 * 会员表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 09:12:45
 */
@Api(tags = "会员")
@RequestMapping("/back/buyer")
@RestController("backBuyerController")
@Slf4j
public class BuyerController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final BuyerService buyerService;

    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    /**
     * 修改会员信息
     */
    @ApiOperation(value = "修改会员信息")
    @PostMapping("/save")
    public MallResult<BuyerResp> save(@RequestBody BuyerReq buyerReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerReq);
        if (Objects.isNull(buyerReq.getId())) {
            log.debug("返回结果：{}", MallConstant.TEXT_PARAM_FAIL);
            return MallResult.buildParamFail();
        }
        Buyer buyer = buyerService.findById(buyerReq.getId());
        if (Objects.isNull(buyer)) {
            return MallResult.build(MallConstant.DATA_FAIL, MallConstant.TEXT_BUYER_DATA_FAIL);
        }
        BuyerResp buyerResp = MallBeanMapper.map(buyerService.save( MallBeanMapper.map(buyerReq, Buyer.class)), BuyerResp.class);
        log.debug("返回结果：{}", buyerResp);
        return MallResult.build(MallConstant.OK, MallConstant.TEXT_UPDATE_OK, buyerResp);
    }

    /**
     * 分页查询全部会员
     */
    @GetMapping("/page/all")
    @ApiOperation(value = "分页查询全部会员")
    public MallResult<MallPage<BuyerResp>> pageAllProduct(BuyerReq buyerReq) {
        log.debug("请求参数：{}", buyerReq);
        PageRequest pageRequest = PageRequest.of(buyerReq.getPage(), buyerReq.getPageSize());
        if (StringUtils.isNotBlank(buyerReq.getClause())) {
            pageRequest = PageRequest.of(buyerReq.getPage(), buyerReq.getPageSize(), Sort.by(MallUtils.separateOrder(buyerReq.getClause())));
        }
        Specification<Buyer> buyerSpecification = (Specification<Buyer>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(buyerReq.getPhone())) {
                predicateList.add(cb.like(root.get("phone"),  buyerReq.getPhone() + "%"));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            return predicateList.isEmpty() ? null : cb.and(predicateList.toArray(new Predicate[0]));
        };
        Page<Buyer> buyerPage = buyerService.findAll(buyerSpecification, pageRequest);
        MallPage<BuyerResp> buyerRespMallPage = MallUtils.toMallPage(buyerPage, BuyerResp.class);
        log.debug("返回结果：{}", buyerRespMallPage);
        return MallResult.buildQueryOk(buyerRespMallPage);
    }

}