package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.GiveRebate;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.resp.GiveRebateResp;
import com.jingliang.mall.service.GiveRebateService;
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
 * 赠送返利次数Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 14:57:03
 */
@RestController
@Slf4j
@Api(tags = "赠送返利次数")
@RequestMapping(value = "/back/giveRebate")
public class GiveRebateController {

    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;

    private final GiveRebateService giveRebateService;

    public GiveRebateController(GiveRebateService giveRebateService) {
        this.giveRebateService = giveRebateService;
    }


    /**
     * 赠送返利次数
     */
    @PostMapping("/save")
    @ApiOperation("赠送返利次数")
    public Result<GiveRebateResp> give(@RequestBody Map<String, String> map, @ApiIgnore HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        Long buyerId = Long.parseLong(map.get("buyerId"));
        Integer rebateNum = Integer.parseInt(map.get("rebateNum"));
        String msg = map.get("msg");
        GiveRebate giveGold = giveRebateService.give(user.getId(), buyerId, rebateNum, msg);
        return Result.buildSaveOk(BeanMapper.map(giveGold, GiveRebateResp.class));
    }

    /**
     * 审批赠送返利次数
     */
    @PostMapping("/approval")
    @ApiOperation("审批赠送返利次数")
    public Result<Boolean> approval(@RequestBody Map<String, String> map, @ApiIgnore HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        Long id = Long.parseLong(map.get("id"));
        Integer approval = Integer.parseInt(map.get("approval"));
        giveRebateService.approval(user.getId(), id, approval);
        return Result.buildSaveOk(true);
    }

    /**
     * 分页查询所有赠送返利次数记录
     */
    @GetMapping("/page/all")
    @ApiOperation("分页查询所有赠送返利次数记录")
    public Result<MallPage<GiveRebateResp>> pageAll(Long buyerId, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer pageSize, @ApiIgnore HttpSession session) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<GiveRebate> giveGoldPage = giveRebateService.pageAll(buyerId, pageRequest);
        return Result.buildQueryOk(MallUtils.toMallPage(giveGoldPage, GiveRebateResp.class));
    }
}