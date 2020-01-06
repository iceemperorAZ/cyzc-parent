package com.jingliang.mall.controller;

import com.jingliang.mall.service.BuyerSaleService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户销售绑定表Controller
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-01-03 13:10:34
 */
@RequestMapping(value = "/back/buyerSale")
@Api(tags = "商户销售绑定表")
@RestController
@Slf4j
public class BuyerSaleController {

	private final BuyerSaleService buyerSaleService;

	public BuyerSaleController (BuyerSaleService buyerSaleService) {
		this.buyerSaleService = buyerSaleService;
	}

}