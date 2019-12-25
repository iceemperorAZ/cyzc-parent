package com.jingliang.mall.resp;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 优惠券
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-18 16:50:49
 */
@ApiModel(value = "CouponResp", description = "优惠券")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CouponResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 商品区Id
	 */
	@ApiModelProperty(value = "商品区Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long productZoneId;

	/**
	 * 使用范围（商品分类）
	 */
	@ApiModelProperty(value = "使用范围（商品分类）")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long productTypeId;

	/**
	 * 优惠百分比
	 */
	@ApiModelProperty(value = "优惠百分比")
	private Integer percentage;

	/**
	 * 可领取数
	 */
	@ApiModelProperty(value = "可领取数")
	private Integer receiveNum;

	/**
	 * 优惠券剩余数量
	 */
	@ApiModelProperty(value = "优惠券剩余数量")
	private Integer residueNumber;

	/**
	 * 优惠券总数量
	 */
	@ApiModelProperty(value = "优惠券总数量")
	private Integer totalNumber;

	/**
	 * 优惠券类型  100:超级会员,200:普通用户，300:新建用户
	 */
	@ApiModelProperty(value = "优惠券类型  100:超级会员,200:普通用户，300:新建用户")
	private Integer couponType;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTime;

	/**
	 * 到期时间
	 */
	@ApiModelProperty(value = "到期时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date expirationTime;

	/**
	 * 优惠券描述
	 */
	@ApiModelProperty(value = "优惠券描述")
	private String couponDescribe;

	/**
	 * 是否发布 0：否，1：是
	 */
	@ApiModelProperty(value = "是否发布 0：否，1：是")
	private Boolean isRelease;

	/**
	 * 发放开始时间
	 */
	@ApiModelProperty(value = "发放开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date provideStartTime;

	/**
	 * 发放结束时间
	 */
	@ApiModelProperty(value = "发放结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date provideEndTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 创建人名称
	 */
	@ApiModelProperty(value = "创建人名称")
	private String createUserName;

	/**
	 * 创建人Id
	 */
	@ApiModelProperty(value = "创建人Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long createUserId;

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

	/**
	 * 是否已领取
	 */
	@ApiModelProperty(value = "是否已领取")
	private boolean isReceive;

	public void setIsReceive(boolean isReceive) {
        this.isReceive = isReceive;
    }

	public Boolean getIsReceive() {
		return isReceive;
	}

	/**
	 * 商品分类
	 */
	@ApiModelProperty(value = "商品分类")
	private ProductTypeResp productType;

	/**
	 * 状态 -1:未发布,100：未开始，200：已开始，300：已结束
	 */
	@ApiModelProperty(value = " -1:未发布，100：未开始，200：已开始，300：已结束")
	public Integer getStatus() {
		Date date = new Date();
		int start = provideStartTime.compareTo(date);
		int end = provideEndTime.compareTo(date);
		if (!isRelease) {
			return -1;
		}
		if (start > 0) {
			return 100;
		}
		if (start < 0 && end > 0) {
			return 200;
		}
		return 300;
	}
}