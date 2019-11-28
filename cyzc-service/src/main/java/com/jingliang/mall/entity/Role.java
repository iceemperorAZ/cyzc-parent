package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-03 19:58:43
 */
@Entity
@Data
@Table(name = "tb_role")
public class Role implements Serializable {

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
	 * 角色名称(必须以ROLE_起始命名)
	 */
	@Column(name = "role_name")
	private String roleName;

	/**
	 * 角色名称中文
	 */
	@Column(name = "role_name_zh")
	private String roleNameZh;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 是否可用
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
	@Column(name = "create_user_id")
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