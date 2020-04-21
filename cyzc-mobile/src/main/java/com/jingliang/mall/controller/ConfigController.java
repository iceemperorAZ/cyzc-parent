package com.jingliang.mall.controller;

import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Config;
import com.jingliang.mall.service.ConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 写点注释
 *
 * @author Zhenfeng Li
 * @date 2020-02-26 17:11:53
 */
@RestController
public class ConfigController {
    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping("/config/minimum")
    public Result<Config> findMinimum(){
        //是否满足可以下单的订单额度
        Config config = configService.findByCode("300");
        return Result.buildQueryOk(config);
    }
}
