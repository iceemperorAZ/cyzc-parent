package com.jingliang.mall.service;

import com.jingliang.mall.entity.Order;
import com.jingliang.mall.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

/**
 * 订单表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
public interface OrderService {

    /**
     * 保存订单信息
     *
     * @param order 订单对象
     * @return 返回保存后的订单信息
     */
    Order save(Order order, List<OrderDetail> drinksDetails, Long drinksPrice);

    /**
     * 根据订单Id和会员Id查询订单信息
     *
     * @param id      主键Id
     * @param buyerId 会员Id
     * @return 返回查询到的订单信息
     */
    Order findByIdAndBuyerId(Long id, Long buyerId);

    /**
     * 根据订单Id和查询订单信息
     *
     * @param id 主键Id
     * @return 返回查询到的订单信息
     */
    Order findById(Long id);

    /**
     * 分页查询全部用户订单信息
     *
     * @param orderSpecification 查询条件
     * @param pageRequest        分页条件
     * @return 返回查询到的订单信息
     */
    Page<Order> findAll(Specification<Order> orderSpecification, PageRequest pageRequest);

    /**
     * 根据订单Id取消订单
     *
     * @param id 主键Id
     * @return 返回查询到的订单信息
     */
    Order cancelOrderByOrderId(Long id);

    /**
     * 根据订单编号查询订单信息
     *
     * @param orderNo 订单编号
     * @return 返回查询到的订单信息
     */
    Order findByOrderNo(String orderNo);

    /**
     * 更新订单
     *
     * @param order 订单
     * @return 返回查询到的订单信息
     */
    Order update(Order order);

    /**
     * 根据条件查询订单列表
     *
     * @param orderSpecification 查询条件
     * @return 返回查询到的订单信息
     */
    List<Order> findAll(Specification<Order> orderSpecification);

    /**
     * 订单导出excel
     * @return
     */
    List<Map<String,String>> orderExcel();
}