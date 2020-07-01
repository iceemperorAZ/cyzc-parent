package com.jingliang.mall.req;

import java.util.Date;
import lombok.Data;
import com.citrsw.annatation.ApiModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import com.citrsw.annatation.ApiProperty;
import java.io.Serializable;

/**
 * 商户销售绑定表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-01-03 13:10:34
 */
@ApiModel(name =  "BuyerSaleReq", description = "商户销售绑定表")
@EqualsAndHashCode(callSuper = true)
@Data
public class BuyerSaleReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiProperty(description = "主键Id")
	private Long id;

	/**
	 * 主键Id-开始
	 */
	@ApiProperty(description = "主键Id-开始")
	private Long idStart;

	/**
	 * 主键Id-结束
	 */
	@ApiProperty(description = "主键Id-结束")
	private Long idEnd;

	/**
	 * 商户Id
	 */
	@ApiProperty(description = "商户Id")
	private Long buyerId;

	/**
	 * 商户Id-开始
	 */
	@ApiProperty(description = "商户Id-开始")
	private Long buyerIdStart;

	/**
	 * 商户Id-结束
	 */
	@ApiProperty(description = "商户Id-结束")
	private Long buyerIdEnd;

	/**
	 * 销售Id
	 */
	@ApiProperty(description = "销售Id")
	private Long saleId;

	/**
	 * 销售Id-开始
	 */
	@ApiProperty(description = "销售Id-开始")
	private Long saleIdStart;

	/**
	 * 销售Id-结束
	 */
	@ApiProperty(description = "销售Id-结束")
	private Long saleIdEnd;

	/**
	 * 绑定时间
	 */
	@ApiProperty(description = "绑定时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 绑定时间-开始
	 */
	@ApiProperty(description = "绑定时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeStart;

	/**
	 * 绑定时间-结束
	 */
	@ApiProperty(description = "绑定时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeEnd;

	/**
	 * 解绑时间
	 */
	@ApiProperty(description = "解绑时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date untyingTime;

	/**
	 * 解绑时间-开始
	 */
	@ApiProperty(description = "解绑时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date untyingTimeStart;

	/**
	 * 解绑时间-结束
	 */
	@ApiProperty(description = "解绑时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date untyingTimeEnd;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiProperty(description = "是否可用 0：否，1：是")
	private Boolean isAvailable;

	/**
	 * 更新人Id
	 */
	@ApiProperty(description = "更新人Id")
	private Long updateUserId;

	/**
	 * 更新人Id-开始
	 */
	@ApiProperty(description = "更新人Id-开始")
	private Long updateUserIdStart;

	/**
	 * 更新人Id-结束
	 */
	@ApiProperty(description = "更新人Id-结束")
	private Long updateUserIdEnd;

	/**
	 * 更新人
	 */
	@ApiProperty(description = "更新人")
	private String updateUser;

	/**
	 * 更新时间
	 */
	@ApiProperty(description = "更新时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 更新时间-开始
	 */
	@ApiProperty(description = "更新时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeStart;

	/**
	 * 更新时间-结束
	 */
	@ApiProperty(description = "更新时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeEnd;

}