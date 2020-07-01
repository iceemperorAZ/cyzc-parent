package com.jingliang.mall.req;

import java.util.Date;
import lombok.Data;
import com.citrsw.annatation.Api;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.citrsw.annatation.ApiProperty;
import java.io.Serializable;

/**
 * 退货表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-20 17:57:20
 */
@Api(description = "退货表")
@Data
public class OfflineOrderReturnReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiProperty(description = "主键Id")
	private Long id;

	/**
	 * 原订单Id
	 */
	@ApiProperty(description = "原订单Id")
	private Long orderId;

	/**
	 * 商品名称
	 */
	@ApiProperty(description = "商品名称")
	private String productName;

	/**
	 * 商品规格
	 */
	@ApiProperty(description = "商品规格")
	private String productSpecification;

	/**
	 * 单位
	 */
	@ApiProperty(description = "单位")
	private String company;

	/**
	 * 数量
	 */
	@ApiProperty(description = "数量")
	private Integer num;

	/**
	 * 单价(单位：分)
	 */
	@ApiProperty(description = "单价(单位：分)")
	private Integer unitPrice;

	/**
	 * 总价(单位：分)
	 */
	@ApiProperty(description = "总价(单位：分)")
	private Integer totalPrice;

	/**
	 * 状态(退货中，退货完成/退款中，退款完成/结束)
	 */
	@ApiProperty(description = "状态(退货中，退货完成/退款中，退款完成/结束)")
	private Integer orderStatus;

	/**
	 * 操作人Id
	 */
	@ApiProperty(description = "操作人Id")
	private Long createUserId;

	/**
	 * 操作时间
	 */
	@ApiProperty(description = "操作时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 退款人Id
	 */
	@ApiProperty(description = "退款人Id")
	private Long refundUserId;

	/**
	 * 退款时间
	 */
	@ApiProperty(description = "退款时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date refundTime;

}