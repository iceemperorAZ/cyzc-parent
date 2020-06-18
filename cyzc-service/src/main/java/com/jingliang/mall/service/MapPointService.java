package com.jingliang.mall.service;

import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.MapDetail;
import com.rabbitmq.client.LongString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 保存地图信息Service
 *
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-05-29 09:19:03
 */
public interface MapPointService {

    public List<Map<String, Object>> saveMapAndShowMap(String mapNo, MultipartFile multipartFile, Long id);

    public Boolean updateMap(Long id, String address);

    public List<MapDetail> deleteMap(String mapNo);

    MapDetail searchPointByMapNo(Long id);

}