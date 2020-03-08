package com.jingliang.mall.req;

import java.time.LocalDate;
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
 * @date 2020-03-03 10:42:56
 */
@ApiModel(value = "SignInReq", description = "签到日志")
@EqualsAndHashCode(callSuper = true)
@Data
public class SignInReq extends BaseReq implements Serializable {

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
	 * 连续签到天数
	 */
	@ApiModelProperty(value = "连续签到天数")
	private Integer dayNum;

	/**
	 * 连续签到天数-开始
	 */
	@ApiModelProperty(value = "连续签到天数-开始")
	private Integer dayNumStart;

	/**
	 * 连续签到天数-结束
	 */
	@ApiModelProperty(value = "连续签到天数-结束")
	private Integer dayNumEnd;

	/**
	 * 最后一次签到日期
	 */
	@ApiModelProperty(value = "最后一次签到日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lastDate;

	/**
	 * 最后一次签到日期-开始
	 */
	@ApiModelProperty(value = "最后一次签到日期-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lastDateStart;

	/**
	 * 最后一次签到日期-结束
	 */
	@ApiModelProperty(value = "最后一次签到日期-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDate lastDateEnd;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

}