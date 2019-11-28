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
 * 优惠券
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-25 19:09:02
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(value = "CouponResp", description = "优惠券")
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
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private Double money;

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
     * 使用条件(最低使用价格)
     */
    @ApiModelProperty(value = "使用条件(最低使用价格)")
    private Double useCondition;

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
     * 修改时间
     */
    @ApiModelProperty(value = "是否已领取")
    private Boolean isReceive;

    /**
     * 状态 100：未开始，200：已开始，300：已结束
     */
    @ApiModelProperty(value = "100：未开始，200：已开始，300：已结束")
    public Integer getStatus() {
        Date date = new Date();
        int start = startTime.compareTo(date);
        int end = expirationTime.compareTo(date);
        if (start > 0) {
            return 100;
        }
        if (start < 0 && end > 0) {
            return 200;
        }
        return 300;
    }

    public Double getMoney() {
        return money / 100;
    }

    public Double getUseCondition() {
        return useCondition / 100;
    }
}