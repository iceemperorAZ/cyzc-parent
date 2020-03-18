package com.jingliang.mall.req;

import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jingliang.mall.req.BaseReq;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;

/**
 * 转盘日志
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-17 10:04:21
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TurntableLogReq", description = "转盘日志")
@Data
public class TurntableLogReq extends BaseReq implements Serializable {

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
	 * 消息内容
	 */
	@ApiModelProperty(value = "消息内容")
	private String msg;

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