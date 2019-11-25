package com.jingliang.mall.service.impl;

import com.jingliang.mall.repository.DepartmentRepository;
import com.jingliang.mall.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 部门表ServiceImpl
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

	private final DepartmentRepository departmentRepository;

	public DepartmentServiceImpl (DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

}