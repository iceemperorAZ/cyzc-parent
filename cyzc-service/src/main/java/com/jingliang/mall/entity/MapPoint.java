package com.jingliang.mall.entity;

import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
import java.util.Date;

/**
 * 保存地图信息
 * 
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-05-29 09:19:03
 */
@Entity
@Table(name = "tb_map_point")
@Data
public class MapPoint implements Serializable {

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
	 * 编号
	 */
	@Column(name = "map_no")
	private String mapNo;

	/**
	 * 地图名称
	 */
	@Column(name = "map_name")
	private String mapName;

	/**
	 * 是否可用 0：否，1：是
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;
}