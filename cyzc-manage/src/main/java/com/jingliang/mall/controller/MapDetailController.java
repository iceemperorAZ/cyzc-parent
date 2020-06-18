package com.jingliang.mall.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import com.jingliang.mall.service.MapDetailService;
import org.springframework.web.bind.annotation.RestController;

/**
 * 保存地图详情信息Controller
 * 
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-05-29 09:19:03
 */
@RequestMapping(value = "/front/mapDetail")
@RestController
@Slf4j
@Api(tags = "保存地图详情信息")
public class MapDetailController {

	private final MapDetailService mapDetailService;

	public MapDetailController (MapDetailService mapDetailService) {
		this.mapDetailService = mapDetailService;
	}

}