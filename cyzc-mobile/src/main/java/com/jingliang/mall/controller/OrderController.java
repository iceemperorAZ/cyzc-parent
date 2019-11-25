package com.jingliang.mall.controller;

import com.jingliang.mall.amqp.producer.RabbitProducer;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.BuyerCoupon;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.entity.Product;
import com.jingliang.mall.req.OrderDetailReq;
import com.jingliang.mall.req.OrderReq;
import com.jingliang.mall.resp.OrderResp;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.BuyerCouponService;
import com.jingliang.mall.service.OrderService;
import com.jingliang.mall.service.ProductService;
import com.jingliang.mall.wx.service.WechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 订单表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
@RequestMapping("/front/order")
@Api(tags = "订单")
@RestController
@Slf4j
public class OrderController {
    @Value("${session.buyer.key}")
    private String sessionBuyer;
    @Value("${free_shipping_quota}")
    private Double freeShippingQuota;
    @Value("${deliver_fee}")
    private Double deliverFee;
    private final OrderService orderService;
    private final ProductService productService;
    private final BuyerCouponService buyerCouponService;
    private final RedisService redisService;
    private final WechatService wechatService;
    private final RabbitProducer rabbitProducer;

    public OrderController(OrderService orderService, ProductService productService, BuyerCouponService buyerCouponService, RedisService redisService, WechatService wechatService, RabbitProducer rabbitProducer) {
        this.orderService = orderService;
        this.productService = productService;
        this.buyerCouponService = buyerCouponService;
        this.redisService = redisService;
        this.wechatService = wechatService;
        this.rabbitProducer = rabbitProducer;
    }


    /**
     * 创建订单
     */
    @ApiOperation(value = "创建订单")
    @PostMapping("/save")
    public MallResult<Map<String, String>> save(@RequestBody OrderReq orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq);
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        orderReq.setTotalPrice(0D);
        orderReq.setPayableFee(0D);
        orderReq.setDeliverFee(0D);
        int productNum = 0;
        List<OrderDetailReq> orderDetailReqs = new ArrayList<>();
        //计算商品总价
        for (OrderDetailReq orderDetailReq : orderReq.getOrderDetails()) {
            orderDetailReq.setId(null);
            //查询上架商品
            Product product = productService.findShowProductById(orderDetailReq.getProductId());
            if (Objects.isNull(product)) {
                for (OrderDetailReq detailReq : orderDetailReqs) {
                    //如果本次有已经下架的商品就把减掉的库存加回去，并返回库存商品已下架
                    redisService.skuLineIncrement(String.valueOf(detailReq.getProductId()), detailReq.getProductNum());
                }
                return MallResult.build(MallConstant.ORDER_FAIL, MallConstant.TEXT_ORDER_PRODUCT_FAIL);
            }
            //售价[商品价格*数量]
            double sellingPrice = product.getSellingPrice() * orderDetailReq.getProductNum();
            orderReq.setTotalPrice(MatchUtils.add(orderReq.getTotalPrice(), sellingPrice));
            orderReq.setPayableFee(MatchUtils.add(orderReq.getPayableFee(), sellingPrice));
            //查询线上库存
            Long skuNum = redisService.skuLineDecrement(String.valueOf(product.getId()), orderDetailReq.getProductNum());
            orderDetailReq.setSellingPrice(product.getSellingPrice());
            MallUtils.addDateAndBuyer(orderDetailReq, buyer);
            orderDetailReqs.add(orderDetailReq);
            if (skuNum < 0) {
                for (OrderDetailReq detailReq : orderDetailReqs) {
                    //如果小于库存就把减掉的库存加回去，并返回库存不足的信息
                    redisService.skuLineIncrement(String.valueOf(detailReq.getProductId()), detailReq.getProductNum());
                }
                return MallResult.build(MallConstant.ORDER_FAIL, MallConstant.TEXT_ORDER_SKU_FAIL);
            }
            productNum += orderDetailReq.getProductNum();
        }
        orderReq.setProductNum(productNum);
        orderReq.setPreferentialFee(0D);
        //计算使用优惠券后的支付价
        if (Objects.nonNull(orderReq.getCouponId())) {
            BuyerCoupon buyerCoupon = buyerCouponService.findByIdAndBuyerId(orderReq.getCouponId(), buyer.getId());
            if (Objects.isNull(buyerCoupon)) {
                return MallResult.build(MallConstant.ORDER_FAIL, MallConstant.TEXT_ORDER_COUPON_FAIL);
            }
            orderReq.setPayableFee(MatchUtils.sub(orderReq.getPayableFee(), buyerCoupon.getMoney()));
            orderReq.setPreferentialFee(MatchUtils.add(orderReq.getPreferentialFee(), buyerCoupon.getMoney()));
            buyerCoupon.setIsUsed(true);
            //优惠券标记为已使用
            buyerCouponService.save(buyerCoupon);
        }
        //计算运费
        if (orderReq.getPayableFee() > freeShippingQuota) {
            orderReq.setDeliverFee(0D);
        } else {
            orderReq.setDeliverFee(deliverFee);
            orderReq.setPayableFee(MatchUtils.add(orderReq.getPayableFee(), deliverFee));
        }
        //生成订单号
        orderReq.setOrderNo(redisService.getOrderNo());
        orderReq.setOrderStatus(100);
        orderReq.setPayStartTime(new Date());
        //微信支付
        orderReq.setPayWay(100);
        MallUtils.addDateAndBuyer(orderReq, buyer);
        OrderResp orderResp;
        Order order = MallBeanMapper.map(orderReq, Order.class);
        //调起微信预支付
        Map<String, String> resultMap = wechatService.payUnifiedOrder(order, buyer.getUniqueId());
        order = orderService.save(order);
        rabbitProducer.sendOrderExpireMsg(order);
        //订单Id也返回
        resultMap.put("id", order.getId() + "");
        resultMap.put("orderNo", order.getOrderNo());
        orderResp = MallBeanMapper.map(order, OrderResp.class);
        log.debug("微信返回结果二次签名后的返回结果：{}", resultMap);
        log.debug("返回结果：{}", orderResp);
        return MallResult.buildSaveOk(resultMap);
    }

    /**
     * 取消订单
     */
    @ApiOperation(value = "取消订单")
    @PostMapping("/cancel")
    public MallResult<OrderResp> cancel(@RequestBody OrderReq orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq);
        if (Objects.isNull(orderReq.getId())) {
            return MallResult.buildParamFail();
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Order order = orderService.findByIdAndBuyerId(orderReq.getId(), buyer.getId());
        if (Objects.isNull(order)) {
            return MallResult.build(MallConstant.ORDER_FAIL, MallConstant.TEXT_ORDER_NOT_EXIST_FAIL);
        }
        order.setFinishTime(new Date());
        order.setOrderStatus(200);
        OrderResp orderResp = MallBeanMapper.map(orderService.update(order), OrderResp.class);
        log.debug("返回结果：{}", orderResp);
        return MallResult.build(MallConstant.OK, MallConstant.TEXT_CANCEL_OK, orderResp);
    }

    /**
     * 订单完成确认
     */
    @ApiOperation(value = "订单完成确认")
    @PostMapping("/confirm")
    public MallResult<OrderResp> confirm(@RequestBody OrderReq orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq);
        if (Objects.isNull(orderReq.getId())) {
            return MallResult.buildParamFail();
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Order order = orderService.findByIdAndBuyerId(orderReq.getId(), buyer.getId());
        if (Objects.isNull(order)) {
            return MallResult.build(MallConstant.ORDER_FAIL, MallConstant.TEXT_ORDER_NOT_EXIST_FAIL);
        }
        order.setFinishTime(new Date());
        order.setOrderStatus(600);
        OrderResp orderResp = MallBeanMapper.map(orderService.update(order), OrderResp.class);
        log.debug("返回结果：{}", orderResp);
        return MallResult.build(MallConstant.OK, MallConstant.TEXT_CONFIRM_OK, orderResp);
    }

    /**
     * 分页查询全部用户订单信息
     */
    @ApiOperation(value = "分页查询全部用户订单信息")
    @GetMapping("/page/all")
    public MallResult<MallPage<OrderResp>> pageAll(OrderReq orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq);
        PageRequest pageRequest = PageRequest.of(orderReq.getPage(), orderReq.getPageSize());
        if (StringUtils.isNotBlank(orderReq.getClause())) {
            pageRequest = PageRequest.of(orderReq.getPage(), orderReq.getPageSize(), Sort.by(MallUtils.separateOrder(orderReq.getClause())));
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (Objects.nonNull(orderReq.getOrderStatus())) {
                if (Objects.equals(orderReq.getOrderStatus(), 400)) {
                    //between前后都包含
                    predicateList.add(cb.between(root.get("orderStatus"), 300, 500));
                } else {
                    predicateList.add(cb.equal(root.get("orderStatus"), orderReq.getOrderStatus()));
                }
            }
            predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
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