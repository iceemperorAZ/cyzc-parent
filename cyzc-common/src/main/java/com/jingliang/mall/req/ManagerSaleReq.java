package com.jingliang.mall.req;

import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 商户销售绑定表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-02-17 14:21:26
 */
@ApiModel(value = "ManagerSaleReq", description = "商户销售绑定表")
@EqualsAndHashCode(callSuper = true)
@Data
public class ManagerSaleReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiModelProperty(value = "主键Id")
	private Long id;

	/**
	 * 主键Id-开始
	 */
	@ApiModelProperty(value = "主键Id-开始")
	private Long idStart;

	/**
	 * 主键Id-结束
	 */
	@ApiModelProperty(value = "主键Id-结束")
	private Long idEnd;

	/**
	 * 区域经理Id
	 */
	@ApiModelProperty(value = "区域经理Id")
	private Long managerId;

	/**
	 * 区域经理Id-开始
	 */
	@ApiModelProperty(value = "区域经理Id-开始")
	private Long managerIdStart;

	/**
	 * 区域经理Id-结束
	 */
	@ApiModelProperty(value = "区域经理Id-结束")
	private Long managerIdEnd;

	/**
	 * 销售Id
	 */
	@ApiModelProperty(value = "销售Id")
	private Long saleId;

	/**
	 * 销售Id-开始
	 */
	@ApiModelProperty(value = "销售Id-开始")
	private Long saleIdStart;

	/**
	 * 销售Id-结束
	 */
	@ApiModelProperty(value = "销售Id-结束")
	private Long saleIdEnd;

	/**
	 * 绑定时间
	 */
	@ApiModelProperty(value = "绑定时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 绑定时间-开始
	 */
	@ApiModelProperty(value = "绑定时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeStart;

	/**
	 * 绑定时间-结束
	 */
	@ApiModelProperty(value = "绑定时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeEnd;

	/**
	 * 解绑时间
	 */
	@ApiModelProperty(value = "解绑时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date untyingTime;

	/**
	 * 解绑时间-开始
	 */
	@ApiModelProperty(value = "解绑时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date untyingTimeStart;

	/**
	 * 解绑时间-结束
	 */
	@ApiModelProperty(value = "解绑时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date untyingTimeEnd;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

	/**
	 * 更新人Id
	 */
	@ApiModelProperty(value = "更新人Id")
	private Long updateUserId;

	/**
	 * 更新人Id-开始
	 */
	@ApiModelProperty(value = "更新人Id-开始")
	private Long updateUserIdStart;

	/**
	 * 更新人Id-结束
	 */
	@ApiModelProperty(value = "更新人Id-结束")
	private Long updateUserIdEnd;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private String updateUser;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 更新时间-开始
	 */
	@ApiModelProperty(value = "更新时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeStart;

	/**
	 * 更新时间-结束
	 */
	@ApiModelProperty(value = "更新时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeEnd;

}