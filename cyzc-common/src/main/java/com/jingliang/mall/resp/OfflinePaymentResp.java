package com.jingliang.mall.resp;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;
import java.util.Date;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.citrsw.annatation.ApiModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.citrsw.annatation.ApiProperty;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 线下支付
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-06 15:30:50
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(name = "OfflinePaymentResp", description = "线下支付")
public class OfflinePaymentResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiProperty(description = "主键Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 订单Id
	 */
	@ApiProperty(description = "订单Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long orderId;

	/**
	 * 订单编号
	 */
	@ApiProperty(description = "订单编号")
	private String orderNo;

	/**
	 * 比如：xx银行，微信，支付宝，等等
	 */
	@ApiProperty(description = "比如：xx银行，微信，支付宝，等等")
	private String payWay;

	/**
	 * 凭证urls,多个之间用,分割(最多5张)
	 */
	@ApiProperty(description = "凭证urls,多个之间用,分割(最多5张)")
	private String urls;
	/**
	 * 凭证urls,多个之间用,分割(最多5张)
	 */
	@ApiProperty(description = "凭证urls,多个之间用,分割(最多5张)")
	private List<String> imgUrls;

	/**
	 * 备注
	 */
	@ApiProperty(description = "备注")
	private String remark;

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

	public List<String> getImgUrls() {
		return Objects.isNull(urls)?null: Arrays.asList(urls.split(";"));
	}
}