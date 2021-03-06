package com.jingliang.mall.controller;

import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.ProductType;
import com.jingliang.mall.req.ProductTypeReq;
import com.jingliang.mall.resp.ProductTypeResp;
import com.jingliang.mall.service.ProductTypeService;
import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
@RestController
@Slf4j
@RequestMapping("/front/productType")
@Api(description = "商品分类")
public class ProductTypeController {

    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    /**
     * 分页查询所有商品分类
     */
    @ApiOperation(description = "分页查询所有商品分类")
    @GetMapping("/page/all")
    public Result<MallPage<ProductTypeResp>> pageAllProductTypeResp(ProductTypeReq productTypeReq) {
        log.debug("请求参数：{}", productTypeReq);
        PageRequest pageRequest = PageRequest.of(productTypeReq.getPage(), productTypeReq.getPageSize());
        if (StringUtils.isNotBlank(productTypeReq.getClause())) {
            pageRequest = PageRequest.of(productTypeReq.getPage(), productTypeReq.getPageSize(), Sort.by(MUtils.separateOrder(productTypeReq.getClause())));
        }
        Specification<ProductType> productTypeSpecification = (Specification<ProductType>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(productTypeReq.getProductTypeName())) {
                predicateList.add(cb.like(root.get("productTypeName"), "%" + productTypeReq.getProductTypeName() + "%"));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.asc(root.get("productTypeOrder")));
            return query.getRestriction();
        };
        Page<ProductType> productTypeByPage = productTypeService.findAll(productTypeSpecification, pageRequest);
        MallPage<ProductTypeResp> productTypeRespPage = MUtils.toMallPage(productTypeByPage, ProductTypeResp.class);
        log.debug("返回结果：{}", productTypeRespPage);
        return Result.build(Msg.OK, Msg.TEXT_QUERY_OK, productTypeRespPage);
    }

    /**
     * 分页查询所有商品分类
     */
    @ApiOperation(description = "分页查询所有商品分类")
    @GetMapping("/list/all")
    public Result<List<ProductTypeResp>> listAllProductTypeResp() {
        List<ProductTypeResp> productTypeResps = new ArrayList<>();
        List<ProductType> productTypes = productTypeService.findAll();
        for (ProductType productType : productTypes) {
            productTypeResps.add(BeanMapper.map(productType,ProductTypeResp.class));
        }
        log.debug("返回结果：{}", productTypeResps);
        return Result.build(Msg.OK, Msg.TEXT_QUERY_OK, productTypeResps);
    }

    /**
     * 查询分类集合的第一个
     *
     * @return
     */
    //TODO 这个接口手机宝哥提出来，暂时顶替秒杀页面的策略
    @GetMapping("/findFirst")
    @ApiOperation(description = "查询分类集合的第一个")
    public Result<ProductTypeResp> findFirst() {
        ProductType productType = productTypeService.findFirst();
        ProductTypeResp productTypeResp = BeanMapper.map(productType, ProductTypeResp.class);
        log.debug("返回结果：{}", productTypeResp);
        return Result.buildQueryOk(productTypeResp);
    }
}