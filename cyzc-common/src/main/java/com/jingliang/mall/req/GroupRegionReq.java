package com.jingliang.mall.req;

import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 组与区域映射关系表
 * 
 * @author Mengde Liu
 * @version 1.0.0
 * @date 2020-04-24 11:39:41
 */
@ApiModel(value = "GroupRegionReq", description = "组与区域映射关系表")
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupRegionReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@ApiModelProperty(value = "主键ID")
	private Long id;

	/**
	 * 组Id
	 */
	@ApiModelProperty(value = "组Id")
	private Long groupId;

	/**
	 * 区域Id
	 */
	@ApiModelProperty(value = "区域Id")
	private Long regionId;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

}