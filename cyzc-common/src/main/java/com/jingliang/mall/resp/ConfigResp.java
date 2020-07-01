package com.jingliang.mall.resp;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.citrsw.annatation.ApiModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.citrsw.annatation.ApiProperty;
import java.io.Serializable;

/**
 * 配置文件
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-03 15:59:18
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(name = "ConfigResp", description = "配置文件")
public class ConfigResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiProperty(description = "主键")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 编码
	 */
	@ApiProperty(description = "编码")
	private String code;

	/**
	 * 名称
	 */
	@ApiProperty(description = "名称")
	private String name;

	/**
	 * 值(多个之间用,分割)
	 */
	@ApiProperty(description = "值(多个之间用,分割)")
	private String configValues;

	/**
	 * 描述
	 */
	@ApiProperty(description = "描述")
	private String remark;

	/**
	 * 创建人Id
	 */
	@ApiProperty(description = "创建人Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long createUserId;

	/**
	 * 创建时间
	 */
	@ApiProperty(description = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 更新人Id
	 */
	@ApiProperty(description = "更新人Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long updateUserId;

	/**
	 * 更新时间
	 */
	@ApiProperty(description = "更新时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 是否可用
	 */
	@ApiProperty(description = "是否可用")
	private Boolean isAvailable;

}