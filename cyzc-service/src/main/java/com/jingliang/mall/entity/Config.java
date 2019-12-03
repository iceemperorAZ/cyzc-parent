package com.jingliang.mall.entity;

import java.util.Date;
import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * 配置文件
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-03 15:59:18
 */
@Entity
@Data
@Table(name = "tb_config")
public class Config implements Serializable {

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
	 * 编码
	 */
	@Column(name = "code")
	private String code;

	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 值(多个之间用,分割)
	 */
	@Column(name = "config_values")
	private String configValues;

	/**
	 * 描述
	 */
	@Column(name = "remark")
	private String remark;

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
	 * 更新人Id
	 */
	@Column(name = "update_user_id")
	private Long updateUserId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 是否可用
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

}