package com.jingliang.mall.controller;

import lombok.extern.slf4j.Slf4j;
import com.jingliang.mall.service.OfflineOrderReturnService;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

/**
 * 退货表Controller
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-20 17:57:20
 */
@RestController
@RequestMapping(value = "/back/offlineOrderReturn")
@Api(tags = "退货表")
@Slf4j
public class OfflineOrderReturnController {

	private final OfflineOrderReturnService offlineOrderReturnService;

	public OfflineOrderReturnController (OfflineOrderReturnService offlineOrderReturnService) {
		this.offlineOrderReturnService = offlineOrderReturnService;
	}
}