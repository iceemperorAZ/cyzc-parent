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
 * 库存记录单
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-11 11:56:20
 */
@ApiModel(value = "SkuRecordResp", description = "库存记录单")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SkuRecordResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiModelProperty(value = "主键Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String productName;

	/**
	 * 商品Id
	 */
	@ApiModelProperty(value = "商品Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long productId;

	/**
	 * 库存Id
	 */
	@ApiModelProperty(value = "库存Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

	/**
	 * 库存编号
	 */
	@ApiModelProperty(value = "库存编号")
	private String skuNo;

	/**
	 * 库存详情Id
	 */
	@ApiModelProperty(value = "库存详情Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuDetailId;

	/**
	 * 库存详情批号
	 */
	@ApiModelProperty(value = "库存详情批号")
	private String batchNum;

	/**
	 * 类型 100：入库，200：出库
	 */
	@ApiModelProperty(value = "类型 100：入库，200：出库")
	private Integer type;

	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述")
	private String content;

	/**
	 * 数量
	 */
	@ApiModelProperty(value = "数量")
	private Integer num;

	/**
	 * 状态  100：待确认，200：通过，300：驳回
	 */
	@ApiModelProperty(value = "状态  100：待确认，200：通过，300：驳回")
	private Integer status;

	/**
	 * 审批意见
	 */
	@ApiModelProperty(value = "审批意见")
	private String approveOpinion;

	/**
	 * 审批人Id
	 */
	@ApiModelProperty(value = "审批人Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long approveUserId;

	/**
	 * 审批人
	 */
	@ApiModelProperty(value = "审批人")
	private String approveUserName;

	/**
	 * 审批时间
	 */
	@ApiModelProperty(value = "审批时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date approveTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createUserName;

	/**
	 * 创建人Id
	 */
	@ApiModelProperty(value = "创建人Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long createUserId;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updateUserName;

	/**
	 * 修改人Id
	 */
	@ApiModelProperty(value = "修改人Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long updateUserId;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

}