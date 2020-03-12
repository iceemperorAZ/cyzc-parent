package com.jingliang.mall.resp;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 转盘日志
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 11:40:28
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
	 * 奖品Id
	 */
	@ApiModelProperty(value = "奖品Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long prizeId;

	/**
	 * 奖品名称
	 */
	@ApiModelProperty(value = "奖品名称")
	private String prizeName;

	/**
	 * 类型(100:谢谢惠顾,200:金币，300:返币次数，400：商品[为商品时需配置奖品Id],)
	 */
	@ApiModelProperty(value = "类型(100:谢谢惠顾,200:金币，300:返币次数，400：商品[为商品时需配置奖品Id],)")
	private Integer type;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

}