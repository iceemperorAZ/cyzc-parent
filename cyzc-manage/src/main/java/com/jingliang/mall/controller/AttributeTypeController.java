package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.AttributeType;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.service.AttributeTypeService;
import com.jingliang.mall.common.MallBeanMapper;
import com.jingliang.mall.common.MallConstant;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.req.AttributeTypeReq;
import com.jingliang.mall.resp.AttributeTypeResp;
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
 * 属性分类表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 16:53:10
 */
@RequestMapping("/back/attributeType")
@RestController
@Slf4j
@Api(tags = "属性分类")
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
    @ApiOperation(value = "保存属性分类")
    public MallResult<AttributeTypeResp> save(@RequestBody AttributeTypeReq attributeTypeReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", attributeTypeReq);
        if (Objects.isNull(attributeTypeReq.getProductTypeId()) || StringUtils.isBlank(attributeTypeReq.getAttributeTypeName())) {
            log.debug("返回结果：{}",  MallConstant.TEXT_PARAM_FAIL);
            return MallResult.buildParamFail();
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(attributeTypeReq, user);
        AttributeTypeResp attributeTypeResp = MallBeanMapper.map(attributeTypeService.save(MallBeanMapper
                .map(attributeTypeReq, AttributeType.class)), AttributeTypeResp.class);
        log.debug("返回结果：{}", attributeTypeResp);
        return MallResult.buildSaveOk(attributeTypeResp);
    }

    /**
     * 分页查询全部属性分类
     */
    @ApiOperation(value = "分页查询全部属性分类")
    @GetMapping("/page/all")
    public MallResult<MallPage<AttributeTypeResp>> pageAllAttributeType(AttributeTypeReq attributeTypeReq) {
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
        return MallResult.buildQueryOk(attributeTypeResPage);
    }
    /**
     * 分页查询全部属性分类及属性值
     */
    @ApiOperation(value = "分页查询全部属性分类及属性值")
    @GetMapping("/page/sub/all")
    public MallResult<MallPage<AttributeTypeResp>> pageSubAllAttributeType(AttributeTypeReq attributeTypeReq) {
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
        return MallResult.buildQueryOk(attributeTypeResPage);
    }
}