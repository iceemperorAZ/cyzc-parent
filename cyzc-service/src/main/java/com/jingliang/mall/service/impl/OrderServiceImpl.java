package com.jingliang.mall.service.impl;

import com.jingliang.mall.amqp.producer.RabbitProducer;
import com.jingliang.mall.entity.*;
import com.jingliang.mall.repository.*;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.BuyerCouponService;
import com.jingliang.mall.service.OrderDetailService;
import com.jingliang.mall.service.OrderService;
import com.jingliang.mall.service.SkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 订单表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RabbitProducer rabbitProducer;
    private final RedisService redisService;
    private final OrderDetailService orderDetailService;
    private final BuyerCouponService buyerCouponService;
    private final OrderDetailRepository orderDetailRepository;
    private final SkuService skuService;
    private final ConfigRepository configRepository;
    private final BuyerRepository buyerRepository;
    private final GoldLogRepository goldLogRepository;

    public OrderServiceImpl(OrderRepository orderRepository, RabbitProducer rabbitProducer, RedisService redisService, OrderDetailService orderDetailService, BuyerCouponService buyerCouponService, OrderDetailRepository orderDetailRepository, SkuService skuService, ConfigRepository configRepository, BuyerRepository buyerRepository, GoldLogRepository goldLogRepository) {
        this.orderRepository = orderRepository;
        this.rabbitProducer = rabbitProducer;
        this.redisService = redisService;
        this.orderDetailService = orderDetailService;
        this.buyerCouponService = buyerCouponService;
        this.orderDetailRepository = orderDetailRepository;
        this.skuService = skuService;
        this.configRepository = configRepository;
        this.buyerRepository = buyerRepository;
        this.goldLogRepository = goldLogRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order save(Order order) {
        Buyer buyer = buyerRepository.findAllByIdAndIsAvailable(order.getBuyerId(), true);
        if (order.getIsGold() != null && order.getIsGold()) {
            buyer.setGold(buyer.getGold() - order.getGold());
            buyerRepository.save(buyer);
        }
        if (order.getPayableFee() > 0 && buyer.getOrderSpecificNum() > 0) {
            buyer.setOrderSpecificNum(buyer.getOrderSpecificNum() - 1);
            //计算返金币比例
            Config config = configRepository.findFirstByCodeAndIsAvailable("800", true);
            double percentage = Integer.parseInt(config.getConfigValues()) * 0.01;
            //返的金币数
            int gold = (int) ((order.getPayableFee() / 100.00) * percentage);
            order.setReturnGold(gold);
        }
        order = orderRepository.save(order);
        //减库存
        List<OrderDetail> orderDetails = order.getOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.setOrderId(order.getId());
            orderDetail.setOrderNo(order.getOrderNo());
            Sku sku = new Sku();
            sku.setProductId(orderDetail.getProductId());
            sku.setUpdateTime(order.getCreateTime());
            sku.setUpdateUserId(-1L);
            sku.setUpdateUserName("系统");
            sku.setSkuLineNum(-orderDetail.getProductNum());
            rabbitProducer.sendSku(sku);
        }
        orderDetailRepository.saveAll(orderDetails);
        return order;
    }

    @Override
    public Order findByIdAndBuyerId(Long id, Long buyerId) {
        return orderRepository.findFirstByIdAndBuyerId(id, buyerId);
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Order> findAll(Specification<Order> orderSpecification, PageRequest pageRequest) {
        return orderRepository.findAll(orderSpecification, pageRequest);
    }

    @Override
    public Order cancelOrderByOrderId(Long id) {
        return null;
    }

    @Override
    public Order findByOrderNo(String orderNo) {
        return orderRepository.findFirstByOrderNo(orderNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order update(Order order) {
        //如果是取消 则返回库存  返回优惠券
        if (order.getOrderStatus() == 200) {
            //如果有使用优惠券则返还优惠券
            if (Objects.nonNull(order.getCouponIds())) {
                for (String couponId : order.getCouponIds().split(",")) {
                    String[] split = couponId.split("\\|");
                    BuyerCoupon buyerCoupon = buyerCouponService.findByIdAndBuyerId(Long.parseLong(split[0]), order.getBuyerId());
                    buyerCoupon.setReceiveNum(Integer.parseInt(split[1]) + buyerCoupon.getReceiveNum());
                    buyerCouponService.save(buyerCoupon);
                }
            }
            //查询订单详情
            List<OrderDetail> orderDetails = orderDetailService.findByOrderId(order.getId());
            for (OrderDetail orderDetail : orderDetails) {
                //把订单中的商品库存再加回去
                redisService.skuLineIncrement(String.valueOf(orderDetail.getProductId()), orderDetail.getProductNum());
                Sku sku = new Sku();
                sku.setProductId(orderDetail.getProductId());
                sku.setUpdateTime(new Date());
                sku.setUpdateUserId(-1L);
                sku.setUpdateUserName("系统");
                sku.setSkuLineNum(orderDetail.getProductNum());
                rabbitProducer.sendSku(sku);
            }
            Order oldOrder = orderRepository.findAllByIdAndIsAvailable(order.getId(), true);
            Buyer buyer = buyerRepository.findAllByIdAndIsAvailable(oldOrder.getBuyerId(), true);
            if (oldOrder.getIsGold() != null && oldOrder.getIsGold()) {
                buyer.setGold(buyer.getGold() + oldOrder.getGold());
                buyerRepository.save(buyer);
            }
            if (order.getPayableFee() > 0) {
                buyer.setOrderSpecificNum(buyer.getOrderSpecificNum() + 1);
            }
        } else if (order.getOrderStatus() == 400) {
            //如果为发货状态，则减去实际库存
            //查询订单详情
            List<OrderDetail> orderDetails = orderDetailService.findByOrderId(order.getId());
            for (OrderDetail orderDetail : orderDetails) {
                Sku sku = skuService.findByProductId(orderDetail.getProductId());
                if (sku.getSkuRealityNum() - orderDetail.getProductNum() < 0) {
                    return null;
                }
                sku = new Sku();
                sku.setProductId(orderDetail.getProductId());
                sku.setUpdateTime(order.getUpdateTime());
                sku.setUpdateUserId(-1L);
                sku.setUpdateUserName("系统");
                //减实际库存
                sku.setSkuRealityNum(-orderDetail.getProductNum());
                skuService.updateRealitySkuByProductId(sku);
            }
        } else if (order.getOrderStatus() == 700 || order.getOrderStatus() == 800) {
            Order oldOrder = orderRepository.findAllByIdAndIsAvailable(order.getId(), true);
            //查询订单详情
            List<OrderDetail> orderDetails = orderDetailService.findByOrderId(order.getId());
            for (OrderDetail orderDetail : orderDetails) {
                //把订单中的商品库存再加回去
                redisService.skuLineIncrement(String.valueOf(orderDetail.getProductId()), orderDetail.getProductNum());
                Sku sku = new Sku();
                sku.setProductId(orderDetail.getProductId());
                sku.setUpdateTime(new Date());
                sku.setUpdateUserId(-1L);
                sku.setUpdateUserName("系统");
                sku.setSkuLineNum(orderDetail.getProductNum());
                rabbitProducer.sendSku(sku);
                //如果已经发货，退货要加实际库存
                if (oldOrder.getOrderStatus() >= 400) {
                    Sku sku1 = new Sku();
                    sku1.setProductId(orderDetail.getProductId());
                    sku1.setSkuRealityNum(orderDetail.getProductNum());
                    sku1.setUpdateTime(new Date());
                    sku1.setUpdateUserId(-1L);
                    sku1.setUpdateUserName("系统");
                    skuService.updateRealitySkuByProductId(sku1);
                }
            }
        } else if (order.getOrderStatus() == 600 && order.getReturnGold() != null && order.getReturnGold() > 0) {
            //订单完成之后返金币
            //计算返金币比例
            Config config = configRepository.findFirstByCodeAndIsAvailable("800", true);
            double percentage = Integer.parseInt(config.getConfigValues()) * 0.01;
            //返的金币数
            int gold = (int) ((order.getPayableFee() / 100.00) * percentage);
            Buyer buyer = buyerRepository.findAllByIdAndIsAvailable(order.getBuyerId(), true);
            buyer.setGold(buyer.getGold() + gold);
            buyerRepository.save(buyer);
            GoldLog goldLog = new GoldLog();
            goldLog.setGold(gold);
            goldLog.setIsAvailable(true);
            goldLog.setMoney(order.getPayableFee().intValue());
            goldLog.setType(400);
            goldLog.setCreateTime(new Date());
            goldLog.setPayNo(order.getOrderNo());
            goldLog.setBuyerId(order.getBuyerId());
            goldLog.setMsg("微信支付订单[" + order.getOrderNo() + "]￥" + (order.getPayableFee() / 100.00) + "元,获得" + gold + "金币");
            goldLogRepository.save(goldLog);
        }
        order = orderRepository.save(order);
        return order;
    }

    @Override
    public List<Order> findAll(Specification<Order> orderSpecification) {
        return orderRepository.findAll(orderSpecification);
    }
}