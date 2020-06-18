package com.jingliang.mall.entity;

import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
import java.util.Date;

/**
 * 保存地图详情信息
 * 
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-05-29 09:19:03
 */
@Entity
@Table(name = "tb_map_detail")
@Data
public class MapDetail implements Serializable {

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
	 * 所属编号
	 */
	@Column(name = "map_no")
	private String mapNo;

	/**
	 * 经度
	 */
	@Column(name = "longitude")
	private Double longitude;

	/**
	 * 纬度
	 */
	@Column(name = "latitude")
	private Double latitude;

	/**
	 * 地址详情
	 */
	@Column(name = "address_detail")
	private String addressDetail;

	/**
	 * 是否可用 0：否，1：是
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

	/**
	 * 点位序号
	 */
	@Column(name = "point_num")
	private Integer pointNum;
}