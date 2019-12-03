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
 * 配置文件
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-03 15:59:18
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ConfigReq", description = "配置文件")
@Data
public class ConfigReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String name;

	/**
	 * 值(多个之间用,分割)
	 */
	@ApiModelProperty(value = "值(多个之间用,分割)")
	private String configValues;

	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述")
	private String remark;

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
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 创建时间-开始
	 */
	@ApiModelProperty(value = "创建时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeStart;

	/**
	 * 创建时间-结束
	 */
	@ApiModelProperty(value = "创建时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeEnd;

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