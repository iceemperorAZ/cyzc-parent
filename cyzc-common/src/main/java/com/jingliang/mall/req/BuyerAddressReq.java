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
 * 会员收货地址表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-23 16:51:26
 */
@ApiModel(value = "BuyerAddressReq", description = "会员收货地址表")
@EqualsAndHashCode(callSuper = true)
@Data
public class BuyerAddressReq extends BaseReq implements Serializable {

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
	 * 会员Id
	 */
	@ApiModelProperty(value = "会员Id")
	private Long buyerId;

	/**
	 * 会员Id-开始
	 */
	@ApiModelProperty(value = "会员Id-开始")
	private Long buyerIdStart;

	/**
	 * 会员Id-结束
	 */
	@ApiModelProperty(value = "会员Id-结束")
	private Long buyerIdEnd;

	/**
	 * 省编码
	 */
	@ApiModelProperty(value = "省编码")
	private String provinceCode;

	/**
	 * 市编码
	 */
	@ApiModelProperty(value = "市编码")
	private String cityCode;

	/**
	 * 区/县编码
	 */
	@ApiModelProperty(value = "区/县编码")
	private String areaCode;

	/**
	 * 街道编码：0-99999
	 */
	@ApiModelProperty(value = "街道编码：0-99999")
	private String streetCode;

	/**
	 * 街道信息
	 */
	@ApiModelProperty(value = "街道信息")
	private String street;
	/**
	 * 详细地址
	 */
	@ApiModelProperty(value = "详细地址")
	private String detailedAddress;

	/**
	 * 是否默认 0：否，1：是
	 */
	@ApiModelProperty(value = "是否默认 0：否，1：是")
	private Boolean isDefault;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

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
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 修改时间-开始
	 */
	@ApiModelProperty(value = "修改时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeStart;

	/**
	 * 修改时间-结束
	 */
	@ApiModelProperty(value = "修改时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeEnd;

	/**
	 * 收货人名称
	 */
	@ApiModelProperty(value = "收货人名称")
	private String consigneeName;

	/**
	 * 收货人电话
	 */
	@ApiModelProperty(value = "收货人电话")
	private String phone;

	/**
	 * 邮编
	 */
	@ApiModelProperty(value = "邮编")
	private String postCode;


	/**
	 * 经度
	 */
	@ApiModelProperty(value = "经度")
	private String latitude;

	/**
	 * 纬度
	 */
	@ApiModelProperty(value = "纬度")
	private String longitude;

}