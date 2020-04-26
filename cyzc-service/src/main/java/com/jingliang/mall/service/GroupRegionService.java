package com.jingliang.mall.service;

import com.jingliang.mall.entity.GroupRegion;
import com.jingliang.mall.entity.Region;

/**
 * 组与区域映射关系表Service
 * 
 * @author Mengde Liu
 * @version 1.0.0
 * @date 2020-04-24 11:39:41
 */
public interface GroupRegionService {

    /**
     * 根据组id查询映射表,获取区域id，查询对应区域
     * @param groupId
     * @return
     */
    Region findRegionByGroupId(Long groupId);

    /**
     * 保存或者修改组与区域映射关系表
     * @param groupRegion
     * @return
     */
    GroupRegion saveGroupRegion(GroupRegion groupRegion);

    /**
     * 删除组和区域的绑定关系（修改可用性）
     * @param groupRegion
     * @return
     */
    GroupRegion updateIsAvailable(GroupRegion groupRegion);

}