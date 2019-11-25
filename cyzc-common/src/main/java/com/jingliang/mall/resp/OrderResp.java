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
import java.util.List;

/**
 * 订单表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-28 13:36:23
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(value = "OrderResp", description = "订单表")
public class OrderResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 第三方订支付单号/预支付单号
     */
    @ApiModelProperty(value = "第三方订支付单号/预支付单号")
    private String payNo;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    /**
     * 用户Id
     */
    @ApiModelProperty(value = "用户Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long buyerId;

    /**
     * 收货详细地址信息
     */
    @ApiModelProperty(value = "收货详细地址信息")
    private String detailAddress;

    /**
     * 收货人
     */
    @ApiModelProperty(value = "收货人")
    private String receiverName;

    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;

    /**
     * 订单金额
     */
    @ApiModelProperty(value = "订单金额")
    private Double totalPrice;

    /**
     * 应付金额
     */
    @ApiModelProperty(value = "应付金额")
    private Double payableFee;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private Double preferentialFee;

    /**
     * 用户优惠券Id
     */
    @ApiModelProperty(value = "用户优惠券Id")
    private String couponId;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer productNum;

    /**
     * 运费
     */
    @ApiModelProperty(value = "运费")
    private Double deliverFee;

    /**
     * 支付方式 100:微信,200:现金
     */
    @ApiModelProperty(value = "支付方式 100:微信,200:现金")
    private Integer payWay;

    /**
     * 支付发起时间
     */
    @ApiModelProperty(value = "支付发起时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payStartTime;

    /**
     * 支付结束时间
     */
    @ApiModelProperty(value = "支付结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payEndTime;

    /**
     * 订单状态 100:待支付 200:已取消 300:已支付/待发货,400:已发货/待收货,500:已送达/待确认,600:已完成，700:已退货（不扣绩效），800:已退货（扣绩效）
     */
    @ApiModelProperty(value = "订单状态 100:待支付 200:已取消 300:已支付/待发货,400:已发货/待收货,500:已送达/待确认,600:已完成，700:已退货（不扣绩效），800:已退货（扣绩效）")
    private Integer orderStatus;

    /**
     * 送货时间 100:只工作日收货（双休日，节假日不送） 200:只双休日，假日送货   300:工作日，双休日，假日均可送
     */
    @ApiModelProperty(value = "送货时间 100:只工作日收货（双休日，节假日不送） 200:只双休日，假日送货   300:工作日，双休日，假日均可送")
    private Integer deliveryType;

    /**
     * 订单生成时间（创建时间）
     */
    @ApiModelProperty(value = "订单生成时间（创建时间）")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 订单完成时间（取消也算完成）
     */
    @ApiModelProperty(value = "订单完成时间（取消也算完成）")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;

    /**
     * 发货时间
     */
    @ApiModelProperty(value = "发货时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliverGoodsTime;

    /**
     * 预计送达时间
     */
    @ApiModelProperty(value = "预计送达时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expectedDeliveryTime;

    /**
     * 附言
     */
    @ApiModelProperty(value = "附言")
    private String note;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiModelProperty(value = "是否可用 0：否，1：是")
    private Boolean isAvailable;

    /**
     * 配送人名称
     */
    @ApiModelProperty(value = "配送人名称")
    private String deliveryName;

    /**
     * 配送人联系方式
     */
    @ApiModelProperty(value = "配送人联系方式")
    private String deliveryPhone;

    /**
     * 订单详情列表
     */
    @ApiModelProperty(value = "订单详情列表")
    private List<OrderDetailResp> orderDetails;

    @ApiModelProperty(value = "订单状态文字描述")
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
                return "已退货(不扣绩效)";
            case 800:
                return "已退货(扣绩效)";
            default:
                return "未知";
        }
    }
}