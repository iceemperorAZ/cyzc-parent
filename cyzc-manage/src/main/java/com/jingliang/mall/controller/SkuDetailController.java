package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.SkuDetail;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.service.SkuDetailService;
import com.jingliang.mall.common.MallBeanMapper;
import com.jingliang.mall.common.MallConstant;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.req.SkuDetailReq;
import com.jingliang.mall.resp.SkuDetailResp;
import com.jingliang.mall.server.RedisService;
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
 * 库存详情Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 19:00:48
 */
@Api(tags = "库存详情")
@RequestMapping("/back/skuDetail")
@RestController
@Slf4j
public class SkuDetailController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final SkuDetailService skuDetailService;
    private final RedisService redisService;

    public SkuDetailController(SkuDetailService skuDetailService, RedisService redisService) {
        this.skuDetailService = skuDetailService;
        this.redisService = redisService;
    }

    /**
     * 追加库存
     */
    @ApiOperation("追加库存")
    @PostMapping("/save")
    public MallResult<SkuDetailResp> save(@RequestBody SkuDetailReq skuDetailReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", skuDetailReq);
        if (Objects.isNull(skuDetailReq.getExpiredTime()) || Objects.isNull(skuDetailReq.getProductionTime()) || Objects.isNull(skuDetailReq.getPurchasePrice())
                || Objects.isNull(skuDetailReq.getSkuId()) || Objects.isNull(skuDetailReq.getSkuAppendNum())) {
            log.debug("返回结果：{}", MallConstant.TEXT_PARAM_FAIL);
            return MallResult.buildParamFail();
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(skuDetailReq, user);
        skuDetailReq.setBatchNumber(redisService.getSkuBatchNumber());
        SkuDetail skuDetail = skuDetailService.save(MallBeanMapper.map(skuDetailReq, SkuDetail.class));
        if (Objects.isNull(skuDetail)) {
            return MallResult.buildSaveFail();
        }
        SkuDetailResp detailResp = MallBeanMapper.map(skuDetail, SkuDetailResp.class);
        log.debug("返回结果：{}", detailResp);
        return MallResult.buildSaveOk(detailResp);
    }

    /**
     * 分页查询全部库存详情列表
     */
    @GetMapping("/page/all")
    @ApiOperation(value = "分页查询全部库存详情列表")
    public MallResult<MallPage<SkuDetailResp>> pageAllProduct(SkuDetailReq skuDetailReq) {
        log.debug("请求参数：{}", skuDetailReq);
        PageRequest pageRequest = PageRequest.of(skuDetailReq.getPage(), skuDetailReq.getPageSize());
        if (StringUtils.isNotBlank(skuDetailReq.getClause())) {
            pageRequest = PageRequest.of(skuDetailReq.getPage(), skuDetailReq.getPageSize(), Sort.by(MallUtils.separateOrder(skuDetailReq.getClause())));
        }
        Specification<SkuDetail> skuDetailSpecification = (Specification<SkuDetail>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(skuDetailReq.getProductName())) {
                predicateList.add(cb.like(root.get("productName"), "%" + skuDetailReq.getProductName() + "%"));
            }
            if (Objects.nonNull(skuDetailReq.getProductId())) {
                predicateList.add(cb.equal(root.get("productId"), skuDetailReq.getProductId()));
            }
            if (Objects.nonNull(skuDetailReq.getSkuId())) {
                predicateList.add(cb.equal(root.get("skuId"), skuDetailReq.getSkuId()));
            }
            if (StringUtils.isNotBlank(skuDetailReq.getSkuNo())) {
                predicateList.add(cb.like(root.get("skuNo"), "%" + skuDetailReq.getSkuNo() + "%"));
            }
            if (StringUtils.isNotBlank(skuDetailReq.getBatchNumber())) {
                predicateList.add(cb.like(root.get("batchNumber"),  skuDetailReq.getBatchNumber() + "%"));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            return predicateList.isEmpty() ? null : cb.and(predicateList.toArray(new Predicate[0]));
        };
        Page<SkuDetail> skuDetailPage = skuDetailService.findAll(skuDetailSpecification, pageRequest);
        MallPage<SkuDetailResp> skuDetailRespMallPage = MallUtils.toMallPage(skuDetailPage, SkuDetailResp.class);
        log.debug("返回结果：{}", skuDetailRespMallPage);
        return MallResult.buildQueryOk(skuDetailRespMallPage);
    }
}