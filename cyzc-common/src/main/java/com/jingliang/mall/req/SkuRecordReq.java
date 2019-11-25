package com.jingliang.mall.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@ApiModel(value = "SkuRecordReq", description = "库存记录单")
@EqualsAndHashCode(callSuper = true)
@Data
public class SkuRecordReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiModelProperty(value = "主键Id")
	private Long id;

	/**
	 * 主键Id-开始
	 */
	@ApiModelProperty(value = "主键Id-开始")
	private Long idStart;

	/**
	 * 主键Id-结束
	 */
	@ApiModelProperty(value = "主键Id-结束")
	private Long idEnd;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String productName;

	/**
	 * 商品Id
	 */
	@ApiModelProperty(value = "商品Id")
	private Long productId;

	/**
	 * 商品Id-开始
	 */
	@ApiModelProperty(value = "商品Id-开始")
	private Long productIdStart;

	/**
	 * 商品Id-结束
	 */
	@ApiModelProperty(value = "商品Id-结束")
	private Long productIdEnd;

	/**
	 * 库存Id
	 */
	@ApiModelProperty(value = "库存Id")
	private Long skuId;

	/**
	 * 库存Id-开始
	 */
	@ApiModelProperty(value = "库存Id-开始")
	private Long skuIdStart;

	/**
	 * 库存Id-结束
	 */
	@ApiModelProperty(value = "库存Id-结束")
	private Long skuIdEnd;

	/**
	 * 库存编号
	 */
	@ApiModelProperty(value = "库存编号")
	private String skuNo;

	/**
	 * 库存详情Id
	 */
	@ApiModelProperty(value = "库存详情Id")
	private Long skuDetailId;

	/**
	 * 库存详情Id-开始
	 */
	@ApiModelProperty(value = "库存详情Id-开始")
	private Long skuDetailIdStart;

	/**
	 * 库存详情Id-结束
	 */
	@ApiModelProperty(value = "库存详情Id-结束")
	private Long skuDetailIdEnd;

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
	 * 类型 100：入库，200：出库-开始
	 */
	@ApiModelProperty(value = "类型 100：入库，200：出库-开始")
	private Integer typeStart;

	/**
	 * 类型 100：入库，200：出库-结束
	 */
	@ApiModelProperty(value = "类型 100：入库，200：出库-结束")
	private Integer typeEnd;

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
	 * 数量-开始
	 */
	@ApiModelProperty(value = "数量-开始")
	private Integer numStart;

	/**
	 * 数量-结束
	 */
	@ApiModelProperty(value = "数量-结束")
	private Integer numEnd;

	/**
	 * 状态  100：待确认，200：通过，300：驳回
	 */
	@ApiModelProperty(value = "状态  100：待确认，200：通过，300：驳回")
	private Integer status;

	/**
	 * 状态  100：待确认，200：通过，300：驳回-开始
	 */
	@ApiModelProperty(value = "状态  100：待确认，200：通过，300：驳回-开始")
	private Integer statusStart;

	/**
	 * 状态  100：待确认，200：通过，300：驳回-结束
	 */
	@ApiModelProperty(value = "状态  100：待确认，200：通过，300：驳回-结束")
	private Integer statusEnd;

	/**
	 * 审批意见
	 */
	@ApiModelProperty(value = "审批意见")
	private String approveOpinion;

	/**
	 * 审批人Id
	 */
	@ApiModelProperty(value = "审批人Id")
	private Long approveUserId;

	/**
	 * 审批人Id-开始
	 */
	@ApiModelProperty(value = "审批人Id-开始")
	private Long approveUserIdStart;

	/**
	 * 审批人Id-结束
	 */
	@ApiModelProperty(value = "审批人Id-结束")
	private Long approveUserIdEnd;

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
	 * 审批时间-开始
	 */
	@ApiModelProperty(value = "审批时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date approveTimeStart;

	/**
	 * 审批时间-结束
	 */
	@ApiModelProperty(value = "审批时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date approveTimeEnd;

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
	private Long createUserId;

	/**
	 * 创建人Id-开始
	 */
	@ApiModelProperty(value = "创建人Id-开始")
	private Long createUserIdStart;

	/**
	 * 创建人Id-结束
	 */
	@ApiModelProperty(value = "创建人Id-结束")
	private Long createUserIdEnd;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 创建时间-开始
	 */
	@ApiModelProperty(value = "创建时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeStart;

	/**
	 * 创建时间-结束
	 */
	@ApiModelProperty(value = "创建时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeEnd;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updateUserName;

	/**
	 * 修改人Id
	 */
	@ApiModelProperty(value = "修改人Id")
	private Long updateUserId;

	/**
	 * 修改人Id-开始
	 */
	@ApiModelProperty(value = "修改人Id-开始")
	private Long updateUserIdStart;

	/**
	 * 修改人Id-结束
	 */
	@ApiModelProperty(value = "修改人Id-结束")
	private Long updateUserIdEnd;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 修改时间-开始
	 */
	@ApiModelProperty(value = "修改时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeStart;

	/**
	 * 修改时间-结束
	 */
	@ApiModelProperty(value = "修改时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeEnd;

}