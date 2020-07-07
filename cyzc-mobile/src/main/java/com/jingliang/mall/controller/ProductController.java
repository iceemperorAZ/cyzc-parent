package com.jingliang.mall.controller;

import com.citrsw.annatation.ApiIgnore;
import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.MUtils;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.Product;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.ProductReq;
import com.jingliang.mall.resp.ProductResp;
import com.jingliang.mall.service.ProductService;
import com.jingliang.mall.utils.PageMapperUtils;
import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
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
@Api(description = "商品")
@RestController
@Slf4j
@RequestMapping("/front/product")
public class ProductController {

    /**
     * session用户Key
     */
    @Value("${session.buyer.key}")
    private String sessionBuyer;
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 分页查询全部商品
     */
    @GetMapping("/page/all")
    @ApiOperation(description = "分页查询全部商品")
    public Result<MallPage<ProductResp>> pageAllProduct(@RequestBody ProductReq productReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", productReq);
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        PageRequest pageRequest = PageRequest.of(productReq.getPage(), productReq.getPageSize());
        if (StringUtils.isNotBlank(productReq.getClause())) {
            pageRequest = PageRequest.of(productReq.getPage(), productReq.getPageSize(), Sort.by(MUtils.separateOrder(productReq.getClause())));
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
            //此处增加判断用户所在区是否能够显示该商品
            if (Objects.nonNull(productReq.getProductArea())) {
                predicateList.add(cb.equal(root.get("productArea"), productReq.getProductArea()));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            predicateList.add(cb.equal(root.get("isShow"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("isHot")), cb.asc(root.get("productSort")));
            return query.getRestriction();
        };
        Page<Product> productPage = productService.findAll(productSpecification, pageRequest);
        List<Product> productList = productPage.getContent();
        List<Product> products = new ArrayList<>();
        //商户默认地址所在区
        String buyerArea = productService.getBuyerProduct(buyer.getId());
        //添加向用户展示的商品到list
        for (Product product : productList) {
            //设立标志，判断是否是第一次添加
            boolean flg = false;
            //非空判断
            product.setProductArea(product.getProductArea() == null ? "" : product.getProductArea());
            //商品不展示的几个分区
            String[] areaCode = product.getProductArea().split("/");
            for (String area : areaCode) {
                if (StringUtils.equals(area, buyerArea)) {
                    flg = true;
                }
            }
            if (!flg) {
                products.add(product);
            }
        }
        Page<Product> pageProduct = PageMapperUtils.listToPage(products, pageRequest);
        MallPage<ProductResp> productRespPage = MUtils.toMallPage(pageProduct, ProductResp.class);
        log.debug("返回结果：{}", productRespPage);
        return Result.buildQueryOk(productRespPage);
    }

    /**
     * 根据Id查询商品信息
     */
    @ApiOperation(description = "根据Id查询商品信息")
    @GetMapping("/id")
    public Result<ProductResp> findById(Long id) {
        log.debug("请求参数：{}", id);
        ProductResp productResp = BeanMapper.map(productService.findAllById(id), ProductResp.class);
        log.debug("返回结果：{}", productResp);
        return Result.buildSaveOk(productResp);
    }
}