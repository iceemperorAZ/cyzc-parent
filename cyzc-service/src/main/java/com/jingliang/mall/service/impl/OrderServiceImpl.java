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

    public OrderServiceImpl(OrderRepository orderRepository, RabbitProducer rabbitProducer, RedisService redisService, OrderDetailService orderDetailService, BuyerCouponService buyerCouponService, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.rabbitProducer = rabbitProducer;
        this.redisService = redisService;
        this.orderDetailService = orderDetailService;
        this.buyerCouponService = buyerCouponService;
        this.orderDetailRepository = orderDetailRepository;
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
        order = orderRepository.save(order);
        //如果是取消 则返回库存  返回优惠券
        if (order.getOrderStatus() == 200) {
            //如果有使用优惠券则返还优惠券
            if (Objects.nonNull(order.getCouponId())) {
                BuyerCoupon buyerCoupon = new BuyerCoupon();
                buyerCoupon.setId(order.getCouponId());
                buyerCoupon.setIsUsed(false);
                buyerCouponService.save(buyerCoupon);
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
        }
        return order;
    }

    @Override
    public List<Order> findAll(Specification<Order> orderSpecification) {
        return orderRepository.findAll(orderSpecification);
    }
}