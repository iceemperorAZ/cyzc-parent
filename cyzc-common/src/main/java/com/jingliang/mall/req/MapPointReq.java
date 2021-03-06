package com.jingliang.mall.req;

import lombok.Data;
import com.citrsw.annatation.ApiModel;
import lombok.EqualsAndHashCode;
import com.citrsw.annatation.ApiProperty;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 保存地图信息
 * 
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-05-29 09:19:03
 */
@ApiModel(name = "MapPointReq", description = "保存地图信息")
@EqualsAndHashCode(callSuper = true)
@Data
public class MapPointReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiProperty(description = "主键")
	private Long id;

	/**
	 * 编号
	 */
	@ApiProperty(description = "编号")
	private String mapNo;

	/**
	 * 地图名称
	 */
	@ApiProperty(description = "地图名称")
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