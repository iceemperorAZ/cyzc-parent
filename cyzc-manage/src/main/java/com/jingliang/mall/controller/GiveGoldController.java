package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallBeanMapper;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.GiveGold;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.resp.GiveGoldResp;
import com.jingliang.mall.service.GiveGoldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
@Api(tags = "赠送金币")
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
    @ApiOperation("赠送金币")
    public MallResult<GiveGoldResp> give(@RequestBody Map<String, String> map, @ApiIgnore HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        Long buyerId = Long.parseLong(map.get("buyerId"));
        Integer goldNum = Integer.parseInt(map.get("goldNum"));
        String msg = map.get("msg");
        GiveGold giveGold = giveGoldService.give(user.getId(), buyerId, goldNum, msg);
        return MallResult.buildSaveOk(MallBeanMapper.map(giveGold, GiveGoldResp.class));
    }

    /**
     * 审批赠送金币
     */
    @PostMapping("/approval")
    @ApiOperation("审批赠送金币")
    public MallResult<Boolean> approval(@RequestBody Map<String, String> map, @ApiIgnore HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        Long id = Long.parseLong(map.get("id"));
        Integer approval = Integer.parseInt(map.get("approval"));
        giveGoldService.approval(user.getId(), id, approval);
        return MallResult.buildSaveOk(true);
    }

    /**
     * 分页查询所有赠送金币记录
     */
    @GetMapping("/page/all")
    @ApiOperation("分页查询所有赠送金币记录")
    public MallResult<MallPage<GiveGoldResp>> pageAll(Long buyerId, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer pageSize, @ApiIgnore HttpSession session) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<GiveGold> giveGoldPage = giveGoldService.pageAll(buyerId, pageRequest);
        return MallResult.buildQueryOk(MallUtils.toMallPage(giveGoldPage, GiveGoldResp.class));
    }
}