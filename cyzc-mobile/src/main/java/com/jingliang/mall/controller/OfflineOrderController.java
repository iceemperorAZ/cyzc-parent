package com.jingliang.mall.controller;

import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.OfflineOrder;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.OfflineOrderReq;
import com.jingliang.mall.resp.OfflineOrderResp;
import com.jingliang.mall.service.OfflineOrderService;
import com.jingliang.mall.service.UserService;
import com.citrsw.annatation.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import com.citrsw.annatation.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 线下订单Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-25 10:05:44
 */
@RestController
@Slf4j
@Api(description = "线下订单")
public class OfflineOrderController {

    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final OfflineOrderService offlineOrderService;
    private final UserService userService;

    public OfflineOrderController(OfflineOrderService offlineOrderService, UserService userService) {
        this.offlineOrderService = offlineOrderService;
        this.userService = userService;
    }

    /**
     * 保存
     */
    @PostMapping("/back/offlineOrder/save")
    public Result<OfflineOrderResp> save(@RequestBody OfflineOrderReq offlineOrderReq, HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        offlineOrderReq.setSalesmanName(user.getUserName());
        offlineOrderReq.setSalesmanId(user.getId());
        offlineOrderReq.setSalesmanNo(user.getUserNo());
        if (offlineOrderReq.getRate() != null && offlineOrderReq.getRate() != 200) {
            offlineOrderReq.setRate(null);
        }
        offlineOrderReq.setEnable(false);
        offlineOrderReq.setIsAvailable(true);
        offlineOrderReq.setSalesmanPhone(user.getPhone());
        offlineOrderReq.setCreateTime(new Date());
        OfflineOrder offlineOrder = BeanMapper.map(offlineOrderReq, OfflineOrder.class);
        String region = user.getRegion();
        if ((StringUtils.isBlank(region) || "-".equals(region)) && user.getManagerId() != null) {
            //查询大区
            User manage = userService.findById(user.getManagerId());
            if (manage != null) {
                assert offlineOrder != null;
                offlineOrder.setRegion(manage.getRegion());
            }
        } else {
            assert offlineOrder != null;
            offlineOrder.setRegion(region);
        }
        //状态为待发货
        offlineOrder.setOrderStatus(300);
        offlineOrder = offlineOrderService.save(offlineOrder);
        OfflineOrderResp offlineOrderResp = BeanMapper.map(offlineOrder, OfflineOrderResp.class);
        return Result.build(Msg.OK, offlineOrderReq.getId() == null ? "保存成功" : "修改成功", offlineOrderResp);
    }

    /**
     * 删除
     */
    @PostMapping("/back/offlineOrder/delete")
    public Result<OfflineOrderResp> delete(@RequestBody OfflineOrderReq offlineOrderReq, HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        OfflineOrder offlineOrder = offlineOrderService.delete(user.getId(), offlineOrderReq.getId());
        OfflineOrderResp offlineOrderResp = BeanMapper.map(offlineOrder, OfflineOrderResp.class);
        return Result.build(Msg.OK, "删除成功", offlineOrderResp);
    }

    /**
     * 分页查询全部
     */
    @GetMapping("/back/offlineOrder/page/all")
    public Result<MallPage<OfflineOrderResp>> pageAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer pageSize,
                                                      Long createTimeStart,
                                                      Long createTimeEnd,
                                                      Integer rate, @ApiIgnore HttpSession session) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        User user = (User) session.getAttribute(sessionUser);
        Specification<OfflineOrder> specification = (Specification<OfflineOrder>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (createTimeStart != null && createTimeEnd != null) {
                predicateList.add(cb.between(root.get("createTime"), new Date(createTimeStart), new Date(createTimeEnd)));
            }
            if (rate != null) {
                predicateList.add(cb.equal(root.get("rate"), rate));
            }
            predicateList.add(cb.equal(root.get("salesmanId"), user.getId()));
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("createTime")));
            return query.getRestriction();
        };
        Page<OfflineOrder> offlineOrderPage = offlineOrderService.pageAll(specification, pageRequest);
        return Result.buildQueryOk(MallUtils.toMallPage(offlineOrderPage, OfflineOrderResp.class));
    }
}