package com.jingliang.mall.server.impl;

import com.jingliang.mall.dao.EsProductRepository;
import com.jingliang.mall.esdocument.EsProduct;
import com.jingliang.mall.server.EsProductService;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

/**
 * EsService实现
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-27 22:59
 */
@Service
public class EsProductServiceImpl implements EsProductService {
    private final EsProductRepository esProductRepository;

    public EsProductServiceImpl(EsProductRepository esProductRepository) {
        this.esProductRepository = esProductRepository;
    }

    @Override
    public Page<EsProduct> findAll(String keyword, Pageable pageable) {
        DisMaxQueryBuilder disMaxQueryBuilder = QueryBuilders.disMaxQuery();
        //商品名称全拼优先级最高   .boost(5) 暂时不太理解什么意思
        MatchQueryBuilder fullProductNameMatchQueryBuilder = QueryBuilders.matchQuery("productName.full_pinyin", keyword).boost(5);
        //商品名称简拼  minimumShouldMatch("100%") 暂时不太理解什么意思
        QueryBuilder simpleProductNameMatchQueryBuilder = QueryBuilders.matchQuery("productName.simple_pinyin", keyword).minimumShouldMatch("100%");
        //商品类型名称全拼
        QueryBuilder fullProductTypeNameMatchQueryBuilder = QueryBuilders.matchQuery("productTypeName.simple_pinyin", keyword).minimumShouldMatch("100%");
        //商品类型名称简拼
        QueryBuilder simpleProductTypeNameMatchQueryBuilder = QueryBuilders.matchQuery("productTypeName.simple_pinyin", keyword).minimumShouldMatch("100%");
        disMaxQueryBuilder.add(fullProductNameMatchQueryBuilder);
        disMaxQueryBuilder.add(simpleProductNameMatchQueryBuilder);
        disMaxQueryBuilder.add(fullProductTypeNameMatchQueryBuilder);
        disMaxQueryBuilder.add(simpleProductTypeNameMatchQueryBuilder);
        SearchQuery query = new NativeSearchQueryBuilder().withSearchType(SearchType.DFS_QUERY_THEN_FETCH).withQuery(disMaxQueryBuilder).withPageable(pageable).build();
        return esProductRepository.search(query);
    }
}
