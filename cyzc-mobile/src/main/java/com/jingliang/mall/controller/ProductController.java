package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.Product;
import com.jingliang.mall.req.ProductReq;
import com.jingliang.mall.resp.ProductResp;
import com.jingliang.mall.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.util.Objects;

/**
 * 商品表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
@Api(tags = "商品")
@RestController
@Slf4j
@RequestMapping("/front/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 分页查询全部商品
     */
    @GetMapping("/page/all")
    @ApiOperation(value = "分页查询全部商品")
    public Result<MallPage<ProductResp>> pageAllProduct(ProductReq productReq) {
        log.debug("请求参数：{}", productReq);
        PageRequest pageRequest = PageRequest.of(productReq.getPage(), productReq.getPageSize());
        if (StringUtils.isNotBlank(productReq.getClause())) {
            pageRequest = PageRequest.of(productReq.getPage(), productReq.getPageSize(), Sort.by(MallUtils.separateOrder(productReq.getClause())));
        }
        Specification<Product> productSpecification = (Specification<Product>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (Objects.nonNull(productReq.getProductTypeId())) {
                predicateList.add(cb.equal(root.get("productTypeId"), productReq.getProductTypeId()));
            }
            if (StringUtils.isNotBlank(productReq.getProductName())) {
                predicateList.add(cb.like(root.get("productName"), "%" + productReq.getProductName() + "%"));
            }
            if (Objects.nonNull(productReq.getProductZoneId())) {
                predicateList.add(cb.equal(root.get("productZoneId"), productReq.getProductZoneId()));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            predicateList.add(cb.equal(root.get("isShow"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("isHot")),cb.asc(root.get("productName")), cb.asc(root.get("weight")));
            return query.getRestriction();
        };
        Page<Product> productPage = productService.findAll(productSpecification, pageRequest);
        MallPage<ProductResp> productRespPage = MallUtils.toMallPage(productPage, ProductResp.class);
        log.debug("返回结果：{}", productRespPage);
        return Result.buildQueryOk(productRespPage);
    }

    /**
     * 根据Id查询商品信息
     */
    @ApiOperation(value = "根据Id查询商品信息")
    @GetMapping("/id")
    public Result<ProductResp> findById(Long id) {
        log.debug("请求参数：{}", id);
        ProductResp productResp = BeanMapper.map(productService.findAllById(id), ProductResp.class);
        log.debug("返回结果：{}", productResp);
        return Result.buildSaveOk(productResp);
    }
}