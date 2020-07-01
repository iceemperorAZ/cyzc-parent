package com.jingliang.mall.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户购物车
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(name = "Cart", description = "用户购物车")
public class CartReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiProperty(description = "主键")
	private Long id;

	/**
	 * 主键-开始
	 */
	@ApiProperty(description = "主键-开始")
	private Long idStart;

	/**
	 * 主键-结束
	 */
	@ApiProperty(description = "主键-结束")
	private Long idEnd;

	/**
	 * 用户Id
	 */
	@ApiProperty(description = "用户Id")
	private Long buyerId;

	/**
	 * 用户Id-开始
	 */
	@ApiProperty(description = "用户Id-开始")
	private Long buyerIdStart;

	/**
	 * 用户Id-结束
	 */
	@ApiProperty(description = "用户Id-结束")
	private Long buyerIdEnd;

	/**
	 * 商品Id
	 */
	@ApiProperty(description = "商品Id")
	private Long productId;

	/**
	 * 商品Id-开始
	 */
	@ApiProperty(description = "商品Id-开始")
	private Long productIdStart;

	/**
	 * 商品Id-结束
	 */
	@ApiProperty(description = "商品Id-结束")
	private Long productIdEnd;

	/**
	 * 商品数量
	 */
	@ApiProperty(description = "商品数量")
	private Integer productNum;

	/**
	 * 商品数量-开始
	 */
	@ApiProperty(description = "商品数量-开始")
	private Integer productNumStart;

	/**
	 * 商品数量-结束
	 */
	@ApiProperty(description = "商品数量-结束")
	private Integer productNumEnd;

	/**
	 * 添加时间(创建时间)
	 */
	@ApiProperty(description = "添加时间(创建时间)")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 添加时间(创建时间)-开始
	 */
	@ApiProperty(description = "添加时间(创建时间)-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeStart;

	/**
	 * 添加时间(创建时间)-结束
	 */
	@ApiProperty(description = "添加时间(创建时间)-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeEnd;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiProperty(description = "是否可用 0：否，1：是")
	private Boolean isAvailable;

}