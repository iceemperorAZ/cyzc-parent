package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.GiveGold;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.resp.GiveGoldResp;
import com.jingliang.mall.service.GiveGoldService;
import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import com.citrsw.annatation.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 赠送金币Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 14:57:03
 */
@RestController
@Slf4j
@Api(description = "赠送金币")
@RequestMapping(value = "/back/giveGold")
public class GiveGoldController {

    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;

    private final GiveGoldService giveGoldService;

    public GiveGoldController(GiveGoldService giveGoldService) {
        this.giveGoldService = giveGoldService;
    }


    /**
     * 赠送金币
     */
    @PostMapping("/save")
    @ApiOperation(description = "赠送金币")
    public Result<GiveGoldResp> give(@RequestBody Map<String, String> map, @ApiIgnore HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        Long buyerId = Long.parseLong(map.get("buyerId"));
        Integer goldNum = Integer.parseInt(map.get("goldNum"));
        String msg = map.get("msg");
        GiveGold giveGold = giveGoldService.give(user.getId(), buyerId, goldNum, msg);
        return Result.buildSaveOk(BeanMapper.map(giveGold, GiveGoldResp.class));
    }

    /**
     * 审批赠送金币
     */
    @PostMapping("/approval")
    @ApiOperation(description = "审批赠送金币")
    public Result<Boolean> approval(@RequestBody Map<String, String> map, @ApiIgnore HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        Long id = Long.parseLong(map.get("id"));
        Integer approval = Integer.parseInt(map.get("approval"));
        giveGoldService.approval(user.getId(), id, approval);
        return Result.buildSaveOk(true);
    }

    /**
     * 分页查询所有赠送金币记录
     */
    @GetMapping("/page/all")
    @ApiOperation(description = "分页查询所有赠送金币记录")
    public Result<MallPage<GiveGoldResp>> pageAll(Long buyerId, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer pageSize, @ApiIgnore HttpSession session) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<GiveGold> giveGoldPage = giveGoldService.pageAll(buyerId, pageRequest);
        return Result.buildQueryOk(MallUtils.toMallPage(giveGoldPage, GiveGoldResp.class));
    }
}