package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallBeanMapper;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.Techarge;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.TechargeReq;
import com.jingliang.mall.resp.TechargeResp;
import com.jingliang.mall.service.TechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
@RequestMapping(value = "/back/techarge")
@Api(tags = "充值配置")
public class TechargeController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final TechargeService techargeService;

    public TechargeController(TechargeService techargeService) {
        this.techargeService = techargeService;
    }

    /**
     * 保存/更新配置
     */
    @PostMapping("/save")
    @ApiOperation("保存/更新配置")
    public MallResult<TechargeResp> save(@RequestBody TechargeReq techargeReq, HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(techargeReq, user);
        Techarge techarge = techargeService.save(MallBeanMapper.map(techargeReq, Techarge.class));
        return MallResult.buildSaveOk(MallBeanMapper.map(techarge, TechargeResp.class));
    }

    /**
     * 上架
     */
    @PostMapping("/show")
    @ApiOperation("上架")
    public MallResult<TechargeResp> show(@RequestBody TechargeReq techargeReq, HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        Techarge techarge = techargeService.show(user.getId(), MallBeanMapper.map(techargeReq, Techarge.class));
        return MallResult.buildSaveOk(MallBeanMapper.map(techarge, TechargeResp.class));
    }

    /**
     * 下架
     */
    @PostMapping("/hide")
    @ApiOperation("下架")
    public MallResult<TechargeResp> hide(@RequestBody TechargeReq techargeReq, HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        Techarge techarge = techargeService.hide(user.getId(), MallBeanMapper.map(techargeReq, Techarge.class));
        return MallResult.buildSaveOk(MallBeanMapper.map(techarge, TechargeResp.class));
    }

    /**
     *  删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public MallResult<TechargeResp> delete(@RequestBody TechargeReq techargeReq, HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        Techarge techarge = techargeService.delete(user.getId(), MallBeanMapper.map(techargeReq, Techarge.class));
        return MallResult.buildSaveOk(MallBeanMapper.map(techarge, TechargeResp.class));
    }

    /**
     * 查询全部
     */
    @GetMapping("/all")
    @ApiOperation("查询全部")
    public MallResult<List<TechargeResp>> findAll() {
        List<Techarge> techarges = techargeService.findAll();
        return MallResult.buildQueryOk(MallBeanMapper.mapList(techarges, TechargeResp.class));
    }
}