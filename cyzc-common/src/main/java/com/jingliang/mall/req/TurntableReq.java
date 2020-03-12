package com.jingliang.mall.req;

import java.time.LocalDateTime;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import com.jingliang.mall.req.BaseReq;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

/**
 * 转盘
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
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
	 * 转盘图片
	 */
	@ApiModelProperty(value = "转盘图片")
	private String img;

	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述")
	private String remark;

	/**
	 * 转盘单次抽取需要的金币
	 */
	@ApiModelProperty(value = "转盘单次抽取需要的金币")
	private Integer gold;

	/**
	 * 转盘单次抽取需要的金币-开始
	 */
	@ApiModelProperty(value = "转盘单次抽取需要的金币-开始")
	private Integer goldStart;

	/**
	 * 转盘单次抽取需要的金币-结束
	 */
	@ApiModelProperty(value = "转盘单次抽取需要的金币-结束")
	private Integer goldEnd;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private Date updateTime;

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
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

}