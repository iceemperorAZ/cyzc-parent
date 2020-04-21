package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.AttributeTypeDetail;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.service.AttributeTypeDetailService;
import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.req.AttributeTypeDetailReq;
import com.jingliang.mall.resp.AttributeTypeDetailResp;
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
 * 属性表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 16:53:10
 */
@RequestMapping("/back/attributeTypeDetail")
@RestController
@Slf4j
@Api(tags = "属性")
public class AttributeTypeDetailController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final AttributeTypeDetailService attributeTypeDetailService;

    public AttributeTypeDetailController(AttributeTypeDetailService attributeTypeDetailService) {
        this.attributeTypeDetailService = attributeTypeDetailService;
    }

    /**
     * 保存属性
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存属性")
    public Result<AttributeTypeDetailResp> save(@RequestBody AttributeTypeDetailReq attributeTypeDetailReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", attributeTypeDetailReq);
        if (Objects.isNull(attributeTypeDetailReq.getAttributeTypeId()) || StringUtils.isBlank(attributeTypeDetailReq.getAttributeName())) {
            log.debug("返回结果：{}",  Msg.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(attributeTypeDetailReq, user);
        AttributeTypeDetailResp attributeTypeDetailResp = BeanMapper.map(attributeTypeDetailService
                .save(BeanMapper.map(attributeTypeDetailReq, AttributeTypeDetail.class)), AttributeTypeDetailResp.class);
        log.debug("返回结果：{}", attributeTypeDetailResp);
        return Result.buildSaveOk(attributeTypeDetailResp);
    }

    /**
     * 分页查询全部属性
     */
    @ApiOperation(value = "分页查询全部属性")
    @GetMapping("/page/all")
    public Result<MallPage<AttributeTypeDetailResp>> pageAllCoupon(AttributeTypeDetailReq attributeTypeDetailReq) {
        log.debug("请求参数：{}", attributeTypeDetailReq);
        PageRequest pageRequest = PageRequest.of(attributeTypeDetailReq.getPage(), attributeTypeDetailReq.getPageSize());
        if (StringUtils.isNotBlank(attributeTypeDetailReq.getClause())) {
            pageRequest = PageRequest.of(attributeTypeDetailReq.getPage(), attributeTypeDetailReq.getPageSize(), Sort.by(MallUtils.separateOrder(attributeTypeDetailReq.getClause())));
        }
        Specification<AttributeTypeDetail> attributeTypeDetailSpecification = (Specification<AttributeTypeDetail>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (Objects.nonNull(attributeTypeDetailReq.getAttributeTypeId())) {
                predicateList.add(cb.equal(root.get("attributeTypeId"), attributeTypeDetailReq.getAttributeTypeId()));
            }
            if (StringUtils.isNotBlank(attributeTypeDetailReq.getAttributeName())) {
                predicateList.add(cb.like(root.get("attributeName"), "%" + attributeTypeDetailReq.getAttributeName() + "%"));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            return predicateList.isEmpty() ? null : cb.and(predicateList.toArray(new Predicate[0]));
        };
        Page<AttributeTypeDetail> attributeTypePage = attributeTypeDetailService.findAll(attributeTypeDetailSpecification, pageRequest);
        MallPage<AttributeTypeDetailResp> attributeTypeDetailRespPage = MallUtils.toMallPage(attributeTypePage, AttributeTypeDetailResp.class);
        log.debug("返回结果：{}", attributeTypeDetailRespPage);
        return Result.buildQueryOk(attributeTypeDetailRespPage);
    }
}