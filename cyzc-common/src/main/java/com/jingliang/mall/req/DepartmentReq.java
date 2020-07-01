package com.jingliang.mall.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 14:12:41
 */
@ApiModel(name = "DepartmentReq", description = "部门")
@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiProperty(description = "主键")
	private Long id;

	/**
	 * 主键-开始
	 */
	@ApiProperty(description = "主键-开始")
	private Long idStart;

	/**
	 * 主键-结束
	 */
	@ApiProperty(description = "主键-结束")
	private Long idEnd;

	/**
	 * 组织机构Id预留字段  暂时设置为-1
	 */
	@ApiProperty(description = "组织机构Id预留字段  暂时设置为-1")
	private Long orgId;

	/**
	 * 组织机构Id预留字段  暂时设置为-1-开始
	 */
	@ApiProperty(description = "组织机构Id预留字段  暂时设置为-1-开始")
	private Long orgIdStart;

	/**
	 * 组织机构Id预留字段  暂时设置为-1-结束
	 */
	@ApiProperty(description = "组织机构Id预留字段  暂时设置为-1-结束")
	private Long orgIdEnd;

	/**
	 * 部门编号
	 */
	@ApiProperty(description = "部门编号")
	private String departmentNo;

	/**
	 * 部门名称
	 */
	@ApiProperty(description = "部门名称")
	private String departmentName;

	/**
	 * 是否可用
	 */
	@ApiProperty(description = "是否可用")
	private Boolean isAvailable;

	/**
	 * 创建人
	 */
	@ApiProperty(description = "创建人")
	private String createUserName;

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
	 * 修改人
	 */
	@ApiProperty(description = "修改人")
	private String updateUserName;

	/**
	 * 修改人Id
	 */
	@ApiProperty(description = "修改人Id")
	private Long updateUserId;

	/**
	 * 修改人Id-开始
	 */
	@ApiProperty(description = "修改人Id-开始")
	private Long updateUserIdStart;

	/**
	 * 修改人Id-结束
	 */
	@ApiProperty(description = "修改人Id-结束")
	private Long updateUserIdEnd;

	/**
	 * 修改时间
	 */
	@ApiProperty(description = "修改时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 修改时间-开始
	 */
	@ApiProperty(description = "修改时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeStart;

	/**
	 * 修改时间-结束
	 */
	@ApiProperty(description = "修改时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeEnd;

}