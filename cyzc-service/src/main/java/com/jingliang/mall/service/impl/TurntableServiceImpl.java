package com.jingliang.mall.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.jingliang.mall.repository.TurntableRepository;
import com.jingliang.mall.service.TurntableService;

/**
 * 转盘ServiceImpl
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@Service
@Slf4j
public class TurntableServiceImpl implements TurntableService {

	private final TurntableRepository turntableRepository;

	public TurntableServiceImpl (TurntableRepository turntableRepository) {
		this.turntableRepository = turntableRepository;
	}

}