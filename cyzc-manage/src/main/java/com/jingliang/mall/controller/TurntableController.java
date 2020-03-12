package com.jingliang.mall.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jingliang.mall.service.TurntableService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

/**
 * 转盘Controller
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@Api(tags = "转盘")
@RestController
@RequestMapping(value = "/back/turntable")
@Slf4j
public class TurntableController {

	private final TurntableService turntableService;

	public TurntableController (TurntableService turntableService) {
		this.turntableService = turntableService;
	}

}