package com.jingliang.mall.controller;

import com.alibaba.fastjson.JSON;
import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.OfflineOrder;
import com.jingliang.mall.entity.OfflineOrderReturn;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.resp.OfflineOrderReturnResp;
import com.jingliang.mall.service.OfflineOrderReturnService;
import com.jingliang.mall.service.OfflineOrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 退货表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-20 17:57:20
 */
@RestController
@Api(tags = "退货表")
@Slf4j
public class OfflineOrderReturnController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;

    private final OfflineOrderReturnService offlineOrderReturnService;
    private final OfflineOrderService offlineOrderService;

    public OfflineOrderReturnController(OfflineOrderReturnService offlineOrderReturnService, OfflineOrderService offlineOrderService) {
        this.offlineOrderReturnService = offlineOrderReturnService;
        this.offlineOrderService = offlineOrderService;
    }

    /**
     * 提交退货申请
     */
    @PostMapping("/offlineOrderReturn")
    public Result<Boolean> save(@RequestBody Map<String, Object> map) {
        OfflineOrderReturn origina = JSON.parseObject(String.valueOf(map.get("original")), OfflineOrderReturn.class);
        OfflineOrderReturn now = JSON.parseObject(String.valueOf(map.get("now")), OfflineOrderReturn.class);
        Integer orderStatus = (Integer) map.get("orderStatus");
        OfflineOrder offlineOrder = offlineOrderService.findById(origina.getOrderId());
        if (StringUtils.isBlank(offlineOrder.getProductName())) {
            return Result.build(Msg.FAIL, "商品已全部退完", false);
        }
        Boolean result = offlineOrderReturnService.returnApproval(origina, now, orderStatus);
        return Result.build(result ? Msg.OK : Msg.FAIL, result ? "退货申请已提交" : "提交失败", result);
    }

    /**
     * 退货申请通过
     */
    @PostMapping("/offlineOrderReturn/returnGoodsOpinion/success")
    public Result<Boolean> success(@RequestBody OfflineOrderReturn offlineOrderReturn, HttpSession session) {
        if (StringUtils.isBlank(offlineOrderReturn.getReturnGoodsOpinion())) {
            return Result.build(Msg.FAIL, "审核意见不能为空", false);
        }
        User user = (User) session.getAttribute(sessionUser);
        offlineOrderReturn.setOrderStatus(200);
        offlineOrderReturn.setReturnGoodsId(user.getId());
        offlineOrderReturnService.save(offlineOrderReturn);
        return Result.build(Msg.OK, "审核通过", true);
    }

    /**
     * 退货申请驳回
     */
    @PostMapping("/offlineOrderReturn/returnGoodsOpinion/overrule")
    public Result<Boolean> overrule(@RequestBody OfflineOrderReturn offlineOrderReturn, HttpSession session) {
        if (StringUtils.isBlank(offlineOrderReturn.getReturnGoodsOpinion())) {
            return Result.build(Msg.FAIL, "审核意见不能为空", false);
        }
        User user = (User) session.getAttribute(sessionUser);
        offlineOrderReturn.setOrderStatus(201);
        offlineOrderReturn.setReturnGoodsId(user.getId());
        offlineOrderReturnService.save(offlineOrderReturn);
        return Result.build(Msg.OK, "审核驳回", true);
    }

    /**
     * 退款申请通过
     */
    @PostMapping("/offlineOrderReturn/refundOpinion/success")
    public Result<Boolean> refundOpinionSuccess(@RequestBody OfflineOrderReturn offlineOrderReturn, HttpSession session) {
        if (StringUtils.isBlank(offlineOrderReturn.getRefundOpinion())) {
            return Result.build(Msg.FAIL, "审核意见不能为空", false);
        }
        User user = (User) session.getAttribute(sessionUser);
        offlineOrderReturn.setOrderStatus(300);
        offlineOrderReturn.setReturnGoodsId(user.getId());
        offlineOrderReturnService.save(offlineOrderReturn);
        return Result.build(Msg.OK, "审核通过", true);
    }

    /**
     * 退款申请驳回
     */
    @PostMapping("/offlineOrderReturn/refundOpinion/overrule")
    public Result<Boolean> refundOpinionOverrule(@RequestBody OfflineOrderReturn offlineOrderReturn, HttpSession session) {
        if (StringUtils.isBlank(offlineOrderReturn.getRefundOpinion())) {
            return Result.build(Msg.FAIL, "审核意见不能为空", false);
        }
        User user = (User) session.getAttribute(sessionUser);
        offlineOrderReturn.setOrderStatus(301);
        offlineOrderReturn.setReturnGoodsId(user.getId());
        offlineOrderReturnService.save(offlineOrderReturn);
        return Result.build(Msg.OK, "审核驳回", true);
    }

    /**
     * 退货商品列表
     */
    @GetMapping("/offlineOrderReturn/{orderId}")
    public Result<List<OfflineOrderReturnResp>> refundOpinionOverrule(@PathVariable Long orderId, List<Integer> orderStatuses) {
        Specification<OfflineOrderReturn> specification = (Specification<OfflineOrderReturn>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            List<Predicate> orOredicateList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(orderStatuses)) {
                for (Integer orderStatus : orderStatuses) {
                    orOredicateList.add(cb.equal(root.get("orderStatus"), orderStatus));
                }
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            predicateList.add(cb.equal(root.get("orderId"), orderId));
            if (!CollectionUtils.isEmpty(orderStatuses)) {
                query.where(cb.and(predicateList.toArray(new Predicate[0])), cb.or(orOredicateList.toArray(new Predicate[0])));
            } else {
                query.where(cb.and(predicateList.toArray(new Predicate[0])));
            }
            query.orderBy(cb.desc(root.get("createTime")));
            return query.getRestriction();
        };
        List<OfflineOrderReturn> offlineOrderReturns = offlineOrderReturnService.findAll(specification);
        return Result.buildQueryOk(BeanMapper.mapList(offlineOrderReturns, OfflineOrderReturnResp.class));
    }
}