package com.jingliang.mall.req;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 保存地图详情信息
 * 
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-05-29 09:19:03
 */
@ApiModel(value = "MapDetailReq", description = "保存地图详情信息")
@EqualsAndHashCode(callSuper = true)
@Data
public class MapDetailReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * 所属编号
	 */
	@ApiModelProperty(value = "所属编号")
	private String mapNo;

	/**
	 * 经度
	 */
	@ApiModelProperty(value = "经度")
	private Double longitude;

	/**
	 * 纬度
	 */
	@ApiModelProperty(value = "纬度")
	private Double latitude;

	/**
	 * 地址详情
	 */
	@ApiModelProperty(value = "地址详情")
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