package com.jingliang.mall.resp;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import com.citrsw.annatation.ApiModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.citrsw.annatation.ApiProperty;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 保存地图详情信息
 * 
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-05-29 09:19:03
 */
@ApiModel(name = "MapDetailResp", description = "保存地图详情信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MapDetailResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiProperty(description = "主键")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 所属编号
	 */
	@ApiProperty(description = "所属编号")
	private String mapNo;

	/**
	 * 经度
	 */
	@ApiProperty(description = "经度")
	private Double longitude;

	/**
	 * 纬度
	 */
	@ApiProperty(description = "纬度")
	private Double latitude;

	/**
	 * 地址详情
	 */
	@ApiProperty(description = "地址详情")
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