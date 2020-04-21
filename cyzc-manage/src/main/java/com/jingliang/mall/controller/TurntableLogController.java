package com.jingliang.mall.controller;

import lombok.extern.slf4j.Slf4j;
import com.jingliang.mall.service.TurntableLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

/**
 * 转盘日志Controller
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@RestController
@RequestMapping(value = "/back/turntableLog")
@Slf4j
@Api(tags = "转盘日志")
public class TurntableLogController {

	private final TurntableLogService turntableLogService;

	public TurntableLogController (TurntableLogService turntableLogService) {
		this.turntableLogService = turntableLogService;
	}

}