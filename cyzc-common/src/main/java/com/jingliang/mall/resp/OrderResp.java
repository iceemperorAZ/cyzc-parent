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
import java.util.stream.Collectors;

/**
 * 订单表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-28 13:36:23
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(name =  "OrderResp", description = "订单表")
public class OrderResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiProperty(description = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 第三方订支付单号/预支付单号
     */
    @ApiProperty(description = "第三方订支付单号/预支付单号")
    private String payNo;

    /**
     * 订单编号
     */
    @ApiProperty(description = "订单编号")
    private String orderNo;

    /**
     * 用户Id
     */
    @ApiProperty(description = "用户Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long buyerId;

    /**
     * 收获地址：省
     */
    @ApiProperty(description = "收获地址：省")
    private String detailAddressProvince;

    /**
     * 收获地址：市
     */
    @ApiProperty(description = "收获地址：市")
    private String detailAddressCity;

    /**
     * 收获地址：区
     */
    @ApiProperty(description = "收获地址：区")
    private String detailAddressArea;

    /**
     * 收获地址：街道
     */
    @ApiProperty(description = "收获地址：街道")
    private String detailAddressStreet;

    /**
     * 收货详细地址信息
     */
    @ApiProperty(description = "收货详细地址信息")
    private String detailAddress;

    /**
     * 收货人
     */
    @ApiProperty(description = "收货人")
    private String receiverName;

    /**
     * 收货人电话
     */
    @ApiProperty(description = "收货人电话")
    private String receiverPhone;

    /**
     * 订单金额
     */
    @ApiProperty(description = "订单金额")
    private Double totalPrice;

    /**
     * 应付金额
     */
    @ApiProperty(description = "应付金额")
    private Double payableFee;

    /**
     * 优惠金额
     */
    @ApiProperty(description = "优惠金额")
    private Double preferentialFee;

    /**
     * 使用优惠券Id集合 多个之间使用,分割
     */
    @ApiProperty(description = "使用优惠券Id集合 多个之间使用,分割")
    private String couponIds;

    /**
     * 用户优惠券Id集合
     */
    @ApiProperty(description = "用户优惠券Id集合")
    private List<Long> couponIdList;

    /**
     * 商品数量
     */
    @ApiProperty(description = "商品数量")
    private Integer productNum;

    /**
     * 运费
     */
    @ApiProperty(description = "运费")
    private Double deliverFee;

    /**
     * 支付方式 100:微信,200:其他
     */
    @ApiProperty(description = "支付方式 100:微信,200:其他")
    private Integer payWay;

    /**
     * 支付发起时间
     */
    @ApiProperty(description = "支付发起时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payStartTime;

    /**
     * 支付结束时间
     */
    @ApiProperty(description = "支付结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payEndTime;

    /**
     * 订单状态 100:待支付 200:已取消 300:已支付/待发货,400:已发货/待收货,500:已送达/待确认,600:已完成，700:已退货（不扣绩效），800:已退货（扣绩效）
     */
    @ApiProperty(description = "订单状态 100:待支付 200:已取消 300:已支付/待发货,400:已发货/待收货,500:已送达/待确认,600:已完成，700:已退货（不扣绩效），800:已退货（扣绩效）")
    private Integer orderStatus;

    /**
     * 送货时间 100:只工作日收货（双休日，节假日不送） 200:只双休日，假日送货   300:工作日，双休日，假日均可送
     */
    @ApiProperty(description = "送货时间 100:只工作日收货（双休日，节假日不送） 200:只双休日，假日送货   300:工作日，双休日，假日均可送")
    private Integer deliveryType;

    /**
     * 订单生成时间（创建时间）
     */
    @ApiProperty(description = "订单生成时间（创建时间）")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 订单完成时间（取消也算完成）
     */
    @ApiProperty(description = "订单完成时间（取消也算完成）")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;

    /**
     * 发货时间
     */
    @ApiProperty(description = "发货时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliverGoodsTime;

    /**
     * 预计送达时间
     */
    @ApiProperty(description = "预计送达时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expectedDeliveryTime;

    /**
     * 附言
     */
    @ApiProperty(description = "附言")
    private String note;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiProperty(description = "是否可用 0：否，1：是")
    private Boolean isAvailable;

    /**
     * 配送人名称
     */
    @ApiProperty(description = "配送人名称")
    private String deliveryName;

    /**
     * 配送人联系方式
     */
    @ApiProperty(description = "配送人联系方式")
    private String deliveryPhone;

    /**
     * 修改人
     */
    @ApiProperty(description = "修改人")
    private String updateUserName;

    /**
     * 修改人Id
     */
    @ApiProperty(description = "修改人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateUserId;

    /**
     * 修改时间
     */
    @ApiProperty(description = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 发货仓库
     */
    @ApiProperty(description = "发货仓库")
    private String storehouse;

    /**
     * 是否使用金币
     */
    @ApiProperty(description = "是否使用金币")
    private Boolean isGold;

    /**
     * 使用金币数
     */
    @ApiProperty(description = "使用金币数")
    private Integer gold;

    /**
     * 返金币数
     */
    @ApiProperty(description = "返金币数")
    private Integer returnGold;

    /**
     * 销售员
     */
    @ApiProperty(description = "销售员")
    private UserResp sale;

    /**
     * 商户信息
     */
    @ApiProperty(description = "商户信息")
    private BuyerResp buyer;

    /**
     * 订单详情列表
     */
    @ApiProperty(description = "订单详情列表")
    private List<OrderDetailResp> orderDetails;

    @ApiProperty(description = "订单状态文字描述")
    private String orderStatusView;
    public String getOrderStatusView() {
        switch (orderStatus) {
            case 100:
                return "待支付";
            case 200:
                return "已取消";
            case 300:
                return "待发货";
            case 400:
                return "待收货";
            case 500:
                return "待确认";
            case 600:
                return "已完成";
            case 700:
            case 800:
                return "已退货";
            default:
                return "未知";
        }
    }

    @ApiProperty(description = "订单状态文字描述")
    private String statusView;
    public String getStatusView() {
        switch (orderStatus) {
            case 100:
                return "待支付";
            case 200:
                return "已取消";
            case 300:
                return "待发货";
            case 400:
                return "待收货";
            case 500:
                return "待确认";
            case 600:
                return "已完成";
            case 700:
                return "已退货(不扣绩效)";
            case 800:
                return "已退货(扣绩效)";
            default:
                return "未知";
        }
    }

    public List<Long> getCouponIdList() {
        return StringUtils.isBlank(couponIds) ? null : Arrays.stream(couponIds.split(",")).map(s -> Long.parseLong(s.split("\\|")[0].trim())).collect(Collectors.toList());
    }

    public Double getTotalPrice() {
        return totalPrice / 100;
    }

    public Double getPayableFee() {
        return payableFee / 100;
    }

    public Double getPreferentialFee() {
        return preferentialFee / 100;
    }

    public Double getDeliverFee() {
        return deliverFee / 100;
    }
}