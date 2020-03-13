package com.jingliang.mall.resp;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

/**
 * 转盘详情
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "TurntableDetailResp", description = "转盘详情")
@Data
public class TurntableDetailResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiModelProperty(value = "主键Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 转盘Id
	 */
	@ApiModelProperty(value = "转盘Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long turntableId;

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
	 * 概率
	 */
	@ApiModelProperty(value = "概率")
	private Integer probability;

	/**
	 * 奖品数量
	 */
	@ApiModelProperty(value = "奖品数量")
	private Integer prizeNum;

	/**
	 * 开始角度
	 */
	@ApiModelProperty(value = "开始角度")
	private Integer startAngle;

	/**
	 * 结束角度
	 */
	@ApiModelProperty(value = "结束角度")
	private Integer endAngle;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**
	 * 创建人Id
	 */
	@ApiModelProperty(value = "创建人Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long createUserId;

	/**
	 * 是否上架
	 */
	@ApiModelProperty(value = "是否上架")
	private Boolean isShow;

	/**
	 * 上架人Id
	 */
	@ApiModelProperty(value = "上架人Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long showUserId;

	/**
	 * 上架时间
	 */
	@ApiModelProperty(value = "上架时间")
	private Date showTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

}