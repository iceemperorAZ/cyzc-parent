package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 会员表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-17 15:56:31
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(name = "BuyerResp", description = "会员表")
@Data
public class BuyerResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiProperty(description = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 店铺名称
     */
    @ApiProperty(description = "店铺名称")
    private String shopName;

    /**
     * 商户名
     */
    @ApiProperty(description = "商户名")
    private String userName;

    /**
     * 登录名
     */
    @ApiProperty(description = "登录名")
    private String loginName;

    /**
     * 密码
     */
    @ApiProperty(description = "密码")
    private String password;

    /**
     * 第三方唯一标识
     */
    @ApiProperty(description = "第三方唯一标识")
    private String uniqueId;

    /**
     * 手机号
     */
    @ApiProperty(description = "手机号")
    private String phone;

    /**
     * 邮箱
     */
    @ApiProperty(description = "邮箱")
    private String mail;

    /**
     * 头像
     */
    @ApiProperty(description = "头像")
    private String headUri;

    /**
     * 修改时间
     */
    @ApiProperty(description = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 会员积分
     */
    @ApiProperty(description = "会员积分")
    private Integer memberIntegral;

    /**
     * 超级到期时间
     */
    @ApiProperty(description = "超级到期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTime;

    /**
     * 会员类型
     */
    @ApiProperty(description = "会员类型")
    private Integer memberType;

    /**
     * 注册来源
     */
    @ApiProperty(description = "注册来源")
    private String registerSource;

    /**
     * 注册时间(创建时间)
     */
    @ApiProperty(description = "注册时间(创建时间)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 推荐人Id
     */
    @ApiProperty(description = "推荐人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long recommendUserId;

    /**
     * 销售员Id
     */
    @ApiProperty(description = "销售员Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long saleUserId;

    /**
     * 商户绑定的销售员信息
     */
    @ApiProperty(description = "商户绑定的销售员信息")
    private UserResp user;

    /**
     * 员工自己信息
     */
    @ApiProperty(description = "员工自己信息")
    private UserResp sale;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiProperty(description = "是否可用 0：否，1：是")
    private Boolean isAvailable;

    /**
     * 是否封停 0：否，1：是
     */
    @ApiProperty(description = "是否封停 0：否，1：是")
    private Boolean isSealUp;

    /**
     * 是否新用户
     */
    @ApiProperty(description = "是否新用户")
    private Boolean isNew;

    /**
     * token有效时长
     */
    @ApiProperty(description = "token有效时长")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long effectiveTime;

    /**
     * 级别 9999:代表超级管理员  100：销售，200:领导
     */
    @ApiProperty(description = "级别 9999:代表超级管理员  100：销售，200:领导")
    private Integer level;

    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 最后一次下单时间
     */
    @ApiProperty(description = "最后一次下单时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lastOrderTime;

    /**
     * 最后一次下单距现在的天数
     */
    @ApiProperty(description = "最后一次下单距现在的天数")
    private Long LastOrderDay;

    public Long getLastOrderDay() {
        return (System.currentTimeMillis() - lastOrderTime.getTime()) / (1000 * 3600 * 24);
    }

    /**
     * 默认收货地址
     */
    @ApiProperty(description = "默认收货地址")
    private String defaultAddr;

    /**
     * 金币
     */
    @ApiProperty(description = "金币")
    private Integer gold;

    /**
     * 返利剩余次数
     */
    @ApiProperty(description = "返利剩余次数")
    private Integer orderSpecificNum;

    /**
     * 会员等级（100：普通，200：银牌，300：金牌）
     */
    @ApiProperty(description = "会员等级")
    private Integer memberLevel;

    /**
     * 后台用户修改时间
     */
    @ApiProperty(description = "后台用户修改时间")
    private Date backUpdateTime;

    /**
     * 后台修改人id
     */
    @ApiProperty(description = "后台修改人id")
    private Long backUpdateUserId;

    /**
     * 商铺图片url集合
     */
    @ApiProperty(description = "商铺图片url集合")
    private List<String> buyerImdUrlsList;

    /**
     * 营业执照url集合
     */
    @ApiProperty(description = "business_license_urls")
    private List<String> businessLicenseUrlsList;

    /**
     * 审核人id
     */
    @ApiProperty(description = "审核人id")
    private Long reviewUserId;

    /**
     * 审核时间
     */
    @ApiProperty(description = "审核时间")
    private Date reviewTime;

    /**
     * 审核意见
     */
    @ApiProperty(description = "审核意见")
    private String reviewMsg;

    /**
     * 商户类别标签
     */
    @ApiProperty(description = "商户类别标签")
    private String buyerTypeLabel;


    /**
     * 商户类别标签集合
     */
    @ApiProperty(description = "商户类别标签集合")
    private List<String> buyerTypeLabelList;

    /**
     * 商户状态：100待审核，300审核通过
     */
    @ApiProperty(description = "商户状态：100待审核，300审核通过")
    private Integer buyerStatus;

//    @ApiProperty(description = "商户状态文字描述")
//    public String getBuyerStatusView() {
//        switch (buyerStatus) {
//            case 100:
//                return "待审核";
//            case 300:
//                return "审核通过";
//            default:
//                return "未知";
//        }
//    }
//
//    @ApiProperty(description = "商户状态文字描述")
//    public String getStatusView() {
//        switch (buyerStatus) {
//            case 100:
//                return "待审核";
//            case 300:
//                return "审核通过";
//            default:
//                return "未知";
//        }
//    }

    /**
     * 商铺图片字符串转集合
     */
    public void setBuyerImdUrlsList(String buyerImdUrlsList) {
        this.buyerImdUrlsList = StringUtils.isBlank(buyerImdUrlsList) ? null : Arrays.asList(buyerImdUrlsList.split(";"));
    }

    /**
     * 营业执照图片字符串转集合
     */
    public void setBusinessLicenseList(String businessLicenseUrlsList) {
        this.businessLicenseUrlsList = StringUtils.isBlank(businessLicenseUrlsList) ? null : Arrays.asList(businessLicenseUrlsList.split(";"));
    }

    public void setBuyerTypeLabelList(String buyerTypeLabelList) {
        this.buyerTypeLabelList = StringUtils.isBlank(buyerTypeLabelList) ? null : Arrays.asList(buyerTypeLabelList.split(";"));
    }
}
