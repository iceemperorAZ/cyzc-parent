package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.FileRecord;
import com.jingliang.mall.repository.FileRecordRepository;
import com.jingliang.mall.service.FileRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 文件记录表ServiceImpl
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-29 11:18:02
 */
@Service
@Slf4j
public class FileRecordServiceImpl implements FileRecordService {

	private final FileRecordRepository fileRecordRepository;

	public FileRecordServiceImpl (FileRecordRepository fileRecordRepository) {
		this.fileRecordRepository = fileRecordRepository;
	}
	@Override
	public FileRecord save(FileRecord fileRecord) {
		return fileRecordRepository.save(fileRecord);
	}
}