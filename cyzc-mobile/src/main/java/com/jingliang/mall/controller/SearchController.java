//package com.jingliang.mall.controller;
//
//import com.jingliang.mall.common.MallPage;
//import com.jingliang.mall.common.MallResult;
//import com.jingliang.mall.esdocument.EsKeyword;
//import com.jingliang.mall.esdocument.EsProduct;
//import com.jingliang.mall.common.MallUtils;
//import com.jingliang.mall.req.KeywordReq;
//import com.jingliang.mall.req.ProductReq;
//import com.jingliang.mall.resp.KeywordResp;
//import com.jingliang.mall.resp.ProductResp;
//import com.jingliang.mall.server.EsKeywordService;
//import com.jingliang.mall.server.EsProductService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 搜索Controller
// *
// * @author Zhenfeng Li
// * @version 1.0
// * @date 2019-09-27 22:38
// */
//@Api(tags = "搜索")
//@RequestMapping("/front/search")
//@RestController
//@Slf4j
//public class SearchController {
//    /**
//     * session用户Key
//     */
//    @Value("${session.buyer.key}")
//    private String sessionBuyer;
//    private final EsProductService esProductService;
//    private final EsKeywordService esKeywordService;
//
//    public SearchController(EsProductService esProductService, EsKeywordService esKeywordService) {
//        this.esProductService = esProductService;
//        this.esKeywordService = esKeywordService;
//    }
//
//    /**
//     * 分页搜索全部商品
//     */
//    @ApiOperation(value = "分页搜索全部商品")
//    @GetMapping("/product")
//    public MallResult<MallPage<ProductResp>> searchProduct(ProductReq productReq) {
//        log.debug("请求参数：{}", productReq);
//        PageRequest pageRequest = PageRequest.of(productReq.getPage(), productReq.getPageSize());
//        if (StringUtils.isNotBlank(productReq.getClause())) {
//            pageRequest = PageRequest.of(productReq.getPage(), productReq.getPageSize(), Sort.by(MallUtils.separateOrder(productReq.getClause())));
//        }
//        Page<EsProduct> esProductPage = esProductService.findAll(productReq.getKeyword(), pageRequest);
//        MallPage<ProductResp> productRespMallPage = MallUtils.toMallPage(esProductPage, ProductResp.class);
//        log.debug("返回结果：{}", productRespMallPage);
//        return MallResult.buildQueryOk(productRespMallPage);
//    }
//
//    /**
//     * 分页搜索全部搜索词
//     */
//    @ApiOperation(value = "分页搜索全部搜索词")
//    @GetMapping("/keyword")
//    public MallResult<MallPage<KeywordResp>> searchKeyword(KeywordReq keywordReq) {
//        log.debug("请求参数：{}", keywordReq);
//        PageRequest pageRequest = PageRequest.of(keywordReq.getPage(), keywordReq.getPageSize());
//        if (StringUtils.isNotBlank(keywordReq.getClause())) {
//            pageRequest = PageRequest.of(keywordReq.getPage(), keywordReq.getPageSize(), Sort.by(MallUtils.separateOrder(keywordReq.getClause())));
//        }
//        Page<EsKeyword> esKeywordPage = esKeywordService.findAll(keywordReq.getKeyword(), pageRequest);
//        MallPage<KeywordResp> keywordRespMallPage = MallUtils.toMallPage(esKeywordPage, KeywordResp.class);
//        log.debug("返回结果：{}", keywordRespMallPage);
//        return MallResult.buildQueryOk(keywordRespMallPage);
//    }
//}
