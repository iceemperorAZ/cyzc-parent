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
 * @date 2020-04-16 11:38:50
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
	 * 是否可用 0：否，1：是
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

}