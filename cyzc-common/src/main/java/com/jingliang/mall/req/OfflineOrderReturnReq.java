package com.jingliang.mall.req;

import java.util.Date;
import lombok.Data;
import io.swagger.annotations.Api;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 退货表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-20 17:57:20
 */
@Api(value = "退货表")
@Data
public class OfflineOrderReturnReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiModelProperty(value = "主键Id")
	private Long id;

	/**
	 * 原订单Id
	 */
	@ApiModelProperty(value = "原订单Id")
	private Long orderId;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String productName;

	/**
	 * 商品规格
	 */
	@ApiModelProperty(value = "商品规格")
	private String productSpecification;

	/**
	 * 单位
	 */
	@ApiModelProperty(value = "单位")
	private String company;

	/**
	 * 数量
	 */
	@ApiModelProperty(value = "数量")
	private Integer num;

	/**
	 * 单价(单位：分)
	 */
	@ApiModelProperty(value = "单价(单位：分)")
	private Integer unitPrice;

	/**
	 * 总价(单位：分)
	 */
	@ApiModelProperty(value = "总价(单位：分)")
	private Integer totalPrice;

	/**
	 * 状态(退货中，退货完成/退款中，退款完成/结束)
	 */
	@ApiModelProperty(value = "状态(退货中，退货完成/退款中，退款完成/结束)")
	private Integer orderStatus;

	/**
	 * 操作人Id
	 */
	@ApiModelProperty(value = "操作人Id")
	private Long createUserId;

	/**
	 * 操作时间
	 */
	@ApiModelProperty(value = "操作时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 退款人Id
	 */
	@ApiModelProperty(value = "退款人Id")
	private Long refundUserId;

	/**
	 * 退款时间
	 */
	@ApiModelProperty(value = "退款时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date refundTime;

}