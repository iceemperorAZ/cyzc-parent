package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.SearchHistory;
import com.jingliang.mall.repository.SearchHistoryRepository;
import com.jingliang.mall.service.SearchHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * 历史搜索表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-05 12:21:02
 */
@Service
@Slf4j
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    public SearchHistoryServiceImpl(SearchHistoryRepository searchHistoryRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
    }

    @Override
    public Page<SearchHistory> findAll(Specification<SearchHistory> searchHistorySpecification, PageRequest pageRequest) {
        return searchHistoryRepository.findAll(searchHistorySpecification, pageRequest);
    }

    @Override
    public SearchHistory save(SearchHistory searchHistory) {
        return searchHistoryRepository.save(searchHistory);
    }
}