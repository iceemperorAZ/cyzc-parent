package com.jingliang.mall.controller;

import com.jingliang.mall.service.FileRecordService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件记录表Controller
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-29 11:18:02
 */
@RequestMapping("/front/fileRecord")
@Api(tags = "文件记录")
@RestController
@Slf4j
public class FileRecordController {

	private final FileRecordService fileRecordService;

	public FileRecordController (FileRecordService fileRecordService) {
		this.fileRecordService = fileRecordService;
	}

}