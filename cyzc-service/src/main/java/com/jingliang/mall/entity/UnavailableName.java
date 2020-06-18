package com.jingliang.mall.entity;

import java.util.Date;
import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
/**
 * 不规范商铺名称合集
 * 
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-06-11 15:45:35
 */
@Entity
@Data
@Table(name = "tb_unavailable_name")
public class UnavailableName implements Serializable {

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
	 * 不规范名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 是否可用：0否1是
	 */
	@Column(name = "is_available")
	private String isAvailable;
}