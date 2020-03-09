package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * 会员表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-17 15:56:31
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "BuyerResp", description = "会员表")
@Data
public class BuyerResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
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
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 会员积分
     */
    @ApiModelProperty(value = "会员积分")
    private Integer memberIntegral;

    /**
     * 超级到期时间
     */
    @ApiModelProperty(value = "超级到期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTime;

    /**
     * 会员类型
     */
    @ApiModelProperty(value = "会员类型")
    private Integer memberType;

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
     * 推荐人Id
     */
    @ApiModelProperty(value = "推荐人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long recommendUserId;

    /**
     * 销售员Id
     */
    @ApiModelProperty(value = "销售员Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long saleUserId;

    /**
     * 商户绑定的销售员信息
     */
    @ApiModelProperty(value = "商户绑定的销售员信息")
    private UserResp user;

    /**
     * 员工自己信息
     */
    @ApiModelProperty(value = "员工自己信息")
    private UserResp sale;

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

    /**
     * token有效时长
     */
    @ApiModelProperty(value = "token有效时长")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long effectiveTime;

    /**
     * 级别 9999:代表超级管理员  100：销售，200:领导
     */
    @ApiModelProperty(value = "级别 9999:代表超级管理员  100：销售，200:领导")
    private Integer level;

    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 最后一次下单时间
     */
    @ApiModelProperty(value = "最后一次下单时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lastOrderTime;

    /**
     * 最后一次下单距现在的天数
     */
    @ApiModelProperty(value = "最后一次下单距现在的天数")
    public Long getLastOrderDay() {
        return (System.currentTimeMillis() - lastOrderTime.getTime()) / (1000 * 3600 * 24);
    }

    /**
     * 默认收货地址
     */
    @ApiModelProperty(value = "默认收货地址")
    private String defaultAddr;

    /**
     * 金币
     */
    @ApiModelProperty(value = "金币")
    private Integer gold;

    /**
     * 返利剩余次数
     */
    @ApiModelProperty(value = "返利剩余次数")
    private Integer orderSpecificNum;

    /**
     * 会员等级（100：普通，200：银牌，300：金牌）
     */
    @ApiModelProperty(value = "会员等级")
    private Integer memberLevel;
}
