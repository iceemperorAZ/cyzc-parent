package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.AttributeType;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.service.AttributeTypeService;
import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.req.AttributeTypeReq;
import com.jingliang.mall.resp.AttributeTypeResp;
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
import java.util.List;
import java.util.Objects;

/**
 * 属性分类表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 16:53:10
 */
@RequestMapping("/back/attributeType")
@RestController
@Slf4j
@Api(description = "属性分类")
public class AttributeTypeController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final AttributeTypeService attributeTypeService;

    public AttributeTypeController(AttributeTypeService attributeTypeService) {
        this.attributeTypeService = attributeTypeService;
    }

    /**
     * 保存属性分类
     */
    @PostMapping("/save")
    @ApiOperation(description = "保存属性分类")
    public Result<AttributeTypeResp> save(@RequestBody AttributeTypeReq attributeTypeReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", attributeTypeReq);
        if (Objects.isNull(attributeTypeReq.getProductTypeId()) || StringUtils.isBlank(attributeTypeReq.getAttributeTypeName())) {
            log.debug("返回结果：{}",  Msg.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(attributeTypeReq, user);
        AttributeTypeResp attributeTypeResp = BeanMapper.map(attributeTypeService.save(BeanMapper
                .map(attributeTypeReq, AttributeType.class)), AttributeTypeResp.class);
        log.debug("返回结果：{}", attributeTypeResp);
        return Result.buildSaveOk(attributeTypeResp);
    }

    /**
     * 分页查询全部属性分类
     */
    @ApiOperation(description = "分页查询全部属性分类")
    @GetMapping("/page/all")
    public Result<MallPage<AttributeTypeResp>> pageAllAttributeType(AttributeTypeReq attributeTypeReq) {
        log.debug("请求参数：{}", attributeTypeReq);
        PageRequest pageRequest = PageRequest.of(attributeTypeReq.getPage(), attributeTypeReq.getPageSize());
        if (StringUtils.isNotBlank(attributeTypeReq.getClause())) {
            pageRequest = PageRequest.of(attributeTypeReq.getPage(), attributeTypeReq.getPageSize(), Sort.by(MallUtils.separateOrder(attributeTypeReq.getClause())));
        }
        Specification<AttributeType> attributeTypeSpecification = (Specification<AttributeType>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (Objects.nonNull(attributeTypeReq.getProductTypeId())) {
                predicateList.add(cb.equal(root.get("productTypeId"),  attributeTypeReq.getProductTypeId()));
            }
            if (StringUtils.isNotBlank(attributeTypeReq.getAttributeTypeName())) {
                predicateList.add(cb.like(root.get("attributeTypeName"), "%" + attributeTypeReq.getAttributeTypeName() + "%"));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            return predicateList.isEmpty() ? null : cb.and(predicateList.toArray(new Predicate[0]));
        };
        Page<AttributeType> attributeTypePage = attributeTypeService.findAll(attributeTypeSpecification, pageRequest);
        MallPage<AttributeTypeResp> attributeTypeResPage = MallUtils.toMallPage(attributeTypePage, AttributeTypeResp.class);
        log.debug("返回结果：{}", attributeTypeResPage);
        return Result.buildQueryOk(attributeTypeResPage);
    }
    /**
     * 分页查询全部属性分类及属性值
     */
    @ApiOperation(description = "分页查询全部属性分类及属性值")
    @GetMapping("/page/sub/all")
    public Result<MallPage<AttributeTypeResp>> pageSubAllAttributeType(AttributeTypeReq attributeTypeReq) {
        log.debug("请求参数：{}", attributeTypeReq);
        PageRequest pageRequest = PageRequest.of(attributeTypeReq.getPage(), attributeTypeReq.getPageSize());
        if (StringUtils.isNotBlank(attributeTypeReq.getClause())) {
            pageRequest = PageRequest.of(attributeTypeReq.getPage(), attributeTypeReq.getPageSize(), Sort.by(MallUtils.separateOrder(attributeTypeReq.getClause())));
        }
        Specification<AttributeType> attributeTypeSpecification = (Specification<AttributeType>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (Objects.nonNull(attributeTypeReq.getProductTypeId())) {
                predicateList.add(cb.equal(root.get("productTypeId"),  attributeTypeReq.getProductTypeId()));
            }
            if (StringUtils.isNotBlank(attributeTypeReq.getAttributeTypeName())) {
                predicateList.add(cb.like(root.get("attributeTypeName"), "%" + attributeTypeReq.getAttributeTypeName() + "%"));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            return predicateList.isEmpty() ? null : cb.and(predicateList.toArray(new Predicate[0]));
        };
        Page<AttributeType> attributeTypePage = attributeTypeService.queryAll(attributeTypeSpecification, pageRequest);
        MallPage<AttributeTypeResp> attributeTypeResPage = MallUtils.toMallPage(attributeTypePage, AttributeTypeResp.class);
        log.debug("返回结果：{}", attributeTypeResPage);
        return Result.buildQueryOk(attributeTypeResPage);
    }
}