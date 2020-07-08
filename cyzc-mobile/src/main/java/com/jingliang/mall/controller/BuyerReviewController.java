package com.jingliang.mall.controller;

import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiOperation;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.BuyerReq;
import com.jingliang.mall.resp.BuyerResp;
import com.jingliang.mall.service.BuyerService;
import com.jingliang.mall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lmd
 * @date 2020/6/14
 * @Company 晶粮
 */
@Api(description = "商户审核")
@RequestMapping("/wx/buyer/review")
@RestController("buyerReviewController")
@Slf4j
public class BuyerReviewController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;

    private final BuyerService buyerService;
    private final UserService userService;

    public BuyerReviewController(BuyerService buyerService, UserService userService) {
        this.buyerService = buyerService;
        this.userService = userService;
    }

    /**
     * 商户审核通过
     */
    @PostMapping("/success")
    @ApiOperation(description = "商户审核通过")
    public Result<Boolean> success(@RequestBody BuyerReq buyerReq, HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        if (user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "你的权限不够");
        }
        Buyer buyer = buyerService.findById(buyerReq.getId());
        if (buyerReq.getBuyerStatus() > 100) {
            return Result.build(Msg.FAIL, "商户已经审核");
        }
        //获取当前日期时间，毫秒时间
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date date = Date.from(instant);
        //审核通过，商户状态为300
        buyer.setBuyerStatus(300);
        buyer.setReviewUserId(user.getId());
        buyer.setReviewTime(date);
        buyer.setReviewMsg(buyerReq.getReviewMsg());
        buyerService.save(buyer);
        return Result.build(Msg.OK, "审核已通过");
    }

    /**
     * 商户审核驳回
     */
    @PostMapping("/overrule")
    @ApiOperation(description = "商户审核驳回")
    public Result<Boolean> overrule(@RequestBody BuyerReq buyerReq, HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        if (user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "你的权限不够");
        }
        Buyer buyer = buyerService.findById(buyerReq.getId());
        if (buyerReq.getBuyerStatus() > 100) {
            return Result.build(Msg.FAIL, "商户审核已驳回");
        }
        if (StringUtils.isBlank(buyerReq.getReviewMsg())) {
            return Result.build(Msg.FAIL, "审核意见不能为空");
        }
        //获取当前日期时间，毫秒时间
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date date = Date.from(instant);
        //审核驳回，商户状态为150
        buyer.setBuyerStatus(150);
        buyer.setReviewUserId(user.getId());
        buyer.setReviewTime(date);
        buyer.setReviewMsg(buyerReq.getReviewMsg());
        buyerService.save(buyer);
        return Result.build(Msg.OK, "审核驳回");
    }

    /**
     * 分页查询未审核的商户
     *
     * @param buyerReq
     * @param session
     * @return
     */
    @ApiOperation(description = "分页查询未审核的商户")
    @GetMapping("/page/findAllBuyer")
    public Result<MallPage<BuyerResp>> pageAll(BuyerReq buyerReq, HttpSession session) {
        log.debug("请求参数：{}", buyerReq);
        PageRequest pageRequest = PageRequest.of(buyerReq.getPage(), buyerReq.getPageSize());
        if (StringUtils.isNotBlank(buyerReq.getClause())) {
            pageRequest = PageRequest.of(buyerReq.getPage(), buyerReq.getPageSize(), Sort.by(MUtils.separateOrder(buyerReq.getClause())));
        }
        User user = (User) session.getAttribute(sessionUser);
        Specification<Buyer> buyerSpecification = (Specification<Buyer>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(cb.equal(root.get("saleUserId"), user.getId()));
            predicateList.add(cb.equal(root.get("buyerStatus"), 100));
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            predicateList.add(cb.equal(root.get("isSealUp"), false));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.asc(root.get("createTime")));
            return query.getRestriction();
        };
        Page<Buyer> buyerPage = buyerService.findAll(buyerSpecification, pageRequest);
        MallPage<BuyerResp> buyerCouponRespMallPage = MUtils.toMallPage(buyerPage, BuyerResp.class);
        log.debug("返回结果：{}", buyerCouponRespMallPage);
        return Result.buildQueryOk(buyerCouponRespMallPage);
    }

    /**
     * 查询区域经理下的销售的待审核商户总数
     *
     * @param session
     * @return
     */
    @ApiOperation(description = "查询区域经理下的销售的待审核商户总数")
    @GetMapping("/list/findAllByManageId")
    public Result<List<BuyerResp>> findAllByManageId(HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        if (user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "你的权限不够");
        }
        List<BuyerResp> buyerResps = new ArrayList<>();
        for (Buyer buyer : buyerService.findAllByManageId(user.getId())) {
            BuyerResp buyerResp = BeanMapper.map(buyer, BuyerResp.class);
            buyerResps.add(buyerResp);
        }
        return Result.buildQueryOk(buyerResps);
    }

    /**
     * 查询区域经理下的销售的被驳回的商户
     *
     * @param session
     * @return
     */
    @ApiOperation(description = "查询区域经理下的销售的被驳回的商户")
    @GetMapping("/list/findAllNotReview")
    public Result<List<BuyerResp>> findAllNotReview(HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        if (user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "你的权限不够");
        }
        List<BuyerResp> buyerResps = new ArrayList<>();
        for (Buyer buyer : buyerService.findAllNotReview(user.getId())) {
            BuyerResp buyerResp = BeanMapper.map(buyer, BuyerResp.class);
            buyerResps.add(buyerResp);
        }
        return Result.buildQueryOk(buyerResps);
    }
}
