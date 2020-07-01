package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 区域表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-25 19:09:47
 */
@ApiModel(name = "Region", description = "区域")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RegionResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiProperty(description = "主键Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 名称
	 */
	@ApiProperty(description = "名称")
	private String name;

	/**
	 * 父级编码
	 */
	@ApiProperty(description = "父级编码")
	private String parentCode;

	/**
	 * 编码
	 */
	@ApiProperty(description = "编码")
	private String code;

	/**
	 * 是否可用
	 */
	@ApiProperty(description = "是否可用")
	private Boolean isAvailable;

	/**
	 * 创建日期
	 */
	@ApiProperty(description = "创建日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

}