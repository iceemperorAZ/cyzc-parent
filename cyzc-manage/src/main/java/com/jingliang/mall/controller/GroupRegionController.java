package com.jingliang.mall.controller;

import lombok.extern.slf4j.Slf4j;
import com.jingliang.mall.service.GroupRegionService;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组与区域映射关系表Controller
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
@RestController
@RequestMapping(value = "/back/groupRegion")
@Slf4j
@Api(tags = "组与区域映射关系表")
public class GroupRegionController {

	private final GroupRegionService groupRegionService;

	public GroupRegionController (GroupRegionService groupRegionService) {
		this.groupRegionService = groupRegionService;
	}

}