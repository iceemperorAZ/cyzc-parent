package com.jingliang.mall.controller;

import lombok.extern.slf4j.Slf4j;
import com.jingliang.mall.service.TurntableDetailService;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

/**
 * 转盘详情Controller
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@Api(tags = "转盘详情")
@RestController
@RequestMapping(value = "/back/turntableDetail")
@Slf4j
public class TurntableDetailController {

	private final TurntableDetailService turntableDetailService;

	public TurntableDetailController (TurntableDetailService turntableDetailService) {
		this.turntableDetailService = turntableDetailService;
	}

}