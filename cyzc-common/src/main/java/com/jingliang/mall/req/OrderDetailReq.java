package com.jingliang.mall.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单详情表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-19 17:44:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "OrderDetailReq", description = "订单详情表")
public class OrderDetailReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * 主键-开始
	 */
	@ApiModelProperty(value = "主键-开始")
	private Long idStart;

	/**
	 * 主键-结束
	 */
	@ApiModelProperty(value = "主键-结束")
	private Long idEnd;

	/**
	 * 订单编号
	 */
	@ApiModelProperty(value = "订单编号")
	private String orderNo;

	/**
	 * 订单Id
	 */
	@ApiModelProperty(value = "订单Id")
	private Long orderId;

	/**
	 * 订单Id-开始
	 */
	@ApiModelProperty(value = "订单Id-开始")
	private Long orderIdStart;

	/**
	 * 订单Id-结束
	 */
	@ApiModelProperty(value = "订单Id-结束")
	private Long orderIdEnd;

	/**
	 * 商品Id
	 */
	@ApiModelProperty(value = "商品Id")
	private Long productId;

	/**
	 * 商品Id-开始
	 */
	@ApiModelProperty(value = "商品Id-开始")
	private Long productIdStart;

	/**
	 * 商品Id-结束
	 */
	@ApiModelProperty(value = "商品Id-结束")
	private Long productIdEnd;

	/**
	 * 商品售价
	 */
	@ApiModelProperty(value = "商品售价")
	private Double sellingPrice;

	/**
	 * 商品售价-开始
	 */
	@ApiModelProperty(value = "商品售价-开始")
	private Double sellingPriceStart;

	/**
	 * 商品售价-结束
	 */
	@ApiModelProperty(value = "商品售价-结束")
	private Double sellingPriceEnd;

	/**
	 * 商品数量
	 */
	@ApiModelProperty(value = "商品数量")
	private Integer productNum;

	/**
	 * 商品数量-开始
	 */
	@ApiModelProperty(value = "商品数量-开始")
	private Integer productNumStart;

	/**
	 * 商品数量-结束
	 */
	@ApiModelProperty(value = "商品数量-结束")
	private Integer productNumEnd;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 创建时间-开始
	 */
	@ApiModelProperty(value = "创建时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeStart;

	/**
	 * 创建时间-结束
	 */
	@ApiModelProperty(value = "创建时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeEnd;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

}