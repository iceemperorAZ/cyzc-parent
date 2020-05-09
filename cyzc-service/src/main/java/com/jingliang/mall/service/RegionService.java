package com.jingliang.mall.service;

import com.jingliang.mall.entity.Region;

import java.util.List;

/**
 * 区域表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-25 19:09:47
 */
public interface RegionService {

    /**
     * 查询区域信息
     *
     * @param parentCode 父编码
     * @return 返回区域信息
     */
    List<Region> findByParentCode(String parentCode);

    String findByCode(String provinceCode);
}