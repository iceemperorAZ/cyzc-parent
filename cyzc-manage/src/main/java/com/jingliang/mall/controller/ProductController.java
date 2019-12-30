package com.jingliang.mall.controller;

import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.Product;
import com.jingliang.mall.entity.Sku;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.ProductReq;
import com.jingliang.mall.resp.ProductResp;
import com.jingliang.mall.server.FastdfsService;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.ProductService;
import com.jingliang.mall.service.SkuService;
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
import java.util.Date;
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
@RestController("backProductController")
@Slf4j
@RequestMapping("/back/product")
public class ProductController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    @Value("${product.sku.init.invented.num}")
    private Integer productSkuInitInventedNum;

    private final ProductService productService;
    private final FastdfsService fastdfsService;
    private final RedisService redisService;
    private final SkuService skuService;

    public ProductController(ProductService productService, FastdfsService fastdfsService, RedisService redisService, SkuService skuService) {
        this.productService = productService;
        this.fastdfsService = fastdfsService;
        this.redisService = redisService;
        this.skuService = skuService;
    }

    /**
     * 保存商品
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存商品")
    public MallResult<ProductResp> save(@RequestBody ProductReq productReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", productReq);
        if (Objects.isNull(productReq.getProductTypeId()) || StringUtils.isBlank(productReq.getProductTypeName())
                || productReq.getProductImgs().isEmpty() || StringUtils.isBlank(productReq.getProductName())
                || Objects.isNull(productReq.getSellingPrice()) || StringUtils.isBlank(productReq.getSpecs())
                || StringUtils.isBlank(productReq.getUnit()) || Objects.isNull(productReq.getIsHot()) || Objects.isNull(productReq.getIsNew())) {
            log.debug("返回结果：{}", MallConstant.TEXT_PARAM_FAIL);
            return MallResult.buildParamFail();
        }
        //判断商品是否重复
        if (Objects.isNull(productReq.getId()) && Objects.nonNull(productService.findAllByProductName(productReq.getProductName()))) {
            log.debug("返回结果：{}", MallConstant.TEXT_PRODUCT_EXIST_FAIL);
            return MallResult.build(MallConstant.SAVE_FAIL, MallConstant.TEXT_PRODUCT_EXIST_FAIL);
        }
        if (Objects.nonNull(productReq.getId())) {
            //商品名称不能被修改
            productReq.setProductName(null);
            //将之前所有的图片删除重新上传一份新的
            Product product = productService.findAllById(productReq.getId());
            if (product.getIsShow()) {
                //上架中的商品不能修改
                return MallResult.build(MallConstant.PRODUCT_FAIL, MallConstant.TEXT_PRODUCT_UPDATE_FAIL);
            }
            String productImgUris = product.getProductImgUris();
            if (StringUtils.isNotBlank(productImgUris)) {
                String[] imgUris = productImgUris.split(";");
                for (String imgUri : imgUris) {
                    if (!fastdfsService.deleteFile(imgUri)) {
                        log.error("图片删除失败：{}", imgUri);
                    }
                }
            }
        }
        List<Base64Image> base64Images = new ArrayList<>();
        for (String productImg : productReq.getProductImgs()) {
            Base64Image base64Image = Base64Image.build(productImg);
            if (Objects.isNull(base64Image)) {
                log.debug("返回结果：{}", MallConstant.TEXT_IMAGE_FAIL);
                return MallResult.build(MallConstant.IMAGE_FAIL, MallConstant.TEXT_IMAGE_FAIL);
            }
            base64Images.add(base64Image);
        }
        StringBuilder builder = new StringBuilder();
        for (Base64Image base64Image : base64Images) {
            builder.append(";").append(fastdfsService.uploadFile(base64Image.getBytes(), base64Image.getExtName()));
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(productReq, user);
        productReq.setProductImgUris(builder.substring(1));
        productReq.setProductNo(redisService.getProductNo());
        productReq.setIsShow(false);
        productReq.setSalesVolume(0);
        productReq.setProductZoneId(-1L);
        productReq.setExamineStatus(ExamineStatus.NOT_SUBMITTED.getValue());
        Product product = MallBeanMapper.map(productReq, Product.class);
        ProductResp productResp = new ProductResp();
        MallBeanMapper.map(productService.save(product), productResp);
        log.debug("返回结果：{}", productResp);
        return MallResult.buildSaveOk(productResp);
    }

    /**
     * 分页查询全部商品
     */
    @GetMapping("/page/all")
    @ApiOperation(value = "分页查询全部商品")
    public MallResult<MallPage<ProductResp>> pageAllProduct(ProductReq productReq) {
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
            if (Objects.nonNull(productReq.getIsShow())) {
                predicateList.add(cb.equal(root.get("isShow"), productReq.getIsShow()));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("createTime")));
            return query.getRestriction();
        };
        Page<Product> productPage = productService.findAll(productSpecification, pageRequest);
        MallPage<ProductResp> productRespPage = MallUtils.toMallPage(productPage, ProductResp.class);
        log.debug("返回结果：{}", productRespPage);
        return MallResult.buildQueryOk(productRespPage);
    }

    /**
     * 根据Id查询商品信息
     */
    @ApiOperation(value = "根据Id查询商品信息")
    @GetMapping("/id")
    public MallResult<ProductResp> findById(Long id) {
        log.debug("请求参数：{}", id);
        ProductResp productResp = MallBeanMapper.map(productService.findAllById(id), ProductResp.class);
        log.debug("返回结果：{}", productResp);
        return MallResult.buildSaveOk(productResp);
    }

    /**
     * 批量上架商品
     */
    @ApiOperation(value = "批量上架商品")
    @PostMapping("/batch/show")
    public MallResult<List<ProductResp>> batchShow(@RequestBody ProductReq productReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", productReq.getProductIds());
        List<Product> products = new ArrayList<>();
        User user = (User) session.getAttribute(sessionUser);
        Date date = new Date();
        for (Long id : productReq.getProductIds()) {
            Sku sku = skuService.findByProductId(id);
            //查询线上库存数
            if (Objects.isNull(sku) || sku.getSkuLineNum() < 1) {
                log.debug("返回结果：{}", MallConstant.TEXT_PRODUCT_SHOW_SKU_FAIL);
                return MallResult.build(MallConstant.PRODUCT_FAIL, MallConstant.TEXT_PRODUCT_SHOW_SKU_FAIL);
            }
            Product product = new Product();
            product.setId(id);
            product.setIsShow(true);
            product.setShowTime(date);
            product.setUpdateTime(date);
            product.setShowUserId(user.getId());
            product.setShowUserName(user.getUserName());
            products.add(product);
        }
        List<Product> productList = productService.batchShow(products);
        if (productList.isEmpty()) {
            log.debug("返回结果：{}", MallConstant.TEXT_PRODUCT_SHOW_FAIL);
            return MallResult.build(MallConstant.PRODUCT_FAIL, MallConstant.TEXT_PRODUCT_SHOW_FAIL);
        }
        List<ProductResp> productRespList = MallBeanMapper.mapList(productList, ProductResp.class);
        log.debug("返回结果：{}", productRespList);
        return MallResult.build(MallConstant.OK, MallConstant.TEXT_SHOW_UP_OK, productRespList);
    }

    /**
     * 批量下架商品
     */
    @ApiOperation(value = "批量下架商品")
    @PostMapping("/batch/hide")
    public MallResult<List<ProductResp>> batchHide(@RequestBody ProductReq productReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", productReq.getProductIds());
        List<Product> products = new ArrayList<>();
        User user = (User) session.getAttribute(sessionUser);
        Date date = new Date();
        productReq.getProductIds().forEach(id -> {
            Product product = new Product();
            product.setId(id);
            product.setIsShow(false);
            product.setUpdateTime(date);
            product.setShowUserId(user.getId());
            product.setShowUserName(user.getUserName());
            products.add(product);
        });
        List<Product> productList = productService.batchHide(products);
        List<ProductResp> productRespList = MallBeanMapper.mapList(productList, ProductResp.class);
        log.debug("返回结果：{}", productRespList);
        return MallResult.build(MallConstant.OK, MallConstant.TEXT_SHOW_DOWN_OK, productRespList);
    }

    /**
     * 批量删除商品
     */
    @ApiOperation(value = "批量删除商品")
    @PostMapping("/batch/delete")
    public MallResult<List<ProductResp>> batchDelete(@RequestBody ProductReq productReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", productReq.getProductIds());
        List<Product> products = new ArrayList<>();
        User user = (User) session.getAttribute(sessionUser);
        Date date = new Date();
        for (Long id : productReq.getProductIds()) {
            if(redisService.getProductSkuNum(id+"")<productSkuInitInventedNum){
                log.debug("返回结果：{}", MallConstant.TEXT_PRODUCT_ORDER_FAIL);
                return MallResult.build(MallConstant.PRODUCT_FAIL, MallConstant.TEXT_PRODUCT_ORDER_FAIL);
            }
            if (Objects.nonNull(productService.findShowProductById(id))) {
                log.debug("返回结果：{}", MallConstant.TEXT_PRODUCT_DELETE_FAIL);
                return MallResult.build(MallConstant.PRODUCT_FAIL, MallConstant.TEXT_PRODUCT_DELETE_FAIL);
            }
            Product product = new Product();
            product.setId(id);
            product.setIsAvailable(false);
            product.setShowTime(date);
            product.setShowUserId(user.getId());
            product.setShowUserName(user.getUserName());
            products.add(product);
        }
        List<Product> productList = productService.batchDelete(products);
        List<ProductResp> productRespList = MallBeanMapper.mapList(productList, ProductResp.class);
        log.debug("返回结果：{}", productRespList);
        return MallResult.buildDeleteOk(productRespList);
    }
}