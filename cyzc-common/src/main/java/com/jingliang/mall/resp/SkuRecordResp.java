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
 * 库存记录单
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-11 11:56:20
 */
@ApiModel(name = "SkuRecordResp", description = "库存记录单")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SkuRecordResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiProperty(description = "主键Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 商品名称
	 */
	@ApiProperty(description = "商品名称")
	private String productName;

	/**
	 * 商品Id
	 */
	@ApiProperty(description = "商品Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long productId;

	/**
	 * 库存Id
	 */
	@ApiProperty(description = "库存Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

	/**
	 * 库存编号
	 */
	@ApiProperty(description = "库存编号")
	private String skuNo;

	/**
	 * 库存详情Id
	 */
	@ApiProperty(description = "库存详情Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuDetailId;

	/**
	 * 库存详情批号
	 */
	@ApiProperty(description = "库存详情批号")
	private String batchNum;

	/**
	 * 类型 100：入库，200：出库
	 */
	@ApiProperty(description = "类型 100：入库，200：出库")
	private Integer type;

	/**
	 * 描述
	 */
	@ApiProperty(description = "描述")
	private String content;

	/**
	 * 数量
	 */
	@ApiProperty(description = "数量")
	private Integer num;

	/**
	 * 状态  100：待确认，200：通过，300：驳回
	 */
	@ApiProperty(description = "状态  100：待确认，200：通过，300：驳回")
	private Integer status;

	/**
	 * 审批意见
	 */
	@ApiProperty(description = "审批意见")
	private String approveOpinion;

	/**
	 * 审批人Id
	 */
	@ApiProperty(description = "审批人Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long approveUserId;

	/**
	 * 审批人
	 */
	@ApiProperty(description = "审批人")
	private String approveUserName;

	/**
	 * 审批时间
	 */
	@ApiProperty(description = "审批时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date approveTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiProperty(description = "是否可用 0：否，1：是")
	private Boolean isAvailable;

	/**
	 * 创建人
	 */
	@ApiProperty(description = "创建人")
	private String createUserName;

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
	 * 修改人
	 */
	@ApiProperty(description = "修改人")
	private String updateUserName;

	/**
	 * 修改人Id
	 */
	@ApiProperty(description = "修改人Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long updateUserId;

	/**
	 * 修改时间
	 */
	@ApiProperty(description = "修改时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

}