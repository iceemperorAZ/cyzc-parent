package com.jingliang.mall.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色-资源关联表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-03 19:58:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "RoleMenuReq", description = "角色-资源关联表")
public class RoleMenuReq extends BaseReq implements Serializable {

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
	 * 角色Id
	 */
	@ApiModelProperty(value = "角色Id")
	private Long roleId;

	/**
	 * 角色Id-开始
	 */
	@ApiModelProperty(value = "角色Id-开始")
	private Long roleIdStart;

	/**
	 * 角色Id-结束
	 */
	@ApiModelProperty(value = "角色Id-结束")
	private Long roleIdEnd;

	/**
	 * 菜单Id
	 */
	@ApiModelProperty(value = "菜单Id")
	private Long menuId;

	/**
	 * 菜单Id-开始
	 */
	@ApiModelProperty(value = "菜单Id-开始")
	private Long menuIdStart;

	/**
	 * 菜单Id-结束
	 */
	@ApiModelProperty(value = "菜单Id-结束")
	private Long menuIdEnd;

	/**
	 * 是否可用
	 */
	@ApiModelProperty(value = "是否可用")
	private Boolean isAvailable;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createUserName;

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
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updateUserName;

	/**
	 * 修改人Id
	 */
	@ApiModelProperty(value = "修改人Id")
	private Long updateUserId;

	/**
	 * 修改人Id-开始
	 */
	@ApiModelProperty(value = "修改人Id-开始")
	private Long updateUserIdStart;

	/**
	 * 修改人Id-结束
	 */
	@ApiModelProperty(value = "修改人Id-结束")
	private Long updateUserIdEnd;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 修改时间-开始
	 */
	@ApiModelProperty(value = "修改时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeStart;

	/**
	 * 修改时间-结束
	 */
	@ApiModelProperty(value = "修改时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeEnd;

}