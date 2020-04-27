package com.jingliang.mall.repository;

import com.jingliang.mall.entity.GroupRegion;
import com.jingliang.mall.repository.base.BaseRepository;

/**
 * 组与区域映射关系表Repository
 * 
 * @author Mengde Liu
 * @version 1.0.0
 * @date 2020-04-24 11:39:41
 */
public interface GroupRegionRepository extends BaseRepository<GroupRegion, Long> {

    /**
     * 根据组id查询映射表
     * @param groupId
     * @return
     */
    GroupRegion findGroupRegionByGroupId(Long groupId);

}