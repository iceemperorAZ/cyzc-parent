package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Region;
import com.jingliang.mall.resp.RegionResp;
import com.jingliang.mall.service.RegionService;
import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 区域表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-25 19:09:47
 */
@Api(description = "区域")
@RestController
@RequestMapping("/back/region")
@Slf4j
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @ApiOperation(description = "查询行政区域")
    @GetMapping("/find")
    public Result<List<RegionResp>> findByParentCode(String parentCode) {
        log.debug("请求参数：{}", parentCode);
        List<Region> regions = regionService.findByParentCode(parentCode);
        List<RegionResp> regionRespList = BeanMapper.mapList(regions, RegionResp.class);
        log.debug("返回结果：{}", regionRespList);
        return Result.buildQueryOk(regionRespList);
    }

    /**
     * 分配区域到组
     */



}