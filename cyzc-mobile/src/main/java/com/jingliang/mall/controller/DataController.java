package com.jingliang.mall.controller;

import com.jingliang.mall.common.Result;
import com.jingliang.mall.service.DataService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 数据统计
 *
 * @author Zhenfeng Li
 * @date 2020-03-26 13:22:21
 */
@RestController
public class DataController {
    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

//    @GetMapping("/data/new/buyer/count")
    @GetMapping("/front/data/new/buyer/count")
    public Result<List<Map<String, Object>>> newBuyerCount(@DateTimeFormat(pattern = "yyyy-MM-dd") Date queryDate) {
        return Result.buildQueryOk(dataService.newBuyerCount(queryDate));
    }


}
