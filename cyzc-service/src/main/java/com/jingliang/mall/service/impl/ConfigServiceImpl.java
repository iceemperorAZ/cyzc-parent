package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Config;
import com.jingliang.mall.repository.ConfigRepository;
import com.jingliang.mall.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * 配置文件ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-03 15:59:18
 */
@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    private final ConfigRepository configRepository;

    public ConfigServiceImpl(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public Config findByCode(String code) {
        return configRepository.findFirstByCodeAndIsAvailable(code, true);
    }

    @Override
    public Config save(Config config) {
        return configRepository.save(config);
    }

    @Override
    public Page<Config> findAll(Specification<Config> configSpecification, PageRequest pageRequest) {
        return configRepository.findAll(configSpecification, pageRequest);
    }
}