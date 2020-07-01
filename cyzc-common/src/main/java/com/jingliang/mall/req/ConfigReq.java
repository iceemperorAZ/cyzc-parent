package com.jingliang.mall.req;

import java.util.Date;
import lombok.Data;
import com.citrsw.annatation.ApiModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import com.citrsw.annatation.ApiProperty;
import java.io.Serializable;

/**
 * 配置文件
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-03 15:59:18
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(name = "ConfigReq", description = "配置文件")
@Data
public class ConfigReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiProperty(description = "主键")
	private Long id;

	/**
	 * 名称
	 */
	@ApiProperty(description = "名称")
	private String name;

	/**
	 * 值(多个之间用,分割)
	 */
	@ApiProperty(description = "值(多个之间用,分割)")
	private String configValues;

	/**
	 * 描述
	 */
	@ApiProperty(description = "描述")
	private String remark;

	/**
	 * 创建人Id
	 */
	@ApiProperty(description = "创建人Id")
	private Long createUserId;

	/**
	 * 创建人Id-开始
	 */
	@ApiProperty(description = "创建人Id-开始")
	private Long createUserIdStart;

	/**
	 * 创建人Id-结束
	 */
	@ApiProperty(description = "创建人Id-结束")
	private Long createUserIdEnd;

	/**
	 * 创建时间
	 */
	@ApiProperty(description = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 创建时间-开始
	 */
	@ApiProperty(description = "创建时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeStart;

	/**
	 * 创建时间-结束
	 */
	@ApiProperty(description = "创建时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeEnd;

	/**
	 * 更新人Id
	 */
	@ApiProperty(description = "更新人Id")
	private Long updateUserId;

	/**
	 * 更新人Id-开始
	 */
	@ApiProperty(description = "更新人Id-开始")
	private Long updateUserIdStart;

	/**
	 * 更新人Id-结束
	 */
	@ApiProperty(description = "更新人Id-结束")
	private Long updateUserIdEnd;

	/**
	 * 更新时间
	 */
	@ApiProperty(description = "更新时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 更新时间-开始
	 */
	@ApiProperty(description = "更新时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeStart;

	/**
	 * 更新时间-结束
	 */
	@ApiProperty(description = "更新时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeEnd;
}