package com.jingliang.mall.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.jingliang.mall.service.GroupRegionService;
import org.springframework.stereotype.Service;
import com.jingliang.mall.repository.GroupRegionRepository;

/**
 * 组与区域映射关系表ServiceImpl
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
@Service
@Slf4j
public class GroupRegionServiceImpl implements GroupRegionService {

	private final GroupRegionRepository groupRegionRepository;

	public GroupRegionServiceImpl (GroupRegionRepository groupRegionRepository) {
		this.groupRegionRepository = groupRegionRepository;
	}

}