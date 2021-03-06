package com.jingliang.mall.controller;

import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiIgnore;
import com.citrsw.annatation.ApiOperation;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.ProductType;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.ProductTypeReq;
import com.jingliang.mall.resp.ProductTypeResp;
import com.jingliang.mall.server.FastdfsService;
import com.jingliang.mall.service.ProductService;
import com.jingliang.mall.service.ProductTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
@Api(description = "商品分类")
public class ProductTypeController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;

    private final ProductTypeService productTypeService;
    private final ProductService productService;
    private final FastdfsService fastdfsService;

    public ProductTypeController(ProductTypeService productTypeService, ProductService productService, FastdfsService fastdfsService) {
        this.productTypeService = productTypeService;
        this.productService = productService;
        this.fastdfsService = fastdfsService;
    }

    /**
     * 添加商品分类
     */
    @ApiOperation(description = "添加商品分类")
    @PostMapping("/save")
    public Result<ProductTypeResp> saveProductTypeResp(@RequestBody ProductTypeReq productTypeReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", productTypeReq);
        if (StringUtils.isBlank(productTypeReq.getProductTypeName())) {
            log.debug("返回结果：{}", Msg.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(productTypeReq, user);
        ProductType productType = BeanMapper.map(productTypeReq, ProductType.class);
        //判断图片是否为空
        if (Objects.isNull(productTypeReq.getProductTypeImgBase64()) && Objects.isNull(productTypeReq.getProductTypeImgUrl())) {
            return Result.build(Msg.FAIL, "图片不能为空");
        }
        if (productTypeReq.getProductTypeImgBase64().isEmpty() && productTypeReq.getProductTypeImgUrl().isEmpty()) {
            return Result.build(Msg.FAIL, "图片不能为空");
        }
        StringBuilder builder = new StringBuilder();
        assert productType != null;
        //获取本地url
        String oldProductTypeImgUrl = null == productType.getProductTypeImgUrlList() ? "" : productType.getProductTypeImgUrlList();
        //获取要保存的url
        List<String> productTypeImgUrls = productTypeReq.getProductTypeImgUrl();
        //如果没有要保存的url
        if (Objects.isNull(productTypeReq.getProductTypeImgUrl()) || productTypeReq.getProductTypeImgUrl().isEmpty()) {
            if (StringUtils.isNotBlank(oldProductTypeImgUrl)) {
                String[] imgUris = oldProductTypeImgUrl.split(";");
                for (String imgUri : imgUris) {
                    if (!fastdfsService.deleteFile(imgUri)) {
                        log.error("图片删除失败：{}", imgUri);
                    }
                }
            }
        }
        for (String newProductTypeImgUrl : productTypeImgUrls) {
            builder.append(";").append(newProductTypeImgUrl);
        }
        //获取要删除的url
        if (builder.length() <= 1) {
            String delProductImgUrls = oldProductTypeImgUrl.replaceAll(builder.substring(0), "");
            if (StringUtils.isNotBlank(delProductImgUrls)) {
                String[] imgUris = delProductImgUrls.split(";");
                for (String imgUri : imgUris) {
                    if (!fastdfsService.deleteFile(imgUri)) {
                        log.error("图片删除失败：{}", imgUri);
                    }
                }
            }
        } else {
            String delProductImgUrls = oldProductTypeImgUrl.replaceAll(builder.substring(1), "");
            if (StringUtils.isNotBlank(delProductImgUrls)) {
                String[] imgUris = delProductImgUrls.split(";");
                for (String imgUri : imgUris) {
                    if (!fastdfsService.deleteFile(imgUri)) {
                        log.error("图片删除失败：{}", imgUri);
                    }
                }
            }
        }
        List<Base64Image> base64Images = new ArrayList<>();
        for (String productTypeImgBase64 : productTypeReq.getProductTypeImgBase64()) {
            Base64Image base64Image = Base64Image.build(productTypeImgBase64);
            if (Objects.isNull(base64Image)) {
                log.debug("返回结果：{}", Msg.TEXT_IMAGE_FAIL);
                return Result.build(Msg.IMAGE_FAIL, Msg.TEXT_IMAGE_FAIL);
            }
            base64Images.add(base64Image);
        }
        for (Base64Image base64Image : base64Images) {
            builder.append(";").append(fastdfsService.uploadFile(base64Image.getBytes(), base64Image.getExtName()));
        }
        productType.setProductTypeImgUrlList(builder.length() > 1 ? builder.substring(1) : "");
        productType = productTypeService.save(productType);
        ProductTypeResp productTypeResp = BeanMapper.map(productType, ProductTypeResp.class);
        log.debug("返回结果：{}", productTypeResp);
        return Result.buildSaveOk(productTypeResp);
    }

    /**
     * 删除商品分类
     */
    @ApiOperation(description = "删除商品分类")
    @PostMapping("/delete")
    public Result<ProductTypeResp> deleteProductTypeResp(@RequestBody ProductTypeReq productTypeReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", productTypeReq);
        if (Objects.isNull(productTypeReq.getId())) {
            log.debug("返回结果：{}", Msg.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        //判断商品分类下是否有已经上架的商品
        //1.有则不允许删除
        //2.无则正常删除
        Integer count = productService.countByProductTypeIdAnnShow(productTypeReq.getId(), true);
        if (count > 0) {
            return Result.build(Msg.FAIL, Msg.TEXT_PRODUCT_DELETE_FAIL);
        }
        Long id = productTypeReq.getId();
        productTypeReq = new ProductTypeReq();
        productTypeReq.setId(id);
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(productTypeReq, user);
        productTypeReq.setIsAvailable(false);
        ProductType productType = BeanMapper.map(productTypeReq, ProductType.class);
        productType = productTypeService.save(productType);
        ProductTypeResp productTypeResp = BeanMapper.map(productType, ProductTypeResp.class);
        log.debug("返回结果：{}", productTypeResp);
        return Result.buildDeleteOk(productTypeResp);
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
            pageRequest = PageRequest.of(productTypeReq.getPage(), productTypeReq.getPageSize(), Sort.by(MallUtils.separateOrder(productTypeReq.getClause())));
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
        MallPage<ProductTypeResp> productTypeRespPage = MallUtils.toMallPage(productTypeByPage, ProductTypeResp.class);
        log.debug("返回结果：{}", productTypeRespPage);
        return Result.build(Msg.OK, Msg.TEXT_QUERY_OK, productTypeRespPage);
    }

    /**
     * 查询所有商品分类
     */
    @ApiOperation(description = "查询所有商品分类")
    @GetMapping("/all")
    public Result<List<ProductTypeResp>> pageAllProductTypeResp() {
        List<ProductType> productTypes = productTypeService.findAll();
        List<ProductTypeResp> productTypeResps = BeanMapper.mapList(productTypes, ProductTypeResp.class);
        log.debug("返回结果：{}", productTypeResps);
        return Result.build(Msg.OK, Msg.TEXT_QUERY_OK, productTypeResps);
    }
}