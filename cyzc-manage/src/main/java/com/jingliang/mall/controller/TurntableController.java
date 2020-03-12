package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallBeanMapper;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.Turntable;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.TurntableReq;
import com.jingliang.mall.resp.TurntableResp;
import com.jingliang.mall.service.TurntableService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 转盘Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 11:40:28
 */
@Api(tags = "转盘")
@RestController
@RequestMapping(value = "/back/turntable")
@Slf4j
public class TurntableController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;

    private final TurntableService turntableService;

    public TurntableController(TurntableService turntableService) {
        this.turntableService = turntableService;
    }


    @PostMapping("/save")
    public MallResult<TurntableResp> save(@RequestBody TurntableReq turntableReq, HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(turntableReq, user);
        Turntable turntable = turntableService.save(MallBeanMapper.map(turntableReq, Turntable.class));
        return MallResult.buildSaveOk(MallBeanMapper.map(turntable, TurntableResp.class));
    }
}