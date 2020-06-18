package com.jingliang.mall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.MapDetail;
import com.jingliang.mall.entity.MapPoint;
import com.jingliang.mall.repository.MapDetailRepository;
import com.jingliang.mall.repository.MapPointRepository;
import com.jingliang.mall.utils.ReadExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.jingliang.mall.service.MapPointService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import java.io.File;

import static com.jingliang.mall.utils.MultipartFileToFile.delteTempFile;
import static com.jingliang.mall.utils.MultipartFileToFile.multipartFileToFile;
import static com.jingliang.mall.utils.StaticField.*;
import static com.jingliang.mall.utils.StaticField.LATITUDE;

/**
 * 保存地图信息ServiceImpl
 *
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-05-29 09:19:03
 */
@Service
@Slf4j
public class MapPointServiceImpl implements MapPointService {

    private final MapPointRepository mapPointRepository;
    private final MapDetailRepository mapDetailRepository;

    public MapPointServiceImpl(MapPointRepository mapPointRepository, MapDetailRepository mapDetailRepository) {
        this.mapPointRepository = mapPointRepository;
        this.mapDetailRepository = mapDetailRepository;
    }

    /*
     * 解析地址信息并存入数据库
     * */
    @Override
    public List<Map<String, Object>> saveMapAndShowMap(String mapNo, MultipartFile multipartFile, Long id) {
        MapPoint mapPoint = new MapPoint();
        //判断是否传文件编号
        if (StringUtils.isBlank(mapNo)) {
            UUID uuid = UUID.randomUUID();
            String ud = uuid.toString();
            mapNo = ud.substring(0, 8).concat(ud.substring(10, 12));
        } else {
            List<MapDetail> mapdes = mapDetailRepository.findAllByMapNoAndIsAvailable(mapNo, true);
            for (MapDetail mapDetail : mapdes) {
                mapDetailRepository.save(mapDetail);
            }
        }
        //文件编号保存到实体类
        mapPoint.setMapNo(mapNo);
        List<Map<String, Object>> listMap = new ArrayList<>();
        //MultipartFile转换为File
        File file = new File("D:/经纬度.xlsx");
        file = multipartFileToFile(multipartFile);
        //文件名保存到实体类
        mapPoint.setMapName(file.getName());
        mapPoint.setUserId(id);
        mapPoint.setIsAvailable(true);
        //保存文件信息到数据库
        mapPointRepository.save(mapPoint);
        //调用读取excel表格方法
        List<String> listExcel = ReadExcelUtil.readExcel(file);
        assert listExcel != null;
        int i = 0;
        //遍历获取单个地址
        for (String address : listExcel) {
            Map<String, Object> map = new HashMap<>();
            MapDetail mapDetail = new MapDetail();
            String json = loadJSON(address);
            //判断json是否为null或者是空字符串
            if (StringUtils.isNotBlank(json)) {
                //转成json
                JSONObject jsonObject = JSON.parseObject(json);
                String status = jsonObject.getString(STATUS);
                if ("0".equals(status)) {
                    Double lng = jsonObject.getJSONObject(RESULT).getJSONObject(LOCATION).getDouble(LNG); // 经度
                    Double lat = jsonObject.getJSONObject(RESULT).getJSONObject(LOCATION).getDouble(LAT); // 纬度
                    //向实体类中保存经纬度编号及地址信息
                    mapDetail.setMapNo(mapNo);
                    mapDetail.setAddressDetail(address);
                    mapDetail.setLatitude(lat);
                    mapDetail.setLongitude(lng);
                    mapDetail.setIsAvailable(true);
                    mapDetail.setPointNum(i);
                    MapDetail mp = mapDetailRepository.save(mapDetail);
                    log.debug("返回结果：{}", "地址：" + address + "----纬度：" + lat + "-----经度：" + lng);
                    map.put(ADDRESS, address);
                    map.put(LONGITUDE, lng);
                    map.put(LATITUDE, lat);
                    map.put("mapNo", mapNo);
                    map.put("pointNum", i++);
                    map.put("id",mp.getId());
                    listMap.add(map);
                }
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return listMap;
            }
            delteTempFile(file);
        }
        return listMap;
    }

    /*
     * 更新点位信息，并删除数据库之前保存的信息
     * */
    @Override
    public Boolean updateMap(Long id, String address) {
        Boolean flg = true;
        MapDetail mapDetail = new MapDetail();
        //解析地址经纬度信息
        String json = loadJSON(address);
        if (StringUtils.isNotBlank(json)) {
            //转成json
            JSONObject jsonObject = JSON.parseObject(json);
            String status = jsonObject.getString(STATUS);
            if ("0".equals(status)) {
                Double lng = jsonObject.getJSONObject(RESULT).getJSONObject(LOCATION).getDouble(LNG); // 经度
                Double lat = jsonObject.getJSONObject(RESULT).getJSONObject(LOCATION).getDouble(LAT); // 纬度
                mapDetail.setLongitude(lng);
                mapDetail.setLatitude(lat);
                mapDetail.setAddressDetail(address);
                mapDetail.setId(id);
                mapDetailRepository.save(mapDetail);
            }
        }
        if (flg) {
            return flg;
        }
        return false;
    }

    @Override
    @Transactional
    public List<MapDetail> deleteMap(String mapNo) {
        List<MapDetail> maps = mapDetailRepository.findAllByMapNoAndIsAvailable(mapNo, true);
        for (MapDetail mapDetail1 : maps) {
            mapDetail1.setIsAvailable(false);
            mapDetailRepository.save(mapDetail1);
        }
        List<MapDetail> mapDetails = mapDetailRepository.findAllByMapNoAndIsAvailable(mapNo, true);
        return mapDetails;
    }

    @Override
    public MapDetail searchPointByMapNo(Long id) {
        return mapDetailRepository.findByIdAndIsAvailable(id,true);
    }

    /**
     * 解析url地址
     *
     * @param address
     * @return
     */
    public String loadJSON(String address) {
        //腾讯地图API
        String key = "5BOBZ-SOWRW-DO7RT-OLMIO-ZCZNV-TIBEQ";
        String url = "https://apis.map.qq.com/ws/geocoder/v1/?address=" + address + "&key=" + key;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //TODO 因为腾讯地图api调用的key是个人的，并发上限5，所以在此让线程睡眠来保证可以获取到数据
            // 这个睡眠值是根据excel中地址数测试得到的，不具有通用性
            URL url1 = new URL(url);
            URLConnection yc = url1.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}