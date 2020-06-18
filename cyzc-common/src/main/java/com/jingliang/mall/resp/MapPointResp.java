package com.jingliang.mall.resp;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 保存地图信息
 * 
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-05-29 09:19:03
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "MapPointResp", description = "保存地图信息")
@Data
public class MapPointResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 编号
	 */
	@ApiModelProperty(value = "编号")
	private String mapNo;

	/**
	 * 地图名称
	 */
	@ApiModelProperty(value = "地图名称")
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