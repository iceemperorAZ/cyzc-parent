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
 * 商户联系记录
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-01-15 10:17:00
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(name = "ContactRecordResp", description = "商户联系记录")
@Data
public class ContactRecordResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiProperty(description = "主键Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 商户Id
	 */
	@ApiProperty(description = "商户Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long buyerId;

	/**
	 * 联系结果
	 */
	@ApiProperty(description = "联系结果")
	private Integer result;

	/**
	 * 备注
	 */
	@ApiProperty(description = "备注")
	private String remark;

	/**
	 * 销售Id
	 */
	@ApiProperty(description = "销售Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long salerId;

	/**
	 * 联系时间(创建时间)
	 */
	@ApiProperty(description = "联系时间(创建时间)")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 创建人
	 */
	@ApiProperty(description = "创建人")
	private String createUserName;

	/**
	 * 创建人Id
	 */
	@ApiProperty(description = "创建人Id")
	private Long createUserId;

	/**
	 * 是否可用
	 */
	@ApiProperty(description = "是否可用")
	private Boolean isAvailable;

}