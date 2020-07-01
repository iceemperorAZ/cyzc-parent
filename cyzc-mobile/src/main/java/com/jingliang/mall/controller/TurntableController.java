package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.BuyerAddress;
import com.jingliang.mall.entity.TurntableDetail;
import com.jingliang.mall.exception.TurntableException;
import com.jingliang.mall.req.TurntableReq;
import com.jingliang.mall.resp.TurntableResp;
import com.jingliang.mall.service.BuyerAddressService;
import com.jingliang.mall.service.TurntableService;
import com.citrsw.annatation.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 转盘Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@Api(description = "转盘")
@RestController
@RequestMapping(value = "/front/turntable")
@Slf4j
public class TurntableController {

    @Value("${session.buyer.key}")
    private String sessionBuyer;
    private final TurntableService turntableService;
    private final BuyerAddressService buyerAddressService;

    public TurntableController(TurntableService turntableService, BuyerAddressService buyerAddressService) {
        this.turntableService = turntableService;
        this.buyerAddressService = buyerAddressService;
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
    public Result<TurntableDetail> extract(@RequestBody TurntableReq turntableReq, HttpSession session) {
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        //查询默认地址
        BuyerAddress buyerAddress = buyerAddressService.findDefaultAddrByBuyerId(buyer.getId());
        if (buyerAddress == null) {
            return Result.build(Msg.OK, "请配置默认地址");
        }
        try {
            TurntableDetail turntableDetail = turntableService.extract(turntableReq.getId(), buyer.getId());
            return Result.build(Msg.OK, "抽奖成功", turntableDetail);
        } catch (TurntableException e) {
            e.printStackTrace();
            return Result.build(Msg.OK, e.getMessage());
        }
    }
}