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
 * 会员表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-17 15:56:31
 */
@ApiModel(value = "BuyerReq", description = "会员表")
@EqualsAndHashCode(callSuper = true)
@Data
public class BuyerReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * 店铺名称
	 */
	@ApiModelProperty(value = "店铺名称")
	private String shopName;

	/**
	 * 商户名
	 */
	@ApiModelProperty(value = "商户名")
	private String userName;

    /**
     * 登录时获取的 code
     */
    @ApiModelProperty(value = "登录时获取的 code")
    private String code;

    /**
     * 登录名
     */
    @ApiModelProperty(value = "登录名")
    private String loginName;

	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码")
	private String password;

	/**
	 * 第三方唯一标识
	 */
	@ApiModelProperty(value = "第三方唯一标识")
	private String uniqueId;

	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String phone;

	/**
	 * 邮箱
	 */
	@ApiModelProperty(value = "邮箱")
	private String mail;

	/**
	 * 头像
	 */
	@ApiModelProperty(value = "头像")
	private String headUri;

	/**
	 * 头像base64
	 */
	@ApiModelProperty(value = "头像base64")
	private String head;

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

	/**
	 * 会员积分
	 */
	@ApiModelProperty(value = "会员积分")
	private Integer memberIntegral;

	/**
	 * 会员积分-开始
	 */
	@ApiModelProperty(value = "会员积分-开始")
	private Integer memberIntegralStart;

	/**
	 * 会员积分-结束
	 */
	@ApiModelProperty(value = "会员积分-结束")
	private Integer memberIntegralEnd;

	/**
	 * 超级到期时间
	 */
	@ApiModelProperty(value = "超级到期时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date expirationTime;

	/**
	 * 超级到期时间-开始
	 */
	@ApiModelProperty(value = "超级到期时间-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date expirationTimeStart;

	/**
	 * 超级到期时间-结束
	 */
	@ApiModelProperty(value = "超级到期时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date expirationTimeEnd;

	/**
	 * 会员类型
	 */
	@ApiModelProperty(value = "会员类型")
	private Integer memberType;

	/**
	 * 会员类型-开始
	 */
	@ApiModelProperty(value = "会员类型-开始")
	private Integer memberTypeStart;

	/**
	 * 会员类型-结束
	 */
	@ApiModelProperty(value = "会员类型-结束")
	private Integer memberTypeEnd;

	/**
	 * 注册来源
	 */
	@ApiModelProperty(value = "注册来源")
	private String registerSource;

	/**
	 * 注册时间(创建时间)
	 */
	@ApiModelProperty(value = "注册时间(创建时间)")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 注册时间(创建时间)-开始
	 */
	@ApiModelProperty(value = "注册时间(创建时间)-开始")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createTimeStart;

	/**
	 * 注册时间(创建时间)-结束
	 */
	@ApiModelProperty(value = "注册时间(创建时间)-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createTimeEnd;

	/**
	 * 推荐人Id
	 */
	@ApiModelProperty(value = "推荐人Id")
	private Long recommendUserId;

	/**
	 * 销售员Id
	 */
	@ApiModelProperty(value = "销售员Id")
	private Long saleUserId;

	/**
	 * 销售员名称-开始
	 */
	@ApiModelProperty(value = "销售员名称-开始")
	private Long saleUserNameStart;

	/**
	 * 销售员名称-结束
	 */
	@ApiModelProperty(value = "销售员名称-结束")
	private Long saleUserNameEnd;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiModelProperty(value = "是否可用 0：否，1：是")
	private Boolean isAvailable;

	/**
	 * 是否封停 0：否，1：是
	 */
	@ApiModelProperty(value = "是否封停 0：否，1：是")
	private Boolean isSealUp;

	/**
	 * 是否新用户
	 */
	@ApiModelProperty(value = "是否新用户")
	private Boolean isNew;

}