package com.jingliang.mall.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 用户优惠券
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-28 13:36:23
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
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private Double money;

    /**
     * 金额-开始
     */
    @ApiModelProperty(value = "金额-开始")
    private Double moneyStart;

    /**
     * 金额-结束
     */
    @ApiModelProperty(value = "金额-结束")
    private Double moneyEnd;

    /**
     * 使用条件(最低使用价格)
     */
    @ApiModelProperty(value = "使用条件(最低使用价格)")
    private Double useCondition;

    /**
     * 使用条件(最低使用价格)-开始
     */
    @ApiModelProperty(value = "使用条件(最低使用价格)-开始")
    private Double useConditionStart;

    /**
     * 使用条件(最低使用价格)-结束
     */
    @ApiModelProperty(value = "使用条件(最低使用价格)-结束")
    private Double useConditionEnd;

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
     * 使用范围（商品分类多个）
     */
    @ApiModelProperty(value = "使用范围（商品分类多个）")
    private Set<Long> productTypeIds;


    /**
     * 优惠券描述
     */
    @ApiModelProperty(value = "优惠券描述")
    private String couponDescribe;

    /**
     * 是否已使用 0：否，1：是
     */
    @ApiModelProperty(value = "是否已使用 0：否，1：是")
    private Boolean isUsed;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiModelProperty(value = "是否可用 0：否，1：是")
    private Boolean isAvailable;

    /**
     * 领取时间(创建时间)
     */
    @ApiModelProperty(value = "领取时间(创建时间)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 领取时间(创建时间)-开始
     */
    @ApiModelProperty(value = "领取时间(创建时间)-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeStart;

    /**
     * 领取时间(创建时间)-结束
     */
    @ApiModelProperty(value = "领取时间(创建时间)-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeEnd;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态 未使用：100，已使用：200，已过期：300")
    private Integer status;

    public void setMoney(Double money) {
        this.money = money * 100;
    }

    public void setMoneyStart(Double moneyStart) {
        this.moneyStart = moneyStart * 100;
    }

    public void setMoneyEnd(Double moneyEnd) {
        this.moneyEnd = moneyEnd * 100;
    }

    public void setUseCondition(Double useCondition) {
        this.useCondition = useCondition * 100;
    }

    public void setUseConditionStart(Double useConditionStart) {
        this.useConditionStart = useConditionStart * 100;
    }

    public void setUseConditionEnd(Double useConditionEnd) {
        this.useConditionEnd = useConditionEnd * 100;
    }
}