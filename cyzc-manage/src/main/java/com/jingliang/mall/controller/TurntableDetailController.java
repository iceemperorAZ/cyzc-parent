package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.TurntableDetail;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.TurntableDetailReq;
import com.jingliang.mall.resp.TurntableDetailResp;
import com.jingliang.mall.service.TurntableDetailService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 转盘详情Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@Api(tags = "转盘详情")
@RestController
@RequestMapping(value = "/back/turntableDetail")
@Slf4j
public class TurntableDetailController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final TurntableDetailService turntableDetailService;

    public TurntableDetailController(TurntableDetailService turntableDetailService) {
        this.turntableDetailService = turntableDetailService;
    }

    /**
     * 保存转盘项
     */
    @PostMapping("/save")
    public Result<TurntableDetailResp> save(@RequestBody TurntableDetailReq turntableDetailReq, HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(turntableDetailReq, user);
        TurntableDetail turntableDetail = turntableDetailService.save(BeanMapper.map(turntableDetailReq, TurntableDetail.class));
        return Result.buildSaveOk(BeanMapper.map(turntableDetail, TurntableDetailResp.class));
    }

    /**
     * 根据转盘Id查询转盘项
     */
    @GetMapping("/all/turntableId")
    public Result<List<TurntableDetailResp>> save(Long turntableId) {
        List<TurntableDetail> turntableDetails = turntableDetailService.findAll(turntableId);
        return Result.buildSaveOk(BeanMapper.mapList(turntableDetails, TurntableDetailResp.class));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        turntableDetailService.delete(id, user.getId());
        return Result.buildDeleteOk(true);
    }
}