package com.jingliang.mall.service;

import com.jingliang.mall.entity.Config;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * 配置文件Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-03 15:59:18
 */
public interface ConfigService {

    /**
     * 根据编码查询配置信息
     *
     * @param code 编码
     * @return 返回查询到的配置信息
     */
    Config findByCode(String code);

    /**
     * 保存/修改配置信息
     *
     * @param config 配置信息
     * @return 返回保存后的配置信息
     */
    Config save(Config config);

    /**
     * 分页查询全部配置
     *
     * @param configSpecification 查询条件
     * @param pageRequest         分页条件
     * @return 返回查询到的配置列表
     */
    Page<Config> findAll(Specification<Config> configSpecification, PageRequest pageRequest);
}