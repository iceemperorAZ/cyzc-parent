package com.jingliang.mall.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-28 13:36:23
 */
@ApiModel(name = "OrderReq", description = "订单表")
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiProperty(description = "主键")
    private Long id;

    /**
     * 主键-开始
     */
    @ApiProperty(description = "主键-开始")
    private Long idStart;

    /**
     * 主键-结束
     */
    @ApiProperty(description = "主键-结束")
    private Long idEnd;

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
    private Long buyerId;

    /**
     * 用户Id-开始
     */
    @ApiProperty(description = "用户Id-开始")
    private Long buyerIdStart;

    /**
     * 用户Id-结束
     */
    @ApiProperty(description = "用户Id-结束")
    private Long buyerIdEnd;

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
     * 订单金额-开始
     */
    @ApiProperty(description = "订单金额-开始")
    private Double totalPriceStart;

    /**
     * 订单金额-结束
     */
    @ApiProperty(description = "订单金额-结束")
    private Double totalPriceEnd;

    /**
     * 应付金额
     */
    @ApiProperty(description = "应付金额")
    private Double payableFee;

    /**
     * 应付金额-开始
     */
    @ApiProperty(description = "应付金额-开始")
    private Double payableFeeStart;

    /**
     * 应付金额-结束
     */
    @ApiProperty(description = "应付金额-结束")
    private Double payableFeeEnd;

    /**
     * 优惠金额
     */
    @ApiProperty(description = "优惠金额")
    private Double preferentialFee;

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
     * 商品数量-开始
     */
    @ApiProperty(description = "商品数量-开始")
    private Integer productNumStart;

    /**
     * 商品数量-结束
     */
    @ApiProperty(description = "商品数量-结束")
    private Integer productNumEnd;

    /**
     * 运费
     */
    @ApiProperty(description = "运费")
    private Double deliverFee;

    /**
     * 运费-开始
     */
    @ApiProperty(description = "运费-开始")
    private Double deliverFeeStart;

    /**
     * 运费-结束
     */
    @ApiProperty(description = "运费-结束")
    private Double deliverFeeEnd;

    /**
     * 支付方式 100:微信,200:其他
     */
    @ApiProperty(description = "支付方式 100:微信,200:其他")
    private Integer payWay;

    /**
     * 支付方式 100:微信,200:其他-开始
     */
    @ApiProperty(description = "支付方式 100:微信,200:其他-开始")
    private Integer payWayStart;

    /**
     * 支付方式 100:微信,200:其他-结束
     */
    @ApiProperty(description = "支付方式 100:微信,200:其他-结束")
    private Integer payWayEnd;

    /**
     * 支付发起时间
     */
    @ApiProperty(description = "支付发起时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payStartTime;

    /**
     * 支付发起时间-开始
     */
    @ApiProperty(description = "支付发起时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payStartTimeStart;

    /**
     * 支付发起时间-结束
     */
    @ApiProperty(description = "支付发起时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payStartTimeEnd;

    /**
     * 支付结束时间
     */
    @ApiProperty(description = "支付结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payEndTime;

    /**
     * 支付结束时间-开始
     */
    @ApiProperty(description = "支付结束时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payEndTimeStart;

    /**
     * 支付结束时间-结束
     */
    @ApiProperty(description = "支付结束时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payEndTimeEnd;

    /**
     * 订单状态 100:待支付 200:已取消 300:已支付/待发货,400:已发货/待收货,500:已送达/待确认,600:已完成，700:已退货（不扣绩效），800:已退货（扣绩效）
     */
    @ApiProperty(description = "订单状态 100:待支付 200:已取消 300:已支付/待发货,400:已发货/待收货,500:已送达/待确认,600:已完成，700:已退货（不扣绩效），800:已退货（扣绩效）")
    private Integer orderStatus;

    /**
     * 订单状态 100:待支付 200:已取消 300:已支付/待发货,400:已发货/待收货,500:已送达/待确认,600:已完成，700:已退货（不扣绩效），800:已退货（扣绩效）-开始
     */
    @ApiProperty(description = "订单状态 100:待支付 200:已取消 300:已支付/待发货,400:已发货/待收货,500:已送达/待确认,600:已完成，700:已退货（不扣绩效），800:已退货（扣绩效）-开始")
    private Integer orderStatusStart;

    /**
     * 订单状态 100:待支付 200:已取消 300:已支付/待发货,400:已发货/待收货,500:已送达/待确认,600:已完成，700:已退货（不扣绩效），800:已退货（扣绩效）-结束
     */
    @ApiProperty(description = "订单状态 100:待支付 200:已取消 300:已支付/待发货,400:已发货/待收货,500:已送达/待确认,600:已完成，700:已退货（不扣绩效），800:已退货（扣绩效）-结束")
    private Integer orderStatusEnd;

    /**
     * 送货时间 100:只工作日收货（双休日，节假日不送） 200:只双休日，假日送货   300:工作日，双休日，假日均可送
     */
    @ApiProperty(description = "送货时间 100:只工作日收货（双休日，节假日不送） 200:只双休日，假日送货   300:工作日，双休日，假日均可送")
    private Integer deliveryType;

    /**
     * 送货时间 100:只工作日收货（双休日，节假日不送） 200:只双休日，假日送货   300:工作日，双休日，假日均可送-开始
     */
    @ApiProperty(description = "送货时间 100:只工作日收货（双休日，节假日不送） 200:只双休日，假日送货   300:工作日，双休日，假日均可送-开始")
    private Integer deliveryTypeStart;

    /**
     * 送货时间 100:只工作日收货（双休日，节假日不送） 200:只双休日，假日送货   300:工作日，双休日，假日均可送-结束
     */
    @ApiProperty(description = "送货时间 100:只工作日收货（双休日，节假日不送） 200:只双休日，假日送货   300:工作日，双休日，假日均可送-结束")
    private Integer deliveryTypeEnd;

    /**
     * 订单生成时间（创建时间）
     */
    @ApiProperty(description = "订单生成时间（创建时间）")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 订单生成时间（创建时间）-开始
     */
    @ApiProperty(description = "订单生成时间（创建时间）-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeStart;

    /**
     * 订单生成时间（创建时间）-结束
     */
    @ApiProperty(description = "订单生成时间（创建时间）-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeEnd;

    /**
     * 发货时间
     */
    @ApiProperty(description = "发货时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliverGoodsTime;

    /**
     * 发货时间-开始
     */
    @ApiProperty(description = "发货时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliverGoodsTimeStart;

    /**
     * 发货时间-结束
     */
    @ApiProperty(description = "发货时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliverGoodsTimeEnd;

    /**
     * 预计送达时间
     */
    @ApiProperty(description = "预计送达时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expectedDeliveryTime;

    /**
     * 预计送达时间-开始
     */
    @ApiProperty(description = "预计送达时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expectedDeliveryTimeStart;

    /**
     * 预计送达时间-结束
     */
    @ApiProperty(description = "预计送达时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expectedDeliveryTimeEnd;

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
    private Long updateUserId;

    /**
     * 修改时间
     */
    @ApiProperty(description = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 修改时间开始
     */
    @ApiProperty(description = "修改时间开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTimeStart;

    /**
     * 修改时间结束
     */
    @ApiProperty(description = "修改时间结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTimeEnd;

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
     * 订单详情集合
     */
    @ApiProperty(description = "订单详情集合")
    private List<OrderDetailReq> orderDetails;

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice * 100;
    }

    public void setTotalPriceStart(Double totalPriceStart) {
        this.totalPriceStart = totalPriceStart * 100;
    }

    public void setTotalPriceEnd(Double totalPriceEnd) {
        this.totalPriceEnd = totalPriceEnd * 100;
    }

    public void setPayableFee(Double payableFee) {
        this.payableFee = payableFee * 100;
    }

    public void setPayableFeeStart(Double payableFeeStart) {
        this.payableFeeStart = payableFeeStart * 100;
    }

    public void setPayableFeeEnd(Double payableFeeEnd) {
        this.payableFeeEnd = payableFeeEnd * 100;
    }

    public void setPreferentialFee(Double preferentialFee) {
        this.preferentialFee = preferentialFee * 100;
    }

    public void setDeliverFee(Double deliverFee) {
        this.deliverFee = deliverFee * 100;
    }

    public void setDeliverFeeStart(Double deliverFeeStart) {
        this.deliverFeeStart = deliverFeeStart * 100;
    }

    public void setDeliverFeeEnd(Double deliverFeeEnd) {
        this.deliverFeeEnd = deliverFeeEnd * 100;
    }
}