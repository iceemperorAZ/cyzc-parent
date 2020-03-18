package com.jingliang.mall.req;

import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jingliang.mall.req.BaseReq;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 转盘详情
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-17 10:04:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "TurntableDetailReq", description = "转盘详情")
public class TurntableDetailReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiModelProperty(value = "主键Id")
	private Long id;

	/**
	 * 转盘Id
	 */
	@ApiModelProperty(value = "转盘Id")
	private Long turntableId;

	/**
	 * 奖品Id
	 */
	@ApiModelProperty(value = "奖品Id")
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
	 * 奖品基数
	 */
	@ApiModelProperty(value = "奖品基数")
	private Integer baseNum;

	/**
	 * 奖品最多被抽到的次数
	 */
	@ApiModelProperty(value = "奖品最多被抽到的次数")
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
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 创建人Id
	 */
	@ApiModelProperty(value = "创建人Id")
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
	private Long showUserId;

	/**
	 * 上架时间
	 */
	@ApiModelProperty(value = "上架时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date showTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

}