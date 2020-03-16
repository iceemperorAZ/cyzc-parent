package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.TurntableLog;
import com.jingliang.mall.resp.TurntableLogResp;
import com.jingliang.mall.service.TurntableLogService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

/**
 * 转盘日志Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@RestController
@RequestMapping(value = "/front/turntableLog")
@Slf4j
@Api(tags = "转盘日志")
public class TurntableLogController {

    @Value("${session.buyer.key}")
    private String sessionBuyer;
    private final TurntableLogService turntableLogService;

    public TurntableLogController(TurntableLogService turntableLogService) {
        this.turntableLogService = turntableLogService;
    }

    @GetMapping("/find/all/page")
    public Result<MallPage<TurntableLogResp>> pageAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer pageSize, @ApiIgnore HttpSession session) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Page<TurntableLog> turntableLogPage = turntableLogService.pageAll(buyer.getId(), pageRequest);
        MallPage<TurntableLogResp> turntableLogRespMallPage = MallUtils.toMallPage(turntableLogPage, TurntableLogResp.class);
        return Result.buildQueryOk(turntableLogRespMallPage);

    }

}