package com.jingliang.mall.server.impl;


import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.jingliang.mall.server.FastdfsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * FastdfsService接口实现
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-21 11:22
 */
@Service
public class FastdfsServiceImpl implements FastdfsService {
    @Value("${server.img.url}")
    private String imgUrl;
    private final FastFileStorageClient storageClient;

    public FastdfsServiceImpl(FastFileStorageClient storageClient) {
        this.storageClient = storageClient;
    }

    @Override
    public String uploadFile(byte[] fileBytes, String extName) {
        ByteArrayInputStream stream = new ByteArrayInputStream(fileBytes);
        StorePath storePath = storageClient.uploadFile(stream, fileBytes.length, extName, null);
        return imgUrl + "/" + storePath.getFullPath();
    }

    @Override
    public String uploadFile(InputStream inputStream, String extName) throws IOException {
        StorePath storePath = storageClient.uploadFile(inputStream, inputStream.available(), extName, null);
        return imgUrl + "/" + storePath.getFullPath();
    }

    @Override
    public boolean deleteFile(String groupName, String fileName) {
        storageClient.deleteFile(groupName, fileName);
        return true;
    }

    @Override
    public boolean deleteFile(String uri) {
        if (StringUtils.isBlank(uri)) {
            return true;
        }
        storageClient.deleteFile(uri.replace(imgUrl + "/", ""));
        return true;
    }
}
