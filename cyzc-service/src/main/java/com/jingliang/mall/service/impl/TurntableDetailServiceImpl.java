package com.jingliang.mall.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.jingliang.mall.service.TurntableDetailService;
import org.springframework.stereotype.Service;
import com.jingliang.mall.repository.TurntableDetailRepository;

/**
 * 转盘详情ServiceImpl
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@Service
@Slf4j
public class TurntableDetailServiceImpl implements TurntableDetailService {

	private final TurntableDetailRepository turntableDetailRepository;

	public TurntableDetailServiceImpl (TurntableDetailRepository turntableDetailRepository) {
		this.turntableDetailRepository = turntableDetailRepository;
	}

}