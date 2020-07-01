package com.jingliang.mall.controller;

import com.alibaba.fastjson.JSONArray;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.service.BuyerSaleService;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.citrsw.annatation.Api;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商户销售绑定表Controller
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-01-03 13:10:34
 */
@RequestMapping(value = "/back/buyerSale")
@Api(description = "商户销售绑定表")
@RestController
@Slf4j
public class BuyerSaleController {

	private final BuyerSaleService buyerSaleService;

	public BuyerSaleController (BuyerSaleService buyerSaleService) {
		this.buyerSaleService = buyerSaleService;
	}

	@GetMapping("/getAllBuyerAndSaleByTimeToHtml")
	@ApiOperation(description = "根据时间查询商户信息和绑定的销售以及所在大区展示到前台页面")
	public Result<List<Map<String, Object>> > getAllBuyerAndSaleByTime(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date startTime,
											  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date endTime){
		List<Map<String, Object>> buyerSaleByTime = buyerSaleService.findBuyerSaleByTimeToHtml(startTime, endTime);
		return  Result.buildQueryOk(buyerSaleByTime);
	}

	@GetMapping("/getAllBuyerAndSaleByTimeToExcel")
	@ApiOperation(description = "根据时间查询商户信息和绑定的销售以及所在大区展示到前台页面")
	public Result<Object> getAllBuyerAndSaleByTimeToExcel(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date startTime,
											  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date endTime){
		Boolean flg = buyerSaleService.findBuyerSaleByTime(startTime, endTime);
		if (!flg){
			return  Result.buildParamFail();
		}
		return Result.buildQueryOk("导出excel成功！");
	}
}