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
 * 商户联系记录
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-01-15 10:17:00
 */
@ApiModel(value = "ContactRecordReq", description = "商户联系记录")
@EqualsAndHashCode(callSuper = true)
@Data
public class ContactRecordReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiModelProperty(value = "主键Id")
	private Long id;

	/**
	 * 商户Id
	 */
	@ApiModelProperty(value = "商户Id")
	private Long buyerId;

	/**
	 * 联系结果
	 */
	@ApiModelProperty(value = "联系结果")
	private Integer result;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

	/**
	 * 销售Id
	 */
	@ApiModelProperty(value = "销售Id")
	private Long salerId;

	/**
	 * 联系时间(创建时间)
	 */
	@ApiModelProperty(value = "联系时间(创建时间)")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createUserName;

	/**
	 * 创建人Id
	 */
	@ApiModelProperty(value = "创建人Id")
	private Long createUserId;

	/**
	 * 是否可用
	 */
	@ApiModelProperty(value = "是否可用")
	private Boolean isAvailable;

}