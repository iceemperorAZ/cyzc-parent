package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 历史搜索表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-05 12:21:02
 */
@ApiModel(value = "SearchHistory", description = "历史搜索")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SearchHistoryResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiModelProperty(value = "主键Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 搜索词
	 */
	@ApiModelProperty(value = "搜索词")
	private String keyword;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

	/**
	 * 用户Id（创建人Id）
	 */
	@ApiModelProperty(value = "用户Id（创建人Id）")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long buyerId;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

}