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

import java.util.*;
import java.util.stream.Collectors;

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
    public Order save(Order order, List<OrderDetail> drinksDetails, Long drinksPrice) {
        Buyer buyer = buyerRepository.findAllByIdAndIsAvailable(order.getBuyerId(), true);
        //金币
        final long[] gold = {order.getGold() == null ? 0 : (order.getGold() * 10)};
        for (OrderDetail orderDetail : drinksDetails.stream().sorted(Comparator.comparingLong(OrderDetail::getDifference).reversed()).collect(Collectors.toList())) {
            for (int i = 0; i < orderDetail.getProductNum(); i++) {
                if (gold[0] > 0) {
                    if (gold[0] - (orderDetail.getSellingPrice()) > 0) {
                        //拿出使用的金币数，看能抵消多少件商品
                        gold[0] -= (orderDetail.getSellingPrice());
                        drinksPrice -= (orderDetail.getSellingPrice());
                    }
                    //金币不够支付，算出实际支付价格，以及扣减金币后的价格
                    else {
                        //算出需要支付的钱，除以售价，就是应返金币的比例，用比例乘以应返金币数，就是应返的金币。每个商品的返币比例不同，所以这里的比例根据抵扣完金币后的应付价格和不同商品的售价进行计算。
                        float proportion = ((float) (orderDetail.getSellingPrice() - gold[0]) / (float) orderDetail.getSellingPrice());
                        order.setReturnGold((int) ((order.getReturnGold() == null ? 0 : order.getReturnGold()) + ((orderDetail.getDifference()) / 10 * proportion)));
                        drinksPrice -= gold[0];
                        gold[0] = 0;
                    }
                } else {
                    order.setReturnGold((int) ((order.getReturnGold() == null ? 0 : order.getReturnGold()) + (orderDetail.getDifference() / 10)));
                }
            }
        }
        if ((order.getPayableFee() - drinksPrice) > 0 && buyer.getOrderSpecificNum().compareTo(0) > 0) {
            buyer.setOrderSpecificNum(buyer.getOrderSpecificNum() - 1);
            //计算返金币比例
            Config config = configRepository.findFirstByCodeAndIsAvailable("800", true);
            double percentage = Integer.parseInt(config.getConfigValues()) * 0.01;
            //返的金币数
            int gold1 = (int) (((order.getPayableFee() - drinksPrice) / 100.00) * percentage);
            order.setReturnGold((order.getReturnGold() == null ? 0 : order.getReturnGold()) + gold1);//这里测试时进行了改动，返币应该加上之前饮料的返币
        }

        if (order.getIsGold() != null && order.getIsGold()) {
            buyer.setGold(buyer.getGold() - order.getGold());
            buyerRepository.save(buyer);
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
                //把购买次数退回去
                redisService.decrement("PRODUCT-BUYER-LIMIT-" + oldOrder.getBuyerId() + orderDetail.getProductId() + "", orderDetail.getProductNum());
            }
            Buyer buyer = buyerRepository.findAllByIdAndIsAvailable(oldOrder.getBuyerId(), true);
            if (oldOrder.getIsGold() != null && oldOrder.getIsGold()) {
                buyer.setGold(buyer.getGold() + oldOrder.getGold());
            }
            if (order.getPayableFee() > 0 && order.getReturnGold() != null && order.getReturnGold() > 0) {
                buyer.setOrderSpecificNum(buyer.getOrderSpecificNum() + 1);
            }
            buyerRepository.saveAndFlush(buyer);
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
            if (oldOrder.getGold() != null) {
                //退还的金币数
                int gold = oldOrder.getGold();
                Buyer buyer = buyerRepository.findAllByIdAndIsAvailable(oldOrder.getBuyerId(), true);
                buyer.setGold(buyer.getGold() + gold);
                buyerRepository.saveAndFlush(buyer);
                GoldLog goldLog = new GoldLog();
                goldLog.setGold(gold);
                goldLog.setIsAvailable(true);
                goldLog.setMoney(oldOrder.getPayableFee().intValue());
                goldLog.setType(500);
                goldLog.setCreateTime(new Date());
                goldLog.setPayNo(oldOrder.getOrderNo());
                goldLog.setBuyerId(oldOrder.getBuyerId());
                goldLog.setMsg("订单[" + oldOrder.getOrderNo() + "]退货返还" + gold + "金币");
                goldLogRepository.save(goldLog);
            }
        } else if (order.getOrderStatus() == 600) {
            Order oldOrder = orderRepository.findAllByIdAndIsAvailable(order.getId(), true);
            if (oldOrder.getReturnGold() != null && oldOrder.getReturnGold() > 0) {
                //订单完成之后返金币
                //返的金币数
                int gold = oldOrder.getReturnGold();
                Buyer buyer = buyerRepository.findAllByIdAndIsAvailable(oldOrder.getBuyerId(), true);
                buyer.setGold(buyer.getGold() + gold);
                buyerRepository.saveAndFlush(buyer);
                GoldLog goldLog = new GoldLog();
                goldLog.setGold(gold);
                goldLog.setIsAvailable(true);
                goldLog.setMoney(oldOrder.getPayableFee().intValue());
                goldLog.setType(400);
                goldLog.setCreateTime(new Date());
                goldLog.setPayNo(oldOrder.getOrderNo());
                goldLog.setBuyerId(oldOrder.getBuyerId());
                goldLog.setMsg("微信支付订单[" + oldOrder.getOrderNo() + "]￥" + (oldOrder.getPayableFee() / 100.00) + "元,获得" + gold + "金币");
                goldLogRepository.save(goldLog);
            }
            //留着退货会用到
//            order.setReturnGold(0);
        }
        order.setSale(null);
        order.setBuyer(null);
        order = orderRepository.save(order);
        return order;
    }

    @Override
    public List<Order> findAll(Specification<Order> orderSpecification) {
        return orderRepository.findAll(orderSpecification);
    }

    @Override
    public List<Map<String, String>> orderExcel() {
        return orderRepository.orderExcel();
    }
}