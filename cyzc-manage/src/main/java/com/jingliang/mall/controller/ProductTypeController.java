package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.ProductType;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.service.ProductTypeService;
import com.jingliang.mall.common.*;
import com.jingliang.mall.req.ProductTypeReq;
import com.jingliang.mall.resp.ProductTypeResp;
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

/**
 * 商品分类表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
@RestController("backProductTypeController")
@Slf4j
@RequestMapping("/back/productType")
@Api(tags = "商品分类")
public class ProductTypeController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;

    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    /**
     * 添加商品分类
     */
    @ApiOperation(value = "添加商品分类")
    @PostMapping("/save")
    public MallResult<ProductTypeResp> saveProductTypeResp(@RequestBody ProductTypeReq productTypeReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", productTypeReq);
        if (StringUtils.isBlank(productTypeReq.getProductTypeName())) {
            log.debug("返回结果：{}",  MallConstant.TEXT_PARAM_FAIL);
            return MallResult.buildParamFail();
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(productTypeReq, user);
        ProductType productType = MallBeanMapper.map(productTypeReq, ProductType.class);
        productType = productTypeService.save(productType);
        ProductTypeResp productTypeResp = MallBeanMapper.map(productType, ProductTypeResp.class);
        log.debug("返回结果：{}", productTypeResp);
        return MallResult.build(MallConstant.OK, MallConstant.TEXT_SAVE_OK, productTypeResp);
    }

    /**
     * 分页查询所有商品分类
     */
    @ApiOperation(value = "分页查询所有商品分类")
    @GetMapping("/page/all")
    public MallResult<MallPage<ProductTypeResp>> pageAllProductTypeResp(ProductTypeReq productTypeReq) {
        log.debug("请求参数：{}", productTypeReq);
        PageRequest pageRequest = PageRequest.of(productTypeReq.getPage(), productTypeReq.getPageSize());
        if (StringUtils.isNotBlank(productTypeReq.getClause())) {
            pageRequest = PageRequest.of(productTypeReq.getPage(), productTypeReq.getPageSize(), Sort.by(MallUtils.separateOrder(productTypeReq.getClause())));
        }
        Specification<ProductType> productTypeSpecification = (Specification<ProductType>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(productTypeReq.getProductTypeName())) {
                predicateList.add(cb.like(root.get("productTypeName"), "%" + productTypeReq.getProductTypeName() + "%"));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            return predicateList.isEmpty() ? null : cb.and(predicateList.toArray(new Predicate[0]));
        };
        Page<ProductType> productTypeByPage = productTypeService.findAll(productTypeSpecification, pageRequest);
        MallPage<ProductTypeResp> productTypeRespPage = MallUtils.toMallPage(productTypeByPage, ProductTypeResp.class);
        log.debug("返回结果：{}", productTypeRespPage);
        return MallResult.build(MallConstant.OK, MallConstant.TEXT_QUERY_OK, productTypeRespPage);
    }
}