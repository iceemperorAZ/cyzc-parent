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
import java.util.Objects;

/**
 * 用户优惠券
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-28 13:36:23
 */
@ApiModel(value = "BuyerCouponResp", description = "用户优惠券")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BuyerCouponResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 优惠券Id
     */
    @ApiModelProperty(value = "优惠券Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long couponId;

    /**
     * 用户Id
     */
    @ApiModelProperty(value = "用户Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long buyerId;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private Double money;

    /**
     * 使用条件(最低使用价格)
     */
    @ApiModelProperty(value = "使用条件(最低使用价格)")
    private Double useCondition;

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
     * 使用范围（商品分类）
     */
    @ApiModelProperty(value = "使用范围（商品分类）")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productTypeId;

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

    public Double getMoney() {
        return Objects.isNull(money) ? null : money / 100;
    }

    public Double getUseCondition() {
        return Objects.isNull(useCondition) ? null : useCondition / 100;
    }
}