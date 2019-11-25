package com.jingliang.mall.controller;

import com.jingliang.mall.service.DepartmentService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门表Controller
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
@RestController
@Slf4j
@Api(tags = "部门")
@RequestMapping("/back/department")
public class DepartmentController {

	private final DepartmentService departmentService;

	public DepartmentController (DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

}