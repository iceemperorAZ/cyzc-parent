package com.jingliang.mall.req;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 组
 * 请求参数
 * @author Mengde Liu
 * @version 1.0.0
 * @date 2020-04-24 11:18:08
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "GroupReq", description = "组")
@Data
public class GroupReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiModelProperty(value = "主键Id")
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