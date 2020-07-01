package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Sku;
import com.jingliang.mall.req.SkuReq;
import com.jingliang.mall.resp.SkuResp;
import com.jingliang.mall.service.ProductService;
import com.jingliang.mall.service.SkuService;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 商品库存表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
@RequestMapping("/back/sku")
@Api(description = "商品库存")
@RestController
@Slf4j
public class SkuController {

    private final SkuService skuService;
    private final ProductService productService;

    public SkuController(SkuService skuService, ProductService productService) {
        this.skuService = skuService;
        this.productService = productService;
    }

    /**
     * 分页查询所有库存
     */
    @ApiOperation(description = "分页查询所有库存")
    @GetMapping("/page/all")
    public Result<MallPage<SkuResp>> pageAll(SkuReq skuReq) throws UnsupportedEncodingException {
        log.debug("请求参数:{}", skuReq);
        PageRequest pageRequest = PageRequest.of(skuReq.getPage(), skuReq.getPageSize());
        if (StringUtils.isNotBlank(skuReq.getClause())) {
            pageRequest = PageRequest.of(skuReq.getPage(), skuReq.getPageSize(), Sort.by(MallUtils.separateOrder(skuReq.getClause())));
        }
        if (StringUtils.isNotBlank(skuReq.getProductName())) {
            skuReq.setProductName(URLDecoder.decode(skuReq.getProductName(), "UTF-8"));
        }
        Specification<Sku> skuSpecification = (Specification<Sku>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (Objects.nonNull(skuReq.getProductTypeId())) {
                predicateList.add(cb.equal(root.get("productTypeId"), skuReq.getProductTypeId()));
            }
            if (StringUtils.isNotBlank(skuReq.getProductName())) {
                try {
                    predicateList.add(cb.or(cb.like(root.get("productName"), "%" + skuReq.getProductName() + "%"), cb.equal(root.get("id"), Long.parseLong(skuReq.getProductName()))));
                } catch (Exception e) {
                    predicateList.add(cb.or(cb.like(root.get("productName"), "%" + skuReq.getProductName() + "%")));
                }
            }
            if (Objects.nonNull(skuReq.getProductId())) {
                predicateList.add(cb.equal(root.get("productId"), skuReq.getProductId()));
            }
            if (StringUtils.isNotBlank(skuReq.getSkuNo())) {
                predicateList.add(cb.like(root.get("skuNo"), "%" + skuReq.getSkuNo() + "%"));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.asc(root.get("skuLineNum")), cb.asc(root.get("updateTime")));
            return query.getRestriction();
        };
        Page<Sku> skuPage = skuService.findAll(skuSpecification, pageRequest);
        MallPage<SkuResp> skuRespMallPage = MallUtils.toMallPage(skuPage, SkuResp.class);
        log.debug("返回结果：{}", skuRespMallPage);
        return Result.buildQueryOk(skuRespMallPage);
    }
}