package com.jingliang.mall.resp;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import lombok.Data;
import com.citrsw.annatation.ApiModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.citrsw.annatation.ApiProperty;
import java.io.Serializable;

/**
 * 商户销售绑定表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-02-17 14:21:26
 */
@ApiModel(name = "ManagerSaleResp", description = "商户销售绑定表")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ManagerSaleResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiProperty(description = "主键Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 区域经理Id
	 */
	@ApiProperty(description = "区域经理Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long managerId;

	/**
	 * 销售Id
	 */
	@ApiProperty(description = "销售Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long saleId;

	/**
	 * 绑定时间
	 */
	@ApiProperty(description = "绑定时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 解绑时间
	 */
	@ApiProperty(description = "解绑时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date untyingTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiProperty(description = "是否可用 0：否，1：是")
	private Boolean isAvailable;

	/**
	 * 更新人Id
	 */
	@ApiProperty(description = "更新人Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long updateUserId;

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

}