package com.jingliang.mall.resp;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 签到日志
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-03 13:04:43
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "GoldLogResp", description = "签到日志")
@Data
public class GoldLogResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiModelProperty(value = "主键Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 商户Id
	 */
	@ApiModelProperty(value = "商户Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long buyerId;

	/**
	 * 日志内容
	 */
	@ApiModelProperty(value = "日志内容")
	private String msg;

	/**
	 * 签到时间
	 */
	@ApiModelProperty(value = "签到时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

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