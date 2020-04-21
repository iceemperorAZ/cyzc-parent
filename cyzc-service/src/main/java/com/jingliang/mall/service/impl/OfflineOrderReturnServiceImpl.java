package com.jingliang.mall.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.jingliang.mall.service.OfflineOrderReturnService;
import org.springframework.stereotype.Service;
import com.jingliang.mall.repository.OfflineOrderReturnRepository;

/**
 * 退货表ServiceImpl
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-20 17:57:20
 */
@Service
@Slf4j
public class OfflineOrderReturnServiceImpl implements OfflineOrderReturnService {

	private final OfflineOrderReturnRepository offlineOrderReturnRepository;

	public OfflineOrderReturnServiceImpl (OfflineOrderReturnRepository offlineOrderReturnRepository) {
		this.offlineOrderReturnRepository = offlineOrderReturnRepository;
	}

}