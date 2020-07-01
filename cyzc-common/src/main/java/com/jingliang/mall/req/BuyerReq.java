package com.jingliang.mall.req;

import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 会员表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-17 15:56:31
 */
@ApiModel(name = "BuyerReq", description = "会员表")
@EqualsAndHashCode(callSuper = true)
@Data
public class BuyerReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiProperty(description = "主键")
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
     * 登录时获取的 code
     */
    @ApiProperty(description = "登录时获取的 code")
    private String code;

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
     * 头像base64
     */
    @ApiProperty(description = "头像base64")
    private String head;

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

    /**
     * 会员积分
     */
    @ApiProperty(description = "会员积分")
    private Integer memberIntegral;

    /**
     * 会员积分-开始
     */
    @ApiProperty(description = "会员积分-开始")
    private Integer memberIntegralStart;

    /**
     * 会员积分-结束
     */
    @ApiProperty(description = "会员积分-结束")
    private Integer memberIntegralEnd;

    /**
     * 超级到期时间
     */
    @ApiProperty(description = "超级到期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTime;

    /**
     * 超级到期时间-开始
     */
    @ApiProperty(description = "超级到期时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTimeStart;

    /**
     * 超级到期时间-结束
     */
    @ApiProperty(description = "超级到期时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTimeEnd;

    /**
     * 会员类型
     */
    @ApiProperty(description = "会员类型")
    private Integer memberType;

    /**
     * 会员类型-开始
     */
    @ApiProperty(description = "会员类型-开始")
    private Integer memberTypeStart;

    /**
     * 会员类型-结束
     */
    @ApiProperty(description = "会员类型-结束")
    private Integer memberTypeEnd;

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
     * 注册时间(创建时间)-开始
     */
    @ApiProperty(description = "注册时间(创建时间)-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTimeStart;

    /**
     * 注册时间(创建时间)-结束
     */
    @ApiProperty(description = "注册时间(创建时间)-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTimeEnd;

    /**
     * 推荐人Id
     */
    @ApiProperty(description = "推荐人Id")
    private Long recommendUserId;

    /**
     * 销售员Id
     */
    @ApiProperty(description = "销售员Id")
    private Long saleUserId;

    /**
     * 销售员名称-开始
     */
    @ApiProperty(description = "销售员名称-开始")
    private Long saleUserNameStart;

    /**
     * 销售员名称-结束
     */
    @ApiProperty(description = "销售员名称-结束")
    private Long saleUserNameEnd;

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
     * 最后一次下单时间
     */
    @ApiProperty(description = "最后一次下单时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lastOrderTime;

    /**
     * 金币
     */
    @ApiProperty(description = "金币")
    private Integer gold;

    /**
     * 会员等级（100：普通，200：银牌，300：金牌）
     */
    @ApiProperty(description = "会员等级")
    private Integer memberLevel;

    /**
     * 返利剩余次数
     */
    @ApiProperty(description = "返利剩余次数")
    private Integer orderSpecificNum;

    /**
     * 省编码
     */
    @ApiProperty(description = "省编码")
    private String provinceCode;

    /**
     * 市编码
     */
    @ApiProperty(description = "市编码")
    private String cityCode;

    /**
     * 县编码
     */
    @ApiProperty(description = "县编码")
    private String areaCode;

    /**
     * 详细地址
     */
    @ApiProperty(description = "详细地址")
    private String detailedAddress;

    /**
     * 经度
     */
    @ApiProperty(description = "经度")
    private String latitude;

    /**
     * 纬度
     */
    @ApiProperty(description = "纬度")
    private String longitude;

    /**
     * 是否可以货到付款 0：否，1：是
     */
    @ApiProperty(name = "是否可以货到付款")
    private Boolean cashOnDelivery;

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
    @ApiProperty(description = "营业执照url集合")
    private List<String> businessLicenseUrlsList;

    /**
     * 商铺base64图片集合
     */
    @ApiProperty(description = "商铺base64图片集合")
    private List<String> buyerImgBase64s = new ArrayList<>();

    /**
     * 营业执照base64图片集合
     */
    @ApiProperty(description = "营业执照base64图片集合")
    private List<String> businessLicenseBase64s = new ArrayList<>();

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
     * 商户状态：100待审核，300审核通过
     */
    @ApiProperty(description = "商户状态：100待审核，300审核通过")
    private Integer buyerStatus;

    /**
     * 商户类别标签
     */
    @ApiProperty(description = "商户类别标签")
    private List<String> buyerTypeLabelList;
}