package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.GroupRegion;
import com.jingliang.mall.entity.Region;
import com.jingliang.mall.repository.RegionRepository;
import lombok.extern.slf4j.Slf4j;
import com.jingliang.mall.service.GroupRegionService;
import org.springframework.stereotype.Service;
import com.jingliang.mall.repository.GroupRegionRepository;

import java.util.Date;

/**
 * 组与区域映射关系表ServiceImpl
 * 
 * @author Mengde Liu
 * @version 1.0.0
 * @date 2020-04-24 11:39:41
 */
@Service
@Slf4j
public class GroupRegionServiceImpl implements GroupRegionService {

	private final GroupRegionRepository groupRegionRepository;
	private final RegionRepository regionRepository;

	public GroupRegionServiceImpl(GroupRegionRepository groupRegionRepository, RegionRepository regionRepository) {
		this.groupRegionRepository = groupRegionRepository;
		this.regionRepository = regionRepository;
	}

	/**
	 * 根据组id查询映射表,获取区域id，查询对应区域
	 * @param groupId
	 * @return
	 */
	@Override
	public Region findRegionByGroupId(Long groupId) {
		GroupRegion groupRegion = groupRegionRepository.findGroupRegionByGroupId(groupId);
		return regionRepository.findRegionByIdAndIsAvailable(groupRegion.getRegionId(), true);
	}

	/**
	 * 保存或者修改组与区域映射关系表
	 * @param groupRegion
	 * @return
	 */
	@Override
	public GroupRegion saveGroupRegion(GroupRegion groupRegion) {
		return groupRegionRepository.save(groupRegion);
	}

	/**
	 * 删除组和区域的绑定关系（修改可用性）
	 * @param groupRegion
	 * @return
	 */
	@Override
	public GroupRegion updateIsAvailable(GroupRegion groupRegion) {
		GroupRegion gr = new GroupRegion();
		gr.setId(groupRegion.getId());
		gr.setGroupId(groupRegion.getGroupId());
		gr.setRegionId(groupRegion.getRegionId());
		gr.setCreateTime(new Date());
		if(groupRegion.getIsAvailable()){
			gr.setIsAvailable(false);
		}else{
			gr.setIsAvailable(true);
		}
		return groupRegionRepository.save(groupRegion);
	}


}