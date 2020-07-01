package com.jingliang.mall.req;

import lombok.Data;
import com.citrsw.annatation.ApiModel;
import lombok.EqualsAndHashCode;
import com.citrsw.annatation.ApiProperty;
import java.io.Serializable;

/**
 * 组
 * 请求参数
 * @author Mengde Liu
 * @version 1.0.0
 * @date 2020-04-24 11:18:08
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(name = "GroupReq", description = "组")
@Data
public class GroupReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiProperty(description = "主键Id")
	private Long id;

	/**
	 * 组名称
	 */
	@ApiProperty(description = "组名称")
	private String groupName;

	/**
	 * 父组Id
	 */
	@ApiProperty(description = "父组Id")
	private Long parentGroupId;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiProperty(description = "是否可用 0：否，1：是")
	private Boolean isAvailable;

	/**
	 * 组编码
	 */
	@ApiProperty(description = "组编码")
	private String groupNo;

	/**
	 * 是否有子节点
	 */
	@ApiProperty(description = "是否有子节点")
	private Boolean child;

}