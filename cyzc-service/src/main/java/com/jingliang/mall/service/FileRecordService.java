package com.jingliang.mall.service;

import com.jingliang.mall.entity.FileRecord;

/**
 * 文件记录表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-29 11:18:02
 */
public interface FileRecordService {
    /**
     * 保存文件上传记录
     *
     * @param fileRecord 文件记录对象
     * @return 返回保存后的文件上传记录
     */
    public FileRecord save(FileRecord fileRecord);
}