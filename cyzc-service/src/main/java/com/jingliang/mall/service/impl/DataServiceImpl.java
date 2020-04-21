package com.jingliang.mall.service.impl;

import com.jingliang.mall.repository.DataRepository;
import com.jingliang.mall.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 写点注释
 *
 * @author Zhenfeng Li
 * @date 2020-03-26 13:30:50
 */
@Service
@Slf4j
public class DataServiceImpl implements DataService {
    private final DataRepository dataRepository;

    public DataServiceImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<Map<String, Object>> newBuyerCount(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<Map<String, Object>> mapList = new ArrayList<>();
        //day,datetime
        Map<String, Object> allMap = new HashMap<>(dataRepository.countAllBuyerByDay(date));
        if (allMap.size() == 0) {
            allMap.put("datetime",dateFormat.format(date));
            allMap.put("day",0);
        }
        allMap.put("type", "总");
        Integer month = dataRepository.countAllBuyerByMonth(date);
        allMap.put("month", month);
        //统计所有用户
        Integer total = dataRepository.countAllBuyer();
        allMap.put("total", total);
        mapList.add(allMap);
        //查询所有区
        List<String> regions = dataRepository.allRegion();
        for (String region : regions) {
            //天  region，datetime ，day
            Map<String, Object> regionMap = new HashMap<>(dataRepository.countAllBuyerByRegionAndDay(date, region));
            if (regionMap.size() == 0) {
                regionMap.put("datetime",dateFormat.format(date));
                regionMap.put("day",0);
            }
            regionMap.put("region", region);
            regionMap.put("type", "区");
            month = dataRepository.countAllBuyerByRegionAndMonth(date, region);
            //月份
            regionMap.put("month", month);
            //全部
            total = dataRepository.countAllBuyerByRegion(region);
            regionMap.put("total", total);
            mapList.add(regionMap);
        }
        return mapList;
    }
}
