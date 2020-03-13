package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Techarge;
import com.jingliang.mall.resp.TechargeResp;
import com.jingliang.mall.service.TechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 充值配置Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 10:14:25
 */
@RestController
@Slf4j
@RequestMapping(value = "/front/techarge")
@Api(tags = "充值配置")
public class TechargeController {

    private final TechargeService techargeService;

    public TechargeController(TechargeService techargeService) {
        this.techargeService = techargeService;
    }


    /**
     * 查询全部
     */
    @GetMapping("/all")
    @ApiOperation("查询全部")
    public Result<List<TechargeResp>> findAll() {
        List<Techarge> techarges = techargeService.findAllShow();
        return Result.buildQueryOk(BeanMapper.mapList(techarges, TechargeResp.class));
    }
}