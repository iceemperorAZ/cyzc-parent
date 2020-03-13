package com.jingliang.mall.resp;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

/**
 * 转盘日志
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(value = "TurntableLogResp", description = "转盘日志")
public class TurntableLogResp implements Serializable {

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
	 * 消息内容
	 */
	@ApiModelProperty(value = "消息内容")
	private String msg;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

}