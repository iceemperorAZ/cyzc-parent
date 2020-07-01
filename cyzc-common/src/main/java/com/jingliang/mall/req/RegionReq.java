package com.jingliang.mall.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
@Data
public class RegionReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiProperty(description = "主键Id")
	private Long id;

	/**
	 * 主键Id-开始
	 */
	@ApiProperty(description = "主键Id-开始")
	private Long idStart;

	/**
	 * 主键Id-结束
	 */
	@ApiProperty(description = "主键Id-结束")
	private Long idEnd;

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

	/**
	 * 创建日期-开始
	 */
	@ApiProperty(description = "创建日期-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeStart;

	/**
	 * 创建日期-结束
	 */
	@ApiProperty(description = "创建日期-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeEnd;

}