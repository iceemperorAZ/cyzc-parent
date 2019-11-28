package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallBeanMapper;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.req.OrderReq;
import com.jingliang.mall.resp.OrderResp;
import com.jingliang.mall.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 订单表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
@RequestMapping("/back/order")
@Api(tags = "订单")
@Slf4j
@RestController("backOrderController")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 发货
     */
    @ApiOperation(value = "发货")
    @PostMapping("/deliver")
    public MallResult<OrderResp> deliver(@RequestBody OrderReq orderReq) {
        log.debug("请求参数：{}", orderReq);
        if (Objects.isNull(orderReq.getId()) || StringUtils.isBlank(orderReq.getDeliveryName()) || StringUtils.isBlank(orderReq.getDeliveryPhone())) {
            return MallResult.buildParamFail();
        }
        Order order = new Order();
        order.setId(orderReq.getId());
        order.setDeliveryName(orderReq.getDeliveryName());
        order.setOrderStatus(400);
        order.setDeliveryPhone(orderReq.getDeliveryPhone());
        order.setUpdateTime(new Date());
        order = orderService.update(order);
        OrderResp orderResp = MallBeanMapper.map(order, OrderResp.class);
        log.debug("返回结果：{}", orderResp);
        return MallResult.buildUpdateOk(orderResp);
    }

    /**
     * 退货(不扣绩效)
     */
    @ApiOperation(value = "退货(不扣绩效)")
    @PostMapping("/refunds")
    public MallResult<OrderResp> refunds(@RequestBody OrderReq orderReq) {
        log.debug("请求参数：{}", orderReq);
        if (Objects.isNull(orderReq.getId())) {
            return MallResult.buildParamFail();
        }
        Date date = new Date();
        Order order = new Order();
        order.setId(orderReq.getId());
        order.setDeliveryName(orderReq.getDeliveryName());
        order.setOrderStatus(700);
        order.setDeliveryPhone(orderReq.getDeliveryPhone());
        order.setFinishTime(date);
        order.setUpdateTime(date);
        order = orderService.update(order);
        OrderResp orderResp = MallBeanMapper.map(order, OrderResp.class);
        log.debug("返回结果：{}", orderResp);
        return MallResult.buildUpdateOk(orderResp);
    }

    /**
     * 退货(扣绩效)
     */
    @ApiOperation(value = "退货(扣绩效)")
    @PostMapping("/refunds/money")
    public MallResult<OrderResp> refundsMoney(@RequestBody OrderReq orderReq) {
        log.debug("请求参数：{}", orderReq);
        if (Objects.isNull(orderReq.getId())) {
            return MallResult.buildParamFail();
        }
        Date date = new Date();
        Order order = new Order();
        order.setId(orderReq.getId());
        order.setDeliveryName(orderReq.getDeliveryName());
        order.setOrderStatus(800);
        order.setDeliveryPhone(orderReq.getDeliveryPhone());
        order.setFinishTime(date);
        order.setUpdateTime(date);
        order = orderService.update(order);
        OrderResp orderResp = MallBeanMapper.map(order, OrderResp.class);
        log.debug("返回结果：{}", orderResp);
        return MallResult.buildUpdateOk(orderResp);
    }

    /**
     * 分页查询全部用户订单信息
     */
    @ApiOperation(value = "分页查询全部用户订单信息")
    @GetMapping("/page/all")
    public MallResult<MallPage<OrderResp>> pageAll(OrderReq orderReq) {
        log.debug("请求参数：{}", orderReq);
        PageRequest pageRequest = PageRequest.of(orderReq.getPage(), orderReq.getPageSize());
        if (StringUtils.isNotBlank(orderReq.getClause())) {
            pageRequest = PageRequest.of(orderReq.getPage(), orderReq.getPageSize(), Sort.by(MallUtils.separateOrder(orderReq.getClause())));
        }
        Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (Objects.nonNull(orderReq.getId())) {
                predicateList.add(cb.equal(root.get("id"), orderReq.getId()));
            }
            if (StringUtils.isNotBlank(orderReq.getOrderNo())) {
                predicateList.add(cb.equal(root.get("orderNo"), orderReq.getOrderNo()));
            }
            if (Objects.nonNull(orderReq.getBuyerId())) {
                predicateList.add(cb.equal(root.get("buyerId"), orderReq.getBuyerId()));
            }
            if (Objects.nonNull(orderReq.getCreateTimeStart()) && Objects.nonNull(orderReq.getCreateTimeEnd())) {
                predicateList.add(cb.between(root.get("createTime"), orderReq.getCreateTimeStart(), orderReq.getCreateTimeEnd()));
            }
            if (Objects.nonNull(orderReq.getOrderStatus())) {
                predicateList.add(cb.equal(root.get("orderStatus"), orderReq.getOrderStatus()));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("createTime")));
            return query.getRestriction();
        };
        Page<Order> orderPage = orderService.findAll(orderSpecification, pageRequest);
        MallPage<OrderResp> orderRespMallPage = MallUtils.toMallPage(orderPage, OrderResp.class);
        log.debug("返回结果：{}", orderRespMallPage);
        return MallResult.buildQueryOk(orderRespMallPage);
    }
}