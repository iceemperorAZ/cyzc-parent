package com.jingliang.mall.req;

import java.util.Date;
import lombok.Data;
import com.citrsw.annatation.ApiModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import com.citrsw.annatation.ApiProperty;
import java.io.Serializable;

/**
 * 优惠券
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-18 16:50:49
 */
@ApiModel(name = "CouponReq", description = "优惠券")
@EqualsAndHashCode(callSuper = true)
@Data
public class CouponReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiProperty(description = "主键")
	private Long id;

	/**
	 * 主键-开始
	 */
	@ApiProperty(description = "主键-开始")
	private Long idStart;

	/**
	 * 主键-结束
	 */
	@ApiProperty(description = "主键-结束")
	private Long idEnd;

	/**
	 * 商品区Id
	 */
	@ApiProperty(description = "商品区Id")
	private Long productZoneId;

	/**
	 * 商品区Id-开始
	 */
	@ApiProperty(description = "商品区Id-开始")
	private Long productZoneIdStart;

	/**
	 * 商品区Id-结束
	 */
	@ApiProperty(description = "商品区Id-结束")
	private Long productZoneIdEnd;

	/**
	 * 使用范围（商品分类）
	 */
	@ApiProperty(description = "使用范围（商品分类）")
	private Long productTypeId;

	/**
	 * 使用范围（商品分类）-开始
	 */
	@ApiProperty(description = "使用范围（商品分类）-开始")
	private Long productTypeIdStart;

	/**
	 * 使用范围（商品分类）-结束
	 */
	@ApiProperty(description = "使用范围（商品分类）-结束")
	private Long productTypeIdEnd;

	/**
	 * 优惠百分比
	 */
	@ApiProperty(description = "优惠百分比")
	private Integer percentage;

	/**
	 * 优惠百分比-开始
	 */
	@ApiProperty(description = "优惠百分比-开始")
	private Integer percentageStart;

	/**
	 * 优惠百分比-结束
	 */
	@ApiProperty(description = "优惠百分比-结束")
	private Integer percentageEnd;

	/**
	 * 可领取数
	 */
	@ApiProperty(description = "可领取数")
	private Integer receiveNum;

	/**
	 * 优惠券剩余数量
	 */
	@ApiProperty(description = "优惠券剩余数量")
	private Integer residueNumber;


	/**
	 * 优惠券总数量
	 */
	@ApiProperty(description = "优惠券总数量")
	private Integer totalNumber;


	/**
	 * 优惠券类型  100:超级会员,200:普通用户，300:新建用户
	 */
	@ApiProperty(description = "优惠券类型  100:超级会员,200:普通用户，300:新建用户")
	private Integer couponType;

	/**
	 * 开始时间
	 */
	@ApiProperty(description = "开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTime;

	/**
	 * 开始时间-开始
	 */
	@ApiProperty(description = "开始时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTimeStart;

	/**
	 * 开始时间-结束
	 */
	@ApiProperty(description = "开始时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTimeEnd;

	/**
	 * 到期时间
	 */
	@ApiProperty(description = "到期时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date expirationTime;

	/**
	 * 到期时间-开始
	 */
	@ApiProperty(description = "到期时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date expirationTimeStart;

	/**
	 * 到期时间-结束
	 */
	@ApiProperty(description = "到期时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date expirationTimeEnd;

	/**
	 * 优惠券描述
	 */
	@ApiProperty(description = "优惠券描述")
	private String couponDescribe;

	/**
	 * 是否发布 0：否，1：是
	 */
	@ApiProperty(description = "是否发布 0：否，1：是")
	private Boolean isRelease;

	/**
	 * 发放开始时间
	 */
	@ApiProperty(description = "发放开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date provideStartTime;

	/**
	 * 发放开始时间-开始
	 */
	@ApiProperty(description = "发放开始时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date provideStartTimeStart;

	/**
	 * 发放开始时间-结束
	 */
	@ApiProperty(description = "发放开始时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date provideStartTimeEnd;

	/**
	 * 发放结束时间
	 */
	@ApiProperty(description = "发放结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date provideEndTime;

	/**
	 * 发放结束时间-开始
	 */
	@ApiProperty(description = "发放结束时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date provideEndTimeStart;

	/**
	 * 发放结束时间-结束
	 */
	@ApiProperty(description = "发放结束时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date provideEndTimeEnd;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiProperty(description = "是否可用 0：否，1：是")
	private Boolean isAvailable;

	/**
	 * 创建时间
	 */
	@ApiProperty(description = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 创建时间-开始
	 */
	@ApiProperty(description = "创建时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeStart;

	/**
	 * 创建时间-结束
	 */
	@ApiProperty(description = "创建时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeEnd;

	/**
	 * 创建人名称
	 */
	@ApiProperty(description = "创建人名称")
	private String createUserName;

	/**
	 * 创建人Id-结束
	 */
	@ApiProperty(description = "创建人Id-结束")
	private Long createUserIdEnd;

	/**
	 * 修改人
	 */
	@ApiProperty(description = "修改人")
	private String updateUserName;

	/**
	 * 修改人Id
	 */
	@ApiProperty(description = "修改人Id")
	private Long updateUserId;

	/**
	 * 修改时间
	 */
	@ApiProperty(description = "修改时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 修改时间-开始
	 */
	@ApiProperty(description = "修改时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeStart;

	/**
	 * 修改时间-结束
	 */
	@ApiProperty(description = "修改时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTimeEnd;

}