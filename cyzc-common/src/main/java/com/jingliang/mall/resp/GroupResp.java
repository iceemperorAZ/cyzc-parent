package com.jingliang.mall.resp;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
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
@ApiModel(name = "GroupResp", description = "组")
public class
GroupResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiProperty(description = "主键Id")
	@JsonSerialize(using = ToStringSerializer.class)
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
	@JsonSerialize(using = ToStringSerializer.class)
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