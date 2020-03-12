package com.jingliang.mall.req;

import java.time.LocalDateTime;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 转盘
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 11:40:28
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TurntableReq", description = "转盘")
@Data
public class TurntableReq extends BaseReq implements Serializable {

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
	 * 奖品Id
	 */
	@ApiModelProperty(value = "奖品Id")
	private Long prizeId;

	/**
	 * 奖品Id-开始
	 */
	@ApiModelProperty(value = "奖品Id-开始")
	private Long prizeIdStart;

	/**
	 * 奖品Id-结束
	 */
	@ApiModelProperty(value = "奖品Id-结束")
	private Long prizeIdEnd;

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
	 * 类型(100:谢谢惠顾,200:金币，300:返币次数，400：商品[为商品时需配置奖品Id],)-开始
	 */
	@ApiModelProperty(value = "类型(100:谢谢惠顾,200:金币，300:返币次数，400：商品[为商品时需配置奖品Id],)-开始")
	private Integer typeStart;

	/**
	 * 类型(100:谢谢惠顾,200:金币，300:返币次数，400：商品[为商品时需配置奖品Id],)-结束
	 */
	@ApiModelProperty(value = "类型(100:谢谢惠顾,200:金币，300:返币次数，400：商品[为商品时需配置奖品Id],)-结束")
	private Integer typeEnd;

	/**
	 * 概率
	 */
	@ApiModelProperty(value = "概率")
	private Integer probability;

	/**
	 * 概率-开始
	 */
	@ApiModelProperty(value = "概率-开始")
	private Integer probabilityStart;

	/**
	 * 概率-结束
	 */
	@ApiModelProperty(value = "概率-结束")
	private Integer probabilityEnd;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 创建人Id
	 */
	@ApiModelProperty(value = "创建人Id")
	private Long createUserId;

	/**
	 * 创建人Id-开始
	 */
	@ApiModelProperty(value = "创建人Id-开始")
	private Long createUserIdStart;

	/**
	 * 创建人Id-结束
	 */
	@ApiModelProperty(value = "创建人Id-结束")
	private Long createUserIdEnd;

	/**
	 * 是否上架
	 */
	@ApiModelProperty(value = "是否上架")
	private Boolean isShow;

	/**
	 * 上架人Id
	 */
	@ApiModelProperty(value = "上架人Id")
	private Long showUserId;

	/**
	 * 上架人Id-开始
	 */
	@ApiModelProperty(value = "上架人Id-开始")
	private Long showUserIdStart;

	/**
	 * 上架人Id-结束
	 */
	@ApiModelProperty(value = "上架人Id-结束")
	private Long showUserIdEnd;

	/**
	 * 上架时间
	 */
	@ApiModelProperty(value = "上架时间")
	private LocalDateTime showTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

	/**
	 * 奖品数量
	 */
	@ApiModelProperty(value = "奖品数量")
	private Integer prizeNum;

	/**
	 * 奖品数量-开始
	 */
	@ApiModelProperty(value = "奖品数量-开始")
	private Integer prizeNumStart;

	/**
	 * 奖品数量-结束
	 */
	@ApiModelProperty(value = "奖品数量-结束")
	private Integer prizeNumEnd;

	/**
	 * 开始角度
	 */
	@ApiModelProperty(value = "开始角度")
	private Integer startAngle;

	/**
	 * 开始角度-开始
	 */
	@ApiModelProperty(value = "开始角度-开始")
	private Integer startAngleStart;

	/**
	 * 开始角度-结束
	 */
	@ApiModelProperty(value = "开始角度-结束")
	private Integer startAngleEnd;

	/**
	 * 结束角度
	 */
	@ApiModelProperty(value = "结束角度")
	private Integer endAngle;

	/**
	 * 结束角度-开始
	 */
	@ApiModelProperty(value = "结束角度-开始")
	private Integer endAngleStart;

	/**
	 * 结束角度-结束
	 */
	@ApiModelProperty(value = "结束角度-结束")
	private Integer endAngleEnd;

}