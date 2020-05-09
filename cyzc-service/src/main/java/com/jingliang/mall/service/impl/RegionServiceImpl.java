package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Region;
import com.jingliang.mall.repository.RegionRepository;
import com.jingliang.mall.service.RegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 区域表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-25 19:09:47
 */
@Service
@Slf4j
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public List<Region> findByParentCode(String parentCode) {
        return regionRepository.findAllByParentCodeAndIsAvailable(parentCode, true);
    }

    @Override
    public String findByCode(String code) {
        return regionRepository.findRegionByCodeAndIsAvailable(code,true).getName();
    }
}