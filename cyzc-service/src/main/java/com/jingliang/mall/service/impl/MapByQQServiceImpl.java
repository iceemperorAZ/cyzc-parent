package com.jingliang.mall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.service.MapService;
import com.jingliang.mall.utils.ReadExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jingliang.mall.utils.MultipartFileToFile.delteTempFile;
import static com.jingliang.mall.utils.MultipartFileToFile.multipartFileToFile;
import static com.jingliang.mall.utils.StaticField.*;


/**
 * @author lmd
 * @date 2020/4/22
 * @company
 */
@Service
@Slf4j
public class MapByQQServiceImpl implements MapService {

    /**
     * 通过地址获取经纬度
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getlngAndLat(MultipartFile multipartFile) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        //MultipartFile转换为File
        File file = multipartFileToFile(multipartFile);
        //调用读取excel表格方法
        List<String> listExcel = ReadExcelUtil.readExcel(file);
        assert listExcel != null;
        //遍历获取单个地址
        for (String address : listExcel) {
            int num = 0;
            //腾讯地图API
            String key = "DXCBZ-ICIWF-JL5JP-J64EA-CBMEJ-JTFKP";
            String url = "https://apis.map.qq.com/ws/geocoder/v1/?address=" + address + "&key=" + key;
            String json = loadJSON(url);
            //判断json是否为null或者是空字符串
            if (StringUtils.isNotBlank(json)) {
                Map<String, Object> map = new HashMap<>();
                //转成json
                JSONObject jsonObject = JSON.parseObject(json);
                String status = jsonObject.getString(STATUS);
                if ("0".equals(status)) {
                    Double lng = jsonObject.getJSONObject(RESULT).getJSONObject(LOCATION).getDouble(LNG); // 经度
                    Double lat = jsonObject.getJSONObject(RESULT).getJSONObject(LOCATION).getDouble(LAT); // 纬度
                    log.debug("返回结果：{}", "地址：" + address + "----纬度：" + lat + "-----经度：" + lng);
                    map.put(ADDRESS, address);
                    map.put(LONGITUDE, lng);
                    map.put(LATITUDE, lat);
                    listMap.add(map);
                }
            }
            delteTempFile(file);
        }
        return listMap;
    }

    /**
     * 解析url地址
     *
     * @param url
     * @return
     */
    public String loadJSON(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Thread.sleep(200);
            URL url1 = new URL(url);
            URLConnection yc = url1.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            in.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();

    }


}
