package com.jingliang.mall.entity;

import java.util.Date;
import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * 组与区域映射关系表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-16 11:38:50
 */
@Entity
@Data
@Table(name = "tb_group_region")
public class GroupRegion implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "com.jingliang.mall.common.IdGenerator")
	@Id
	private Long id;

	/**
	 * 组Id
	 */
	@Column(name = "group_id")
	private Long groupId;

	/**
	 * 区域Id
	 */
	@Column(name = "region_id")
	private Long regionId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

}