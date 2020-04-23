package com.jingliang.mall.resp;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

/**
 * 组
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 15:33:55
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class GroupResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 组名称
	 */
	private String groupName;

	/**
	 * 父组Id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentGroupId;

	/**
	 * 是否可用 0：否，1：是
	 */
	private Boolean isAvailable;

	/**
	 * 组编码
	 */
	private String groupNo;

	/**
	 * 是否有子节点
	 */
	private Boolean child;

}