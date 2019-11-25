package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 区域表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-25 19:09:47
 */
@Table(name = "tb_region")
@Entity
@Data
public class Region implements Serializable {

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
	 * 名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 父级编码
	 */
	@Column(name = "parent_code")
	private String parentCode;

	/**
	 * 编码
	 */
	@Column(name = "code")
	private String code;

	/**
	 * 是否可用
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

	/**
	 * 创建日期
	 */
	@Column(name = "create_time")
	private Date createTime;

}