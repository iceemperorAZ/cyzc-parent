package com.jingliang.mall.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户优惠券
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-18 16:50:49
 */
@ApiModel(value = "BuyerCouponReq", description = "用户优惠券")
@EqualsAndHashCode(callSuper = true)
@Data
public class BuyerCouponReq extends BaseReq implements Serializable {

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
     * 优惠券Id
     */
    @ApiModelProperty(value = "优惠券Id")
    private Long couponId;

    /**
     * 优惠券Id-开始
     */
    @ApiModelProperty(value = "优惠券Id-开始")
    private Long couponIdStart;

    /**
     * 优惠券Id-结束
     */
    @ApiModelProperty(value = "优惠券Id-结束")
    private Long couponIdEnd;

    /**
     * 使用范围（商品分类）
     */
    @ApiModelProperty(value = "使用范围（商品分类）")
    private Long productTypeId;

    /**
     * 使用范围（商品分类）-开始
     */
    @ApiModelProperty(value = "使用范围（商品分类）-开始")
    private Long productTypeIdStart;

    /**
     * 使用范围（商品分类）-结束
     */
    @ApiModelProperty(value = "使用范围（商品分类）-结束")
    private Long productTypeIdEnd;

    /**
     * 用户Id
     */
    @ApiModelProperty(value = "用户Id")
    private Long buyerId;

    /**
     * 用户Id-开始
     */
    @ApiModelProperty(value = "用户Id-开始")
    private Long buyerIdStart;

    /**
     * 用户Id-结束
     */
    @ApiModelProperty(value = "用户Id-结束")
    private Long buyerIdEnd;

    /**
     * 优惠百分比
     */
    @ApiModelProperty(value = "优惠百分比")
    private Long percentage;

    /**
     * 优惠百分比-开始
     */
    @ApiModelProperty(value = "优惠百分比-开始")
    private Long percentageStart;

    /**
     * 优惠百分比-结束
     */
    @ApiModelProperty(value = "优惠百分比-结束")
    private Long percentageEnd;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 开始时间-开始
     */
    @ApiModelProperty(value = "开始时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTimeStart;

    /**
     * 开始时间-结束
     */
    @ApiModelProperty(value = "开始时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTimeEnd;

    /**
     * 到期时间
     */
    @ApiModelProperty(value = "到期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTime;

    /**
     * 到期时间-开始
     */
    @ApiModelProperty(value = "到期时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTimeStart;

    /**
     * 到期时间-结束
     */
    @ApiModelProperty(value = "到期时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTimeEnd;

    /**
     * 优惠券描述
     */
    @ApiModelProperty(value = "优惠券描述")
    private String couponDescribe;

    /**
     * 剩余可用张数
     */
    @ApiModelProperty(value = "剩余可用张数")
    private Integer receiveNum;

    /**
     * 剩余可用张数-开始
     */
    @ApiModelProperty(value = "剩余可用张数-开始")
    private Integer receiveNumStart;

    /**
     * 剩余可用张数-结束
     */
    @ApiModelProperty(value = "剩余可用张数-结束")
    private Integer receiveNumEnd;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiModelProperty(value = "是否可用 0：否，1：是")
    private Boolean isAvailable;

    /**
     * 领取时间/赠送时间(创建时间)
     */
    @ApiModelProperty(value = "领取时间/赠送时间(创建时间)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 领取时间/赠送时间(创建时间)-开始
     */
    @ApiModelProperty(value = "领取时间/赠送时间(创建时间)-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeStart;

    /**
     * 领取时间/赠送时间(创建时间)-结束
     */
    @ApiModelProperty(value = "领取时间/赠送时间(创建时间)-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeEnd;

    /**
     * 创建人Id(系统：-1)
     */
    @ApiModelProperty(value = "创建人Id(系统：-1)")
    private Long createUserId;

    /**
     * 创建人Id(系统：-1)-开始
     */
    @ApiModelProperty(value = "创建人Id(系统：-1)-开始")
    private Long createUserIdStart;

    /**
     * 创建人Id(系统：-1)-结束
     */
    @ApiModelProperty(value = "创建人Id(系统：-1)-结束")
    private Long createUserIdEnd;

    /**
     * 创建人(系统：系统)
     */
    @ApiModelProperty(value = "创建人(系统：系统)")
    private String createUser;

    /**
     * 状态 可使用：100，已用完：200，已过期：300
     */
    @ApiModelProperty(value = "状态 可使用：100，已用完：200，已过期：300")
    private Integer status;

    /**
     * 商品分类Id集合
     */
    @ApiModelProperty(value = "商品分类Id集合")
    private List<Long> productTypeIds;

}