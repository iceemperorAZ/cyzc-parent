package com.jingliang.mall.entity;

import java.util.Date;
import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * 区域表
 * 
 * @author Mengde Liu
 * @version 1.0.0
 * @date 2020-04-24 11:39:41
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
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

}