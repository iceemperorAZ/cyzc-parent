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
 * 属性分类表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 16:53:10
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AttributeTypeReq", description = "属性分类")
@Data
public class AttributeTypeReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * 主键-开始
	 */
	@ApiModelProperty(value = "主键-开始")
	private Long idStart;

	/**
	 * 主键-结束
	 */
	@ApiModelProperty(value = "主键-结束")
	private Long idEnd;

	/**
	 * 商品分类Id
	 */
	@ApiModelProperty(value = "商品分类Id")
	private Long productTypeId;

	/**
	 * 商品分类Id-开始
	 */
	@ApiModelProperty(value = "商品分类Id-开始")
	private Long productTypeIdStart;

	/**
	 * 商品分类Id-结束
	 */
	@ApiModelProperty(value = "商品分类Id-结束")
	private Long productTypeIdEnd;

	/**
	 * 属性分类名称
	 */
	@ApiModelProperty(value = "属性分类名称")
	private String attributeTypeName;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
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