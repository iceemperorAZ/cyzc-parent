package com.jingliang.mall.controller;

import com.jingliang.mall.common.Result;
import com.jingliang.mall.service.MapService;
import com.jingliang.mall.utils.ReadExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.jingliang.mall.utils.MultipartFileToFile.delteTempFile;
import static com.jingliang.mall.utils.MultipartFileToFile.multipartFileToFile;


/**
 * @author lmd
 * @date 2020/4/22
 * @company
 */
@RestController
@Slf4j
@RequestMapping(value = "/back/map")
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    /**
     * 获取excel表格并读取内容，返回经纬度
     *
     * @param multipartFile
     * @return
     */
    @PostMapping("/getMap")
    public Result<List<Map<String, Object>>> getMap(@RequestParam("excel") MultipartFile multipartFile) {
        return Result.buildOk(mapService.getlngAndLat(multipartFile));
    }


}
