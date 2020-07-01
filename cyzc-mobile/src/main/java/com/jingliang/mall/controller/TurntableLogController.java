package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.MUtils;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.TurntableLog;
import com.jingliang.mall.resp.TurntableLogResp;
import com.jingliang.mall.service.TurntableLogService;
import com.citrsw.annatation.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import com.citrsw.annatation.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;

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
@Api(description = "转盘日志")
public class TurntableLogController {

    @Value("${session.buyer.key}")
    private String sessionBuyer;
    private final TurntableLogService turntableLogService;

    public TurntableLogController(TurntableLogService turntableLogService) {
        this.turntableLogService = turntableLogService;
    }

    /**
     * 查询自己的抽奖记录
     *
     * @param page
     * @param pageSize
     * @param session
     * @return
     */
    @GetMapping("/find/all/page")
    public Result<MallPage<TurntableLogResp>> pageAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer pageSize, @ApiIgnore HttpSession session) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Page<TurntableLog> turntableLogPage = turntableLogService.pageAll(buyer.getId(), pageRequest);
        MallPage<TurntableLogResp> turntableLogRespMallPage = MUtils.toMallPage(turntableLogPage, TurntableLogResp.class);
        return Result.buildQueryOk(turntableLogRespMallPage);

    }

    /**
     * 查询前num条中奖记录
     */
    @GetMapping("/find/prize/{num}")
    public Result<List<TurntableLogResp>> prizeAll(@PathVariable Integer num) {
        PageRequest pageRequest = PageRequest.of(0, num, Sort.by(Sort.Order.desc("createTime")));
        List<TurntableLog> turntableLogPage = turntableLogService.prizeAll(pageRequest);
        List<TurntableLogResp> turntableLogResps = BeanMapper.mapList(turntableLogPage, TurntableLogResp.class);
        return Result.buildQueryOk(turntableLogResps);
    }
}