package com.jingliang.mall.req;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 组
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 15:33:55
 */
@Data
public class GroupReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	private Long id;

	/**
	 * 组名称
	 */
	private String groupName;

	/**
	 * 父组Id
	 */
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