package com.jingliang.mall.service.impl;

//import com.jingliang.mall.common.MallBeanMapper;
//import com.jingliang.mall.dao.EsKeywordRepository;
//import com.jingliang.mall.dao.EsProductRepository;

import com.jingliang.mall.entity.Product;
import com.jingliang.mall.entity.Sku;
import com.jingliang.mall.repository.ProductRepository;
import com.jingliang.mall.repository.SkuRepository;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//import com.jingliang.mall.esdocument.EsKeyword;
//import com.jingliang.mall.esdocument.EsProduct;

/**
 * 商品表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Value("${product.sku.init.invented.num}")
    private Integer productSkuInitInventedNum;

    private final ProductRepository productRepository;
    private final SkuRepository skuRepository;
    //    private final EsProductRepository esProductRepository;
//    private final EsKeywordRepository esKeywordRepository;
    private final RedisService redisService;

    public ProductServiceImpl(ProductRepository productRepository,
                              SkuRepository skuRepository,/* EsProductRepository esProductRepository, EsKeywordRepository esKeywordRepository,*/ RedisService redisService) {
        this.productRepository = productRepository;
        this.skuRepository = skuRepository;
//        this.esProductRepository = esProductRepository;
//        this.esKeywordRepository = esKeywordRepository;
        this.redisService = redisService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product save(Product product) {
        product = productRepository.save(product);
        //如果不存在则创建库存，否则不处理
        if (Objects.isNull(skuRepository.findFirstByProductIdAndIsAvailable(product.getId(), true))) {
            Sku sku = new Sku();
            sku.setProductTypeId(product.getProductTypeId());
            sku.setProductTypeName(product.getProductTypeName());
            sku.setProductId(product.getId());
            sku.setProductName(product.getProductName());
            sku.setSkuNo(redisService.getSkuNo());
            sku.setCreateTime(product.getCreateTime());
            sku.setCreateUserId(product.getCreateUserId());
            sku.setCreateUserName(product.getCreateUserName());
            sku.setUpdateTime(product.getUpdateTime());
            sku.setUpdateUserId(product.getUpdateUserId());
            sku.setUpdateUserName(product.getUpdateUserName());
            //设置初始虚拟库存为8万
            //追加redis中的线上库存
            log.debug("追加redis中的线上库存，商品[{}],redis线上剩余库存为{}", sku.getProductName(), redisService.skuLineIncrement(String.valueOf(sku.getProductId()), productSkuInitInventedNum));
            sku.setSkuLineNum(productSkuInitInventedNum);
            sku.setSkuRealityNum(0);
            sku.setSkuHistoryTotalNum(0);
            sku.setIsAvailable(true);
            skuRepository.save(sku);
        }
        return product;
    }

    @Override
    public Page<Product> findAll(Specification<Product> productSpecification, PageRequest pageRequest) {
        return productRepository.findAll(productSpecification, pageRequest);
    }

    @Override
    public Product findAllById(Long id) {
        return productRepository.findAllByIdAndIsAvailable(id, true);
    }

    @Override
    public Product findAllByProductName(String productName) {
        return productRepository.findFirstByProductNameAndIsAvailable(productName, true);
    }

    @Override
    public Product findShowProductById(Long id) {
        return productRepository.findByIdAndIsShowAndIsAvailable(id, true, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Product> batchShow(List<Product> products) {
        products = productRepository.saveAll(products);
        products = productRepository.findAllByIsAvailableAndIdIn(true, products.stream().map(Product::getId).collect(Collectors.toList()));
        if (products.isEmpty()) {
            return products;
        }
//        List<EsKeyword> esKeywords = new ArrayList<>();
//        for (Product product : products) {
//            EsKeyword esKeyword = new EsKeyword();
//            esKeyword.setId(product.getProductTypeId());
//            esKeyword.setKeyword(product.getProductTypeName());
//            esKeywords.add(esKeyword);
//        }
//        //保存搜索词到es
//        esKeywordRepository.saveAll(MallBeanMapper.mapList(esKeywords, EsKeyword.class));
//        List<EsProduct> esProducts = MallBeanMapper.mapList(products, EsProduct.class);
//        esProductRepository.saveAll(esProducts);
        return products;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Product> batchHide(List<Product> products) {
        if (productRepository.findAllByIsAvailableAndIdIn(true, products.stream().map(Product::getId).collect(Collectors.toList())).isEmpty()) {
            return new ArrayList<>();
        }
//        esProductRepository.deleteAll(MallBeanMapper.mapList(products, EsProduct.class));
        return productRepository.saveAll(products);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Product> batchDelete(List<Product> products) {
        List<Sku> skus = new ArrayList<>();
        for (Product product : products) {
            //移除redis中的商品库存
            redisService.removeProductSkuNum(product.getId() + "");
            //查询存
            Sku sku = skuRepository.findFirstByProductIdAndIsAvailable(product.getId(), true);
            sku.setProductId(product.getId());
            sku.setIsAvailable(false);
            skus.add(sku);
        }
        //同时删除库存
        skuRepository.saveAll(skus);
        return productRepository.saveAll(products);
    }

    @Override
    public List<Product> findAllByProductNameLike(String productName) {
        return productRepository.findAllByIsAvailableAndProductNameLike(true, "%" + productName + "%");
    }

    @Override
    public List<Product> saveAll(List<Product> products) {
        return productRepository.saveAll(products);
    }

    @Override
    public List<Product> findAllByProductZoneId(Long productZoneId) {
        return productRepository.findAllByProductZoneIdAndIsAvailable(productZoneId, true);
    }

    @Override
    public Integer countByProductTypeIdAnnShow(Long productTypeId, Boolean isShow) {
        return productRepository.countAllByProductTypeIdAndIsShow(productTypeId, isShow);
    }

    @Override
    public Product findAllByProductTypeIdAndSort(Long productTypeId, Integer productSort) {
        return productRepository.findFirstByProductTypeIdAndProductSortAndIsAvailable(productTypeId, productSort, true);
    }

    @Override
    public Product findByProductNameAndSpecs(String productName, String specs) {
        return productRepository.findFirstByProductNameAndSpecsAndIsAvailable(productName, specs, true);
    }
}