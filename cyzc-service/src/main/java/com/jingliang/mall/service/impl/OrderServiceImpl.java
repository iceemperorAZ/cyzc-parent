package com.jingliang.mall.service.impl;

import com.jingliang.mall.amqp.producer.RabbitProducer;
import com.jingliang.mall.entity.BuyerCoupon;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.entity.OrderDetail;
import com.jingliang.mall.entity.Sku;
import com.jingliang.mall.repository.OrderDetailRepository;
import com.jingliang.mall.repository.OrderRepository;
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

    public OrderServiceImpl(OrderRepository orderRepository, RabbitProducer rabbitProducer, RedisService redisService, OrderDetailService orderDetailService, BuyerCouponService buyerCouponService, OrderDetailRepository orderDetailRepository, SkuService skuService) {
        this.orderRepository = orderRepository;
        this.rabbitProducer = rabbitProducer;
        this.redisService = redisService;
        this.orderDetailService = orderDetailService;
        this.buyerCouponService = buyerCouponService;
        this.orderDetailRepository = orderDetailRepository;
        this.skuService = skuService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order save(Order order) {
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
        }
        order = orderRepository.save(order);
        return order;
    }

    @Override
    public List<Order> findAll(Specification<Order> orderSpecification) {
        return orderRepository.findAll(orderSpecification);
    }
}