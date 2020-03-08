package com.jingliang.mall.req;

import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;

/**
 * 签到日志
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-03 13:04:43
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "GoldLogReq", description = "签到日志")
@Data
public class GoldLogReq extends BaseReq implements Serializable {

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
	 * 商户Id
	 */
	@ApiModelProperty(value = "商户Id")
	private Long buyerId;

	/**
	 * 商户Id-开始
	 */
	@ApiModelProperty(value = "商户Id-开始")
	private Long buyerIdStart;

	/**
	 * 商户Id-结束
	 */
	@ApiModelProperty(value = "商户Id-结束")
	private Long buyerIdEnd;

	/**
	 * 日志内容
	 */
	@ApiModelProperty(value = "日志内容")
	private String msg;

	/**
	 * 日志内容-开始
	 */
	@ApiModelProperty(value = "日志内容-开始")
	private Integer msgStart;

	/**
	 * 日志内容-结束
	 */
	@ApiModelProperty(value = "日志内容-结束")
	private Integer msgEnd;

	/**
	 * 签到时间
	 */
	@ApiModelProperty(value = "签到时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 签到时间-开始
	 */
	@ApiModelProperty(value = "签到时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeStart;

	/**
	 * 签到时间-结束
	 */
	@ApiModelProperty(value = "签到时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeEnd;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

	/**
	 * 充值金额
	 */
	@ApiModelProperty(value = "充值金额")
	private Integer money;

	/**
	 * 获得金币
	 */
	@ApiModelProperty(value = "获得金币")
	private Integer gold;
}