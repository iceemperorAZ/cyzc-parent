//package com.jingliang.mall.server.impl;
//
//import com.jingliang.mall.dao.EsKeywordRepository;
//import com.jingliang.mall.esdocument.EsKeyword;
//import com.jingliang.mall.server.EsKeywordService;
//import lombok.extern.slf4j.Slf4j;
//import org.elasticsearch.action.search.SearchType;
//import org.elasticsearch.index.query.DisMaxQueryBuilder;
//import org.elasticsearch.index.query.MatchQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.data.elasticsearch.core.query.SearchQuery;
//import org.springframework.stereotype.Service;
//
///**
// * Es搜索词服务实现类
// *
// * @author Zhenfeng Li
// * @version 1.0
// * @date 2019-10-05 18:00
// */
//@Service
//@Slf4j
//public class EsKeywordServiceImpl implements EsKeywordService {
//
//    private final EsKeywordRepository esKeywordRepository;
//
//    public EsKeywordServiceImpl(EsKeywordRepository esKeywordRepository) {
//        this.esKeywordRepository = esKeywordRepository;
//    }
//
//    @Override
//    public Page<EsKeyword> findAll(String keyword, PageRequest pageRequest) {
//        DisMaxQueryBuilder disMaxQueryBuilder = QueryBuilders.disMaxQuery();
//        //搜索词全拼优先级最高   .boost(5) 暂时不太理解什么意思
//        MatchQueryBuilder fullKeywordMatchQueryBuilder = QueryBuilders.matchQuery("keyword.full_pinyin", keyword).boost(5);
//        //搜索词简拼  minimumShouldMatch("100%") 暂时不太理解什么意思
//        QueryBuilder simpleKeywordMatchQueryBuilder = QueryBuilders.matchQuery("keyword.simple_pinyin", keyword).minimumShouldMatch("100%");
//        disMaxQueryBuilder.add(fullKeywordMatchQueryBuilder);
//        disMaxQueryBuilder.add(simpleKeywordMatchQueryBuilder);
//        SearchQuery query = new NativeSearchQueryBuilder().withSearchType(SearchType.DFS_QUERY_THEN_FETCH).withQuery(disMaxQueryBuilder).withPageable(pageRequest).build();
//        return esKeywordRepository.search(query);
//    }
//}
