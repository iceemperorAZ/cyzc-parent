package com.jingliang.mall.entity;

import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * 组
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 15:33:55
 */
@Table(name = "tb_group")
@Entity
@Data
public class Group implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "com.jingliang.mall.common.IdGenerator")
	@Id
	private Long id;

	/**
	 * 组名称
	 */
	@Column(name = "group_name")
	private String groupName;

	/**
	 * 父组Id
	 */
	@Column(name = "parent_group_id")
	private Long parentGroupId;

	/**
	 * 是否可用 0：否，1：是
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

	/**
	 * 组编码
	 */
	@Column(name = "group_no")
	private String groupNo;

	/**
	 * 是否有子节点
	 */
	@Column(name = "child")
	private Boolean child;

}