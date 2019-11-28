package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-03 19:58:43
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "RoleResp", description = "角色表")
@Data
public class RoleResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiModelProperty(value = "主键Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 角色名称(必须以ROLE_起始命名)
	 */
	@ApiModelProperty(value = "角色名称(必须以ROLE_起始命名)")
	private String roleName;

	/**
	 * 角色名称中文
	 */
	@ApiModelProperty(value = "角色名称中文")
	private String roleNameZh;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

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
	@JsonSerialize(using = ToStringSerializer.class)
	private Long createUserId;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updateUserName;

	/**
	 * 修改人Id
	 */
	@ApiModelProperty(value = "修改人Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long updateUserId;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 当前用户是否拥有此角色
	 */
	@ApiModelProperty(value = "当前用户是否拥有此角色")
	private Boolean isHave = false;
}