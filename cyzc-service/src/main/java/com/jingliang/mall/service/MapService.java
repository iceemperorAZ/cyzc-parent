package com.jingliang.mall.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lmd
 * @date 2020/4/27
 * @company 晶粮
 */
public interface MapService {

    /**
     * 通过地址获取经纬度
     *
     * @return
     */
    List<Object> getlngAndLat(MultipartFile multipartFile);
}
