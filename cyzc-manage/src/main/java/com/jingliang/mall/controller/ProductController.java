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
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public Result<ProductResp> save(@RequestBody ProductReq productReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", productReq);
        if (Objects.isNull(productReq.getProductTypeId()) || StringUtils.isBlank(productReq.getProductTypeName())
                || StringUtils.isBlank(productReq.getProductName())
                || Objects.isNull(productReq.getSellingPrice()) || StringUtils.isBlank(productReq.getSpecs()) || productReq.getProductSort() == null
                || StringUtils.isBlank(productReq.getUnit()) || Objects.isNull(productReq.getIsHot()) || Objects.isNull(productReq.getIsNew())
                || Objects.isNull(productReq.getProductDetails())) {
            log.debug("返回结果：{}", Msg.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        //判断图片是否为空
        if ((Objects.isNull(productReq.getProductImgs()) || productReq.getProductImgs().isEmpty()) &&
                ((Objects.isNull(productReq.getProductImgUris())) || productReq.getProductImgUris().isEmpty())) {
            return Result.build(Msg.FAIL, "图片不能为空");
        }
        if ((Objects.isNull(productReq.getProductDetailsImgBase64s()) || productReq.getProductDetailsImgBase64s().isEmpty()) &&
                ((Objects.isNull(productReq.getProductDetailsImgUrls())) || productReq.getProductDetailsImgUrls().isEmpty())) {
            return Result.build(Msg.FAIL, "图片详情不能为空");
        }
        //判断商品是否重复
        Product oldProduct = productService.findByProductNameAndSpecs(productReq.getProductName(), productReq.getSpecs());
        if (!Objects.isNull(oldProduct) && Objects.isNull(oldProduct.getId())) {
            return Result.build(Msg.FAIL, "该商品已存在，不能重复添加");
        }
//        if (Objects.isNull(productReq.getId()) && Objects.nonNull(productService.findAllByProductName(productReq.getProductName()))) {
//            log.debug("返回结果：{}", MallConstant.TEXT_PRODUCT_EXIST_FAIL);
//            return MallResult.build(MallConstant.SAVE_FAIL, MallConstant.TEXT_PRODUCT_EXIST_FAIL);
//        }
        StringBuilder builder = new StringBuilder();
        StringBuilder detailsBuilder = new StringBuilder();
        if (Objects.nonNull(productReq.getId())) {
            //商品名称不能被修改
//            productReq.setProductName(null);
            //将之前所有的图片删除重新上传一份新的
            Product product = productService.findAllById(productReq.getId());
            if (product.getIsShow()) {
                //上架中的商品不能修改
                return Result.build(Msg.PRODUCT_FAIL, Msg.TEXT_PRODUCT_UPDATE_FAIL);
            }
            //获取本地url
            String oldProductImgUris = product.getProductImgUris();
            //获取要保存的url
            List<String> productImgUris = productReq.getProductImgUris();
            //如果没有要保存的url
            if (productImgUris.isEmpty() || Objects.isNull(productImgUris)) {
                if (StringUtils.isNotBlank(oldProductImgUris)) {
                    String[] imgUris = oldProductImgUris.split(";");
                    for (String imgUri : imgUris) {
                        if (!fastdfsService.deleteFile(imgUri)) {
                            log.error("图片删除失败：{}", imgUri);
                        }
                    }
                }
            }
            for (String newProductImgUris : productImgUris) {
                builder.append(";").append(newProductImgUris);
            }
            //获取要删除的url
            if (builder.length() <= 1) {
                String delProductImgUrls = oldProductImgUris.replaceAll(builder.substring(0), "");
                if (StringUtils.isNotBlank(delProductImgUrls)) {
                    String[] imgUris = delProductImgUrls.split(";");
                    for (String imgUri : imgUris) {
                        if (!fastdfsService.deleteFile(imgUri)) {
                            log.error("图片删除失败：{}", imgUri);
                        }
                    }
                }
            } else {
                String delProductImgUrls = oldProductImgUris.replaceAll(builder.substring(1), "");
                if (StringUtils.isNotBlank(delProductImgUrls)) {
                    String[] imgUris = delProductImgUrls.split(";");
                    for (String imgUri : imgUris) {
                        if (!fastdfsService.deleteFile(imgUri)) {
                            log.error("图片删除失败：{}", imgUri);
                        }
                    }
                }
            }
            //获取本地商品详情url
            String oldProductDetailsImgUrls = product.getProductDetailsImgUrls();
            //获取要保存的url
            List<String> productDetailsImgUrls = productReq.getProductDetailsImgUrls();
            //如果没有要保存的url
            if (productDetailsImgUrls.isEmpty()) {
                if (StringUtils.isNotBlank(oldProductDetailsImgUrls)) {
                    String[] imgUris = oldProductDetailsImgUrls.split(";");
                    for (String imgUri : imgUris) {
                        if (!fastdfsService.deleteFile(imgUri)) {
                            log.error("图片删除失败：{}", imgUri);
                        }
                    }
                }
            }
            for (String newProductDetailsImgUrls : productDetailsImgUrls) {
                detailsBuilder.append(";").append(newProductDetailsImgUrls);
            }
            //获取要删除的url
            if (detailsBuilder.length() <= 1) {
                String delProductDetailsImgUrls = oldProductDetailsImgUrls.replaceAll(detailsBuilder.substring(0), "");
                if (StringUtils.isNotBlank(delProductDetailsImgUrls)) {
                    String[] imgUris = delProductDetailsImgUrls.split(";");
                    for (String imgUri : imgUris) {
                        if (!fastdfsService.deleteFile(imgUri)) {
                            log.error("图片删除失败：{}", imgUri);
                        }
                    }
                }
            } else {
                String delProductDetailsImgUrls = oldProductDetailsImgUrls.replaceAll(detailsBuilder.substring(1), "");
                if (StringUtils.isNotBlank(delProductDetailsImgUrls)) {
                    String[] imgUris = delProductDetailsImgUrls.split(";");
                    for (String imgUri : imgUris) {
                        if (!fastdfsService.deleteFile(imgUri)) {
                            log.error("图片删除失败：{}", imgUri);
                        }
                    }
                }
            }
        }
        Product product = productService.findAllById(productReq.getId());
        if (product != null && !product.getId().equals(productReq.getId())) {
            //序号重复
            return Result.build(Msg.FAIL, "序号重复");
        }
        List<Base64Image> base64Images = new ArrayList<>();
        for (String productImg : productReq.getProductImgs()) {
            Base64Image base64Image = Base64Image.build(productImg);
            if (Objects.isNull(base64Image)) {
                log.debug("返回结果：{}", Msg.TEXT_IMAGE_FAIL);
                return Result.build(Msg.IMAGE_FAIL, Msg.TEXT_IMAGE_FAIL);
            }
            base64Images.add(base64Image);
        }
        for (Base64Image base64Image : base64Images) {
            builder.append(";").append(fastdfsService.uploadFile(base64Image.getBytes(), base64Image.getExtName()));
        }
        //详情页图片处理
        List<Base64Image> detailsBase64Images = new ArrayList<>();
        for (String productDetailsImgUrl : productReq.getProductDetailsImgBase64s()) {
            Base64Image base64Image = Base64Image.build(productDetailsImgUrl);
            if (Objects.isNull(base64Image)) {
                log.debug("返回结果：{}", Msg.TEXT_IMAGE_FAIL);
                return Result.build(Msg.IMAGE_FAIL, Msg.TEXT_IMAGE_FAIL);
            }
            detailsBase64Images.add(base64Image);
        }
        for (Base64Image base64Image : detailsBase64Images) {
            detailsBuilder.append(";").append(fastdfsService.uploadFile(base64Image.getBytes(), base64Image.getExtName()));
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(productReq, user);
        if (StringUtils.isBlank(productReq.getProductNo())) {
            productReq.setProductNo(redisService.getProductNo());
        }
        productReq.setIsShow(false);
        productReq.setIsSoonShow(productReq.getIsSoonShow() == null ? false : productReq.getIsSoonShow());
        if (productReq.getSalesVolume() != null) {
            productReq.setSalesVolume(0);
        }
        productReq.setProductZoneId(-1L);
        productReq.setExamineStatus(ExamineStatus.NOT_SUBMITTED.getValue());
        product = BeanMapper.map(productReq, Product.class);
        assert product != null;
        product.setProductImgUris(builder.length() > 1 ? builder.substring(1) : "");
        product.setProductDetailsImgUrls(detailsBuilder.length() > 1 ? detailsBuilder.substring(1) : "");
        product.setMinNum(Objects.isNull(productReq.getMinNum()) || productReq.getMinNum() < 0 ? 0 : productReq.getMinNum());
        ProductResp productResp = new ProductResp();
        BeanMapper.map(productService.save(product), productResp);
        log.debug("返回结果：{}", productResp);
        return Result.buildSaveOk(productResp);
    }

    /**
     * 分页查询全部商品
     */
    @GetMapping("/page/all")
    @ApiOperation(value = "分页查询全部商品")
    public Result<MallPage<ProductResp>> pageAllProduct(ProductReq productReq) throws UnsupportedEncodingException {
        log.debug("请求参数：{}", productReq);
        PageRequest pageRequest = PageRequest.of(productReq.getPage(), productReq.getPageSize());
        if (StringUtils.isNotBlank(productReq.getClause())) {
            pageRequest = PageRequest.of(productReq.getPage(), productReq.getPageSize(), Sort.by(MallUtils.separateOrder(productReq.getClause())));
        }
        if (StringUtils.isNotBlank(productReq.getProductName())) {
            productReq.setProductName(URLDecoder.decode(productReq.getProductName(), "UTF-8"));
        }
        Specification<Product> productSpecification = (Specification<Product>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (Objects.nonNull(productReq.getProductTypeId())) {
                predicateList.add(cb.equal(root.get("productTypeId"), productReq.getProductTypeId()));
            }
            if (StringUtils.isNotBlank(productReq.getProductName())) {
                predicateList.add(cb.or(cb.like(root.get("productName"), "%" + productReq.getProductName() + "%"), cb.like(root.get("productTypeName"), "%" + productReq.getProductName() + "%"),cb.equal(root.get("id"),Long.parseLong(productReq.getProductName()))));
            }
            if (Objects.nonNull(productReq.getProductZoneId())) {
                predicateList.add(cb.equal(root.get("productZoneId"), productReq.getProductZoneId()));
            }
            if (Objects.nonNull(productReq.getIsShow())) {
                predicateList.add(cb.equal(root.get("isShow"), productReq.getIsShow()));
            }
            if (Objects.nonNull(productReq.getIsSoonShow())) {
                predicateList.add(cb.equal(root.get("isSoonShow"), productReq.getIsSoonShow()));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("createTime")));
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

    /**
     * 批量上架商品
     */
    @ApiOperation(value = "批量上架商品")
    @PostMapping("/batch/show")
    public Result<List<ProductResp>> batchShow(@RequestBody ProductReq productReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", productReq.getProductIds());
        List<Product> products = new ArrayList<>();
        User user = (User) session.getAttribute(sessionUser);
        Date date = new Date();
        for (Long id : productReq.getProductIds()) {
            Sku sku = skuService.findByProductId(id);
            //查询线上库存数
            if (Objects.isNull(sku) || sku.getSkuLineNum() < 1) {
                log.debug("返回结果：{}", Msg.TEXT_PRODUCT_SHOW_SKU_FAIL);
                return Result.build(Msg.PRODUCT_FAIL, Msg.TEXT_PRODUCT_SHOW_SKU_FAIL);
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
            log.debug("返回结果：{}", Msg.TEXT_PRODUCT_SHOW_FAIL);
            return Result.build(Msg.PRODUCT_FAIL, Msg.TEXT_PRODUCT_SHOW_FAIL);
        }
        List<ProductResp> productRespList = BeanMapper.mapList(productList, ProductResp.class);
        log.debug("返回结果：{}", productRespList);
        return Result.build(Msg.OK, Msg.TEXT_SHOW_UP_OK, productRespList);
    }

    /**
     * 批量下架商品
     */
    @ApiOperation(value = "批量下架商品")
    @PostMapping("/batch/hide")
    public Result<List<ProductResp>> batchHide(@RequestBody ProductReq productReq, @ApiIgnore HttpSession session) {
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
        List<ProductResp> productRespList = BeanMapper.mapList(productList, ProductResp.class);
        log.debug("返回结果：{}", productRespList);
        return Result.build(Msg.OK, Msg.TEXT_SHOW_DOWN_OK, productRespList);
    }

    /**
     * 批量删除商品
     */
    @ApiOperation(value = "批量删除商品")
    @PostMapping("/batch/delete")
    public Result<List<ProductResp>> batchDelete(@RequestBody ProductReq productReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", productReq.getProductIds());
        List<Product> products = new ArrayList<>();
        User user = (User) session.getAttribute(sessionUser);
        Date date = new Date();
        for (Long id : productReq.getProductIds()) {
            //判断线上库存是否小于初始库存，即是否用用户下单
            if (redisService.getProductSkuNum(id + "") < productSkuInitInventedNum) {
                log.debug("返回结果：{}", Msg.TEXT_PRODUCT_ORDER_FAIL);
                return Result.build(Msg.PRODUCT_FAIL, Msg.TEXT_PRODUCT_ORDER_FAIL);
            }
            //判断库存是否为0,有库存不允许删除
            Sku sku = skuService.findByProductId(id);
            if (sku.getSkuRealityNum() > 0) {
                log.debug("返回结果：{}", Msg.TEXT_PRODUCT_SKU_FAIL);
                return Result.build(Msg.PRODUCT_FAIL, Msg.TEXT_PRODUCT_SKU_FAIL);
            }
            if (Objects.nonNull(productService.findShowProductById(id))) {
                log.debug("返回结果：{}", Msg.TEXT_PRODUCT_DELETE_FAIL);
                return Result.build(Msg.PRODUCT_FAIL, Msg.TEXT_PRODUCT_DELETE_FAIL);
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
        List<ProductResp> productRespList = BeanMapper.mapList(productList, ProductResp.class);
        log.debug("返回结果：{}", productRespList);
        return Result.buildDeleteOk(productRespList);
    }

    /**
     * 导出商品表
     */
    @GetMapping("/download/excel")
    @ApiOperation(value = "导出商品表")
    public ResponseEntity<byte[]> getAllBuyerAndSaleByTimeToExcel() throws IOException {
        List<Map<String, Object>> product = productService.getAllProduct();
        // 定义一个新的工作簿
        XSSFWorkbook productWorkbook = new XSSFWorkbook();
        // 创建一个Sheet页
        XSSFSheet sheet = productWorkbook.createSheet("First sheet");
        //设置行高
        sheet.setDefaultRowHeight((short) (2 * 256));
        //设置列宽
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        XSSFFont font = productWorkbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        //获得表格第一行
        XSSFRow row = sheet.createRow(0);
        //根据需要给第一行每一列设置标题
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("商品编号");
        cell = row.createCell(1);
        cell.setCellValue("商户名称");
        cell = row.createCell(2);
        cell.setCellValue("商户规格");
        cell = row.createCell(3);
        XSSFRow rows;
        XSSFCell cells;
        //循环拿到的数据给所有行每一列设置对应的值
        for (int i = 0; i < product.size(); i++) {

            // 在这个sheet页里创建一行
            rows = sheet.createRow(i + 1);
            // 该行创建一个单元格,在该单元格里设置值
            String no = product.get(i).get("商品编号").toString();
            String name = product.get(i).get("商品名称").toString();
            String specs = product.get(i).get("商品规格").toString();
            cells = rows.createCell(0);
            cells.setCellValue(no);
            cells = rows.createCell(1);
            cells.setCellValue(name);
            cells = rows.createCell(2);
            cells.setCellValue(specs);
        }
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        productWorkbook.write(arrayOutputStream);
        String newName = URLEncoder.encode("商品明细表-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ".xlsx", "utf-8")
                .replaceAll("\\+", "%20").replaceAll("%28", "\\(")
                .replaceAll("%29", "\\)").replaceAll("%3B", ";")
                .replaceAll("%40", "@").replaceAll("%23", "\\#")
                .replaceAll("%26", "\\&").replaceAll("%2C", "\\,");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", newName));
        headers.add("Expires", "0");
        headers.add("Pragma", "no-cache");
        return ResponseEntity.ok().headers(headers)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(arrayOutputStream.size())
                .body(arrayOutputStream.toByteArray());
    }
}