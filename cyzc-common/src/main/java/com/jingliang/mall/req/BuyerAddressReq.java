package com.jingliang.mall.req;

import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@ApiModel(name =  "BuyerAddressReq", description = "会员收货地址表")
@EqualsAndHashCode(callSuper = true)
@Data
public class BuyerAddressReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiProperty(description = "主键")
	private Long id;

	/**
	 * 会员Id
	 */
	@ApiProperty(description = "会员Id")
	private Long buyerId;

	/**
	 * 省编码
	 */
	@ApiProperty(description = "省编码")
	private String provinceCode;

	/**
	 * 市编码
	 */
	@ApiProperty(description = "市编码")
	private String cityCode;

	/**
	 * 区/县编码
	 */
	@ApiProperty(description = "区/县编码")
	private String areaCode;

	/**
	 * 街道编码：0-99999
	 */
	@ApiProperty(description = "街道编码：0-99999")
	private String streetCode;

	/**
	 * 街道信息
	 */
	@ApiProperty(description = "街道信息")
	private String street;
	/**
	 * 详细地址
	 */
	@ApiProperty(description = "详细地址")
	private String detailedAddress;

	/**
	 * 是否默认 0：否，1：是
	 */
	@ApiProperty(description = "是否默认 0：否，1：是")
	private Boolean isDefault;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiProperty(description = "是否可用 0：否，1：是")
	private Boolean isAvailable;

	/**
	 * 创建时间
	 */
	@ApiProperty(description = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@ApiProperty(description = "修改时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 收货人名称
	 */
	@ApiProperty(description = "收货人名称")
	private String consigneeName;

	/**
	 * 收货人电话
	 */
	@ApiProperty(description = "收货人电话")
	private String phone;

	/**
	 * 邮编
	 */
	@ApiProperty(description = "邮编")
	private String postCode;


	/**
	 * 经度
	 */
	@ApiProperty(description = "经度")
	private String latitude;

	/**
	 * 纬度
	 */
	@ApiProperty(description = "纬度")
	private String longitude;

	/**
	 * 地图地址
	 */
	@ApiProperty(description = "地图地址")
	private String mapAddress;

}