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
 * 优惠券
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-25 19:09:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "CouponReq", description = "优惠券")
public class CouponReq extends BaseReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 主键-开始
     */
    @ApiModelProperty(value = "主键-开始")
    private Long idStart;

    /**
     * 主键-结束
     */
    @ApiModelProperty(value = "主键-结束")
    private Long idEnd;

    /**
     * 商品区Id
     */
    @ApiModelProperty(value = "商品区Id")
    private Long productZoneId;

    /**
     * 商品区Id-开始
     */
    @ApiModelProperty(value = "商品区Id-开始")
    private Long productZoneIdStart;

    /**
     * 商品区Id-结束
     */
    @ApiModelProperty(value = "商品区Id-结束")
    private Long productZoneIdEnd;

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
     * 优惠券剩余数量
     */
    @ApiModelProperty(value = "优惠券剩余数量")
    private Integer residueNumber;

    /**
     * 优惠券剩余数量-开始
     */
    @ApiModelProperty(value = "优惠券剩余数量-开始")
    private Integer residueNumberStart;

    /**
     * 优惠券剩余数量-结束
     */
    @ApiModelProperty(value = "优惠券剩余数量-结束")
    private Integer residueNumberEnd;

    /**
     * 优惠券总数量
     */
    @ApiModelProperty(value = "优惠券总数量")
    private Integer totalNumber;

    /**
     * 优惠券总数量-开始
     */
    @ApiModelProperty(value = "优惠券总数量-开始")
    private Integer totalNumberStart;

    /**
     * 优惠券总数量-结束
     */
    @ApiModelProperty(value = "优惠券总数量-结束")
    private Integer totalNumberEnd;

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
     * 优惠券类型  100:超级会员,200:普通用户，300:新建用户
     */
    @ApiModelProperty(value = "优惠券类型  100:超级会员,200:普通用户，300:新建用户")
    private Integer couponType;

    /**
     * 优惠券类型  100:超级会员,200:普通用户，300:新建用户-开始
     */
    @ApiModelProperty(value = "优惠券类型  100:超级会员,200:普通用户，300:新建用户-开始")
    private Integer couponTypeStart;

    /**
     * 优惠券类型  100:超级会员,200:普通用户，300:新建用户-结束
     */
    @ApiModelProperty(value = "优惠券类型  100:超级会员,200:普通用户，300:新建用户-结束")
    private Integer couponTypeEnd;

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
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称")
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