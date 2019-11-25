package com.jingliang.mall.controller;

import com.jingliang.mall.service.OrderDetailService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单详情表Controller
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 09:10:24
 */
@Api(tags = "订单详情")
@RestController
@RequestMapping("/front/orderDetail")
@Slf4j
public class OrderDetailController {

	private final OrderDetailService orderDetailService;

	public OrderDetailController (OrderDetailService orderDetailService) {
		this.orderDetailService = orderDetailService;
	}

}