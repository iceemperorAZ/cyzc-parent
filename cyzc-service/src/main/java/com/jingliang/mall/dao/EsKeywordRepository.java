package com.jingliang.mall.dao;

import com.jingliang.mall.esdocument.EsKeyword;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 搜索词Repository
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-10-05 18:01
 */
public interface EsKeywordRepository extends ElasticsearchRepository<EsKeyword, Long> {

}
