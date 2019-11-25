package com.jingliang.mall.service;

import com.jingliang.mall.entity.SearchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * 历史搜索表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-05 12:21:02
 */
public interface SearchHistoryService {

    /**
     * 分页查询所有历史搜索
     *
     * @param searchHistorySpecification 查询条件
     * @param pageRequest                分页条件
     * @return 返回查询到的搜索历史列表
     */
    Page<SearchHistory> findAll(Specification<SearchHistory> searchHistorySpecification, PageRequest pageRequest);

    /**
     * 保存搜索历史
     *
     * @param searchHistory 搜索历史
     * @return 返回保存后的搜索历史
     */
    SearchHistory save(SearchHistory searchHistory);
}