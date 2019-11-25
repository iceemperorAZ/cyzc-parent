package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.SkuDetail;
import com.jingliang.mall.entity.SkuRecord;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.service.SkuDetailService;
import com.jingliang.mall.service.SkuRecordService;
import com.jingliang.mall.common.*;
import com.jingliang.mall.req.SkuRecordReq;
import com.jingliang.mall.resp.SkuRecordResp;
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
 * 库存记录单Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-11 11:56:20
 */
@RestController
@Api(tags = "库存记录单")
@Slf4j
@RequestMapping(value = "/back/skuRecord")
public class SkuRecordController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final SkuRecordService skuRecordService;
    private final SkuDetailService skuDetailService;

    public SkuRecordController(SkuRecordService skuRecordService, SkuDetailService skuDetailService) {
        this.skuRecordService = skuRecordService;
        this.skuDetailService = skuDetailService;
    }

    /**
     * 新建库存出入记录单
     */
    @ApiOperation(value = "新建库存出入记录单")
    @PostMapping("/save")
    public MallResult<SkuRecordResp> save(@RequestBody SkuRecordReq skuRecordReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", skuRecordReq);
        if (Objects.isNull(skuRecordReq.getSkuDetailId()) || Objects.isNull(skuRecordReq.getType()) || StringUtils.isBlank(skuRecordReq.getContent())) {
            return MallResult.buildParamFail();
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(skuRecordReq, user);
        SkuDetail skuDetail = skuDetailService.findById(skuRecordReq.getSkuDetailId());
        if (skuRecordReq.getNum() > skuDetail.getSkuResidueNum()) {
            return MallResult.build(MallConstant.SAVE_FAIL, MallConstant.TEXT_SKU_NUM_FAIL);
        }
        skuRecordReq.setProductId(skuDetail.getProductId());
        skuRecordReq.setProductName(skuDetail.getProductName());
        skuRecordReq.setSkuId(skuDetail.getSkuId());
        skuRecordReq.setSkuNo(skuDetail.getSkuNo());
        skuRecordReq.setBatchNum(skuDetail.getBatchNumber());
        skuRecordReq.setStatus(100);
        SkuRecordResp skuRecordResp = MallBeanMapper.map(skuRecordService.save(MallBeanMapper.map(skuRecordReq, SkuRecord.class)), SkuRecordResp.class);
        log.debug("返回结果：{}", skuRecordResp);
        return MallResult.buildSaveOk(skuRecordResp);
    }

    /**
     * 更新库存出入记录单状态
     */
    @ApiOperation(value = "更新库存出入记录单状态")
    @PostMapping("/update")
    public MallResult<SkuRecordResp> update(@RequestBody SkuRecordReq skuRecordReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", skuRecordReq);
        if (Objects.isNull(skuRecordReq.getStatus())) {
            return MallResult.buildParamFail();
        }
        if(skuRecordReq.getStatus()==300&&StringUtils.isBlank(skuRecordReq.getApproveOpinion())){
            return MallResult.buildParamFail();
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(skuRecordReq, user);
        SkuRecordResp skuRecordResp = MallBeanMapper.map(skuRecordService.save(MallBeanMapper.map(skuRecordReq, SkuRecord.class)), SkuRecordResp.class);
        log.debug("返回结果：{}", skuRecordResp);
        return MallResult.buildSaveOk(skuRecordResp);
    }

    /**
     * 分页查询库存出入记录列表
     */
    @ApiOperation(value = "分页查询库存出入记录列表")
    @GetMapping("/page/all")
    public MallResult<MallPage<SkuRecordResp>> pageAllProduct(SkuRecordReq skuRecordReq) {
        log.debug("请求参数：{}", skuRecordReq);
        PageRequest pageRequest = PageRequest.of(skuRecordReq.getPage(), skuRecordReq.getPageSize());
        if (StringUtils.isNotBlank(skuRecordReq.getClause())) {
            pageRequest = PageRequest.of(skuRecordReq.getPage(), skuRecordReq.getPageSize(), Sort.by(MallUtils.separateOrder(skuRecordReq.getClause())));
        }
        Specification<SkuRecord> skuRecordSpecification = (Specification<SkuRecord>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(skuRecordReq.getProductName())) {
                predicateList.add(cb.like(root.get("productName"), "%" + skuRecordReq.getProductName() + "%"));
            }
            if (Objects.nonNull(skuRecordReq.getSkuDetailId())) {
                predicateList.add(cb.equal(root.get("skuDetailId"), skuRecordReq.getSkuDetailId()));
            }
            if (Objects.nonNull(skuRecordReq.getProductId())) {
                predicateList.add(cb.equal(root.get("productId"), skuRecordReq.getProductId()));
            }
            if (Objects.nonNull(skuRecordReq.getSkuId())) {
                predicateList.add(cb.equal(root.get("skuId"), skuRecordReq.getSkuId()));
            }
            if (StringUtils.isNotBlank(skuRecordReq.getSkuNo())) {
                predicateList.add(cb.like(root.get("skuNo"), skuRecordReq.getSkuNo() + "%"));
            }
            if (StringUtils.isNotBlank(skuRecordReq.getBatchNum())) {
                predicateList.add(cb.like(root.get("batchNum"), skuRecordReq.getBatchNum() + "%"));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            return predicateList.isEmpty() ? null : cb.and(predicateList.toArray(new Predicate[0]));
        };
        Page<SkuRecord> skuRecordPage = skuRecordService.findAll(skuRecordSpecification, pageRequest);
        MallPage<SkuRecordResp> skuRecordRespMallPage = MallUtils.toMallPage(skuRecordPage, SkuRecordResp.class);
        log.debug("返回结果：{}", skuRecordRespMallPage);
        return MallResult.buildQueryOk(skuRecordRespMallPage);
    }
}