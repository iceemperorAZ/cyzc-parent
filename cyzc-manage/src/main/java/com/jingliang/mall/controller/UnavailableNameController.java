package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.UnavailableName;
import com.jingliang.mall.req.UnavailableNameReq;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.jingliang.mall.service.UnavailableNameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.citrsw.annatation.Api;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 不规范商铺名称合集Controller
 *
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-06-11 15:45:35
 */
@RequestMapping(value = "/front/unavailableName")
@RestController
@Slf4j
@Api(description = "不规范商铺名称合集")
public class UnavailableNameController {

    private final UnavailableNameService unavailableNameService;

    public UnavailableNameController(UnavailableNameService unavailableNameService) {
        this.unavailableNameService = unavailableNameService;
    }


    @GetMapping("/saveUnableName")
    @ApiOperation(description = "更新并存储不可用名称")
    public Result<?> updatsaveUnableName() {
        return Result.buildSaveOk(unavailableNameService.update());
    }

}