package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 属性表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 16:53:10
 */
@Entity
@Table(name = "tb_attribute_type_detail")
@Data
public class AttributeTypeDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "com.jingliang.mall.common.IdGenerator")
	@Id
	private Long id;

	/**
	 * 属性分类Id
	 */
	@Column(name = "attribute_type_id")
	private Long attributeTypeId;

	/**
	 * 属性名称
	 */
	@Column(name = "attribute_name")
	private String attributeName;

	/**
	 * 是否可用 0：否，1：是
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

	/**
	 * 创建人
	 */
	@Column(name = "create_user_name")
	private String createUserName;

	/**
	 * 创建人Id
	 */
	@Column(name = "create_user_Id")
	private Long createUserId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 修改人
	 */
	@Column(name = "update_user_name")
	private String updateUserName;

	/**
	 * 修改人Id
	 */
	@Column(name = "update_user_id")
	private Long updateUserId;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

}