package com.jingliang.mall.repository;

import com.jingliang.mall.repository.base.BaseRepository;
import com.jingliang.mall.entity.Region;

import java.util.List;

/**
 * 区域表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-25 19:09:47
 */
public interface RegionRepository extends BaseRepository<Region, Long> {
    /**
     * 根据父编码查询区域列表
     *
     * @param parentCode  父编码
     * @param isAvailable 是否可用
     * @return 返回查询到的区域列表
     */
    List<Region> findAllByParentCodeAndIsAvailable(String parentCode, Boolean isAvailable);

    /**
     * 根据区域id查询区域
     */
    Region findRegionByIdAndIsAvailable(Long regionId,Boolean isAvailable);

    Region findRegionByCodeAndIsAvailable(String code, boolean b);
}