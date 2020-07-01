package com.jingliang.mall.req;

import java.util.Date;
import lombok.Data;
import com.citrsw.annatation.ApiModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.citrsw.annatation.ApiProperty;
import java.io.Serializable;

/**
 * 不规范商铺名称合集
 * 
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-06-11 15:45:35
 */
@Data
@ApiModel(name = "UnavailableNameReq", description = "不规范商铺名称合集")
public class UnavailableNameReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiProperty(description = "主键")
	private Long id;

	/**
	 * 不规范名称
	 */
	@ApiProperty(description = "不规范名称")
	private String name;

	/**
	 * 创建时间
	 */
	@ApiProperty(description = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 是否可用：0否1是
	 */
	@ApiProperty(description = "是否可用：0否1是")
	private String isAvailable;
}