package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Constant;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.TurntableDetail;
import com.jingliang.mall.req.TurntableReq;
import com.jingliang.mall.resp.TurntableResp;
import com.jingliang.mall.service.TurntableService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 转盘Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@Api(tags = "转盘")
@RestController
@RequestMapping(value = "/front/turntable")
@Slf4j
public class TurntableController {

    @Value("${session.buyer.key}")
    private String sessionBuyer;
    private final TurntableService turntableService;

    public TurntableController(TurntableService turntableService) {
        this.turntableService = turntableService;
    }

    /**
     * 全部转盘信息
     */
    @GetMapping("/all")
    public Result<List<TurntableResp>> all() {
        return Result.buildQueryOk(BeanMapper.mapList(turntableService.findAll(), TurntableResp.class));
    }

    /**
     * 抽奖
     */
    @PostMapping("/extract")
    public Result<TurntableDetail> extract(TurntableReq turntableReq, HttpSession session) {
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        try {
            TurntableDetail turntableDetail = turntableService.extract(turntableReq.getId(), buyer.getId());
            return Result.build(Constant.OK, "抽奖成功", turntableDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.build(Constant.FAIL, e.getMessage());
        }
    }
}