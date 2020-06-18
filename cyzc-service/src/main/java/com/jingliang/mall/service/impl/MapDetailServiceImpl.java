package com.jingliang.mall.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.jingliang.mall.repository.MapDetailRepository;
import com.jingliang.mall.service.MapDetailService;

/**
 * 保存地图详情信息ServiceImpl
 * 
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-05-29 09:19:03
 */
@Service
@Slf4j
public class MapDetailServiceImpl implements MapDetailService {

	private final MapDetailRepository mapDetailRepository;

	public MapDetailServiceImpl (MapDetailRepository mapDetailRepository) {
		this.mapDetailRepository = mapDetailRepository;
	}

}