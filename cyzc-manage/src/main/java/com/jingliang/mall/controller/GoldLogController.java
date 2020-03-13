package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.GoldLog;
import com.jingliang.mall.resp.GoldLogResp;
import com.jingliang.mall.service.GoldLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 签到日志Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-03 13:04:43
 */
@RestController
@Slf4j
@Api(tags = "签到日志")
@RequestMapping(value = "/back/gold")
public class GoldLogController {

    /**
     * session用户Key
     */
    @Value("${session.buyer.key}")
    private String sessionBuyer;
    private final GoldLogService signInLogService;

    public GoldLogController(GoldLogService signInLogService) {
        this.signInLogService = signInLogService;
    }

    /**
     * 查询签到领取金币的记录
     */
    @GetMapping("/page/all")
    @ApiOperation("查询签到领取金币的记录")
    public Result<MallPage<GoldLogResp>> signIn(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer pageSize, Integer type, @ApiIgnore HttpSession session) {
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Specification<GoldLog> specification = (Specification<GoldLog>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (type != null) {
                predicateList.add(cb.equal(root.get("type"), type));
            }
            predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
            predicateList.add(cb.equal(root.get("isAvailable"),true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("createTime")));
            return query.getRestriction();
        };
        Page<GoldLog> signIn = signInLogService.findAll(specification, pageRequest);
        return Result.buildQueryOk(MallUtils.toMallPage(signIn, GoldLogResp.class));
    }
}