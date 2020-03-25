package com.jingliang.mall.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.jingliang.mall.repository.OfflineOrderRepository;
import org.springframework.stereotype.Service;
import com.jingliang.mall.service.OfflineOrderService;

/**
 * 线下订单ServiceImpl
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-25 10:05:44
 */
@Service
@Slf4j
public class OfflineOrderServiceImpl implements OfflineOrderService {

	private final OfflineOrderRepository offlineOrderRepository;

	public OfflineOrderServiceImpl (OfflineOrderRepository offlineOrderRepository) {
		this.offlineOrderRepository = offlineOrderRepository;
	}

}