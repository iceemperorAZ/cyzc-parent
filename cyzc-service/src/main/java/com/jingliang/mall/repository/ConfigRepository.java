package com.jingliang.mall.repository;


import com.jingliang.mall.entity.Config;
import com.jingliang.mall.repository.base.BaseRepository;

/**
 * 配置文件Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-03 15:59:18
 */
public interface ConfigRepository extends BaseRepository<Config, Long> {

    /**
     * 根据编码查询配置信息
     *
     * @param code        编码
     * @param isAvailable 是否可用
     * @return 返回查询到的配置信息
     */
    Config findFirstByCodeAndIsAvailable(String code, Boolean isAvailable);

}