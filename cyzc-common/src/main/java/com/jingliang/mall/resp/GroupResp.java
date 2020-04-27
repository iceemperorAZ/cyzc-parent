package com.jingliang.mall.resp;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 组
 * 返回参数
 * @author Mengde Liu
 * @version 1.0.0
 * @date 2020-04-24 11:18:08
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(value = "GroupResp", description = "组")
public class
GroupResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiModelProperty(value = "主键Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 组名称
	 */
	@ApiModelProperty(value = "组名称")
	private String groupName;

	/**
	 * 父组Id
	 */
	@ApiModelProperty(value = "父组Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentGroupId;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

	/**
	 * 组编码
	 */
	@ApiModelProperty(value = "组编码")
	private String groupNo;

	/**
	 * 是否有子节点
	 */
	@ApiModelProperty(value = "是否有子节点")
	private Boolean child;

}