package com.jingliang.mall.controller;

import com.jingliang.mall.amqp.producer.RabbitProducer;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.*;
import com.jingliang.mall.req.OrderReq;
import com.jingliang.mall.resp.OrderResp;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.BuyerCouponService;
import com.jingliang.mall.service.ConfigService;
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
    @Value("${product.sku.init.invented.num}")
    private Integer productSkuInitInventedNum;

    private final OrderService orderService;
    private final ProductService productService;
    private final BuyerCouponService buyerCouponService;
    private final RedisService redisService;
    private final WechatService wechatService;
    private final RabbitProducer rabbitProducer;
    private final ConfigService configService;

    public OrderController(OrderService orderService, ProductService productService, BuyerCouponService buyerCouponService, RedisService redisService, WechatService wechatService, RabbitProducer rabbitProducer, ConfigService configService) {
        this.orderService = orderService;
        this.productService = productService;
        this.buyerCouponService = buyerCouponService;
        this.redisService = redisService;
        this.wechatService = wechatService;
        this.rabbitProducer = rabbitProducer;
        this.configService = configService;
    }


    /**
     * 创建订单
     */
    @ApiOperation(value = "创建订单")
    @PostMapping("/save")
    public MallResult<Map<String, String>> save(@RequestBody OrderReq orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq);
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        MallUtils.addDateAndBuyer(orderReq, buyer);
        Order order = MallBeanMapper.map(orderReq, Order.class);
        assert order != null;
        order.setTotalPrice(0L);
        order.setPayableFee(0L);
        order.setDeliverFee(0L);
        int productNum = 0;
        List<OrderDetail> orderDetails = new ArrayList<>();
        Date date = new Date();
        //是否有真实库存
        boolean hasSku = true;
        //计算商品总价
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            orderDetail.setId(null);
            //查询上架商品
            Product product = productService.findShowProductById(orderDetail.getProductId());
            if (Objects.isNull(product)) {
                for (OrderDetail detailReq : orderDetails) {
                    //如果本次有已经下架的商品就把减掉的库存加回去，并返回库存商品已下架
                    redisService.skuLineIncrement(String.valueOf(detailReq.getProductId()), detailReq.getProductNum());
                }
                return MallResult.build(MallConstant.ORDER_FAIL, MallConstant.TEXT_ORDER_PRODUCT_FAIL);
            }
            //售价[商品价格*数量]
            long sellingPrice = product.getSellingPrice() * orderDetail.getProductNum();
            order.setTotalPrice(order.getTotalPrice() + sellingPrice);
            order.setPayableFee(order.getPayableFee() + sellingPrice);
            //查询线上库存
            Long skuNum = redisService.skuLineDecrement(String.valueOf(product.getId()), orderDetail.getProductNum());
            orderDetail.setSellingPrice(product.getSellingPrice());
            orderDetail.setCreateTime(date);
            orderDetail.setIsAvailable(true);
            orderDetails.add(orderDetail);
            if (skuNum < productSkuInitInventedNum) {
                hasSku = false;
            }
            if (skuNum < 0) {
                for (OrderDetail detail : orderDetails) {
                    //如果小于库存就把减掉的库存加回去，并返回库存不足的信息
                    redisService.skuLineIncrement(String.valueOf(detail.getProductId()), detail.getProductNum());
                }
                return MallResult.build(MallConstant.ORDER_FAIL, MallConstant.TEXT_ORDER_SKU_FAIL);
            }
            productNum += orderDetail.getProductNum();
        }
        order.setProductNum(productNum);
        order.setPreferentialFee(0L);
        //计算使用优惠券后的支付价
        if (Objects.nonNull(order.getCouponId())) {
            BuyerCoupon buyerCoupon = buyerCouponService.findByIdAndBuyerId(order.getCouponId(), buyer.getId());
            if (Objects.isNull(buyerCoupon) || buyerCoupon.getIsUsed()) {
                return MallResult.build(MallConstant.ORDER_FAIL, MallConstant.TEXT_ORDER_COUPON_FAIL);
            }
            order.setPayableFee(order.getPayableFee() - buyerCoupon.getMoney());
            order.setPreferentialFee(order.getPreferentialFee() + buyerCoupon.getMoney());
            buyerCoupon.setIsUsed(true);
            //优惠券标记为已使用
            buyerCouponService.save(buyerCoupon);
        }
        //是否满足可以下单的订单额度
        Config config = configService.findByCode("300");
        if (order.getPayableFee() < (long) (Double.parseDouble(config.getConfigValues()) * 100)) {
            return MallResult.build(MallConstant.ORDER_FAIL, MallConstant.TEXT_ORDER_INSUFFICIENT_AMOUNT_FAIL);
        }
        //计算运费
        config = configService.findByCode("100");
        if (order.getPayableFee() > (long) (Double.parseDouble(config.getConfigValues()) * 100)) {
            order.setDeliverFee(0L);
        } else {
            config = configService.findByCode("200");
            order.setDeliverFee((long) (Double.parseDouble(config.getConfigValues()) * 100));
            order.setPayableFee((order.getPayableFee() + (long) (Double.parseDouble(config.getConfigValues()) * 100)));
        }
        //生成订单号
        order.setOrderNo(redisService.getOrderNo());
        order.setOrderStatus(100);
        order.setPayStartTime(date);
        order.setUpdateTime(date);
        //微信支付
        order.setPayWay(100);
        //调起微信预支付
        Map<String, String> resultMap = wechatService.payUnifiedOrder(order, buyer.getUniqueId());
        if (Objects.isNull(resultMap)) {
            return MallResult.build(MallConstant.ORDER_FAIL, MallConstant.TEXT_ORDER_FAIL);
        }
        //订单预计送达时间
        //1.真实库存有值，则送达时间T+1
        //2.真实库存无值，则送达时间从配置表获取
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        //捕获异常，防止填写错误，填写格式错误则默认延时3天
        if (hasSku) {
            //真实库存不足延迟配送
            config = configService.findByCode("500");
        } else {
            //真实库存不足延迟配送
            config = configService.findByCode("400");
        }
        try {
            instance.add(Calendar.DAY_OF_MONTH, Integer.parseInt(config.getConfigValues()));
        } catch (Exception e) {
            //捕获异常，防止填写错误，填写格式错误则默认延时3天
            instance.add(Calendar.DAY_OF_MONTH, 3);
        }
        order.setExpectedDeliveryTime(instance.getTime());


        order = orderService.save(order);
        rabbitProducer.sendOrderExpireMsg(order);
        //订单Id也返回
        resultMap.put("id", order.getId() + "");
        resultMap.put("orderNo", order.getOrderNo());
        OrderResp orderResp = MallBeanMapper.map(order, OrderResp.class);
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

    /**
     * 根据订单总价查询运费金额
     */
    @ApiOperation(value = "根据订单总价查询运费金额")
    @GetMapping("/find/deliverFee")
    public MallResult<Double> getDeliverFee(Double deliverFee) {
        Config config = configService.findByCode("100");
        if ((long) (deliverFee * 100) > (long) (Double.parseDouble(config.getConfigValues()) * 100)) {
            return MallResult.buildQueryOk(0D);
        }
        config = configService.findByCode("200");
        return MallResult.buildQueryOk(Double.parseDouble(config.getConfigValues()));
    }
}