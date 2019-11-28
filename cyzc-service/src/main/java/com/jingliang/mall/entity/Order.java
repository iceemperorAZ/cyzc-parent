package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
@Table(name = "tb_order")
@Entity
@Data
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.jingliang.mall.common.IdGenerator")
    @Id
    private Long id;

    /**
     * 第三方订支付单号/预支付单号
     */
    @Column(name = "pay_no")
    private String payNo;

    /**
     * 订单编号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 用户Id
     */
    @Column(name = "buyer_id")
    private Long buyerId;

    /**
     * 收货详细地址信息
     */
    @Column(name = "detail_address")
    private String detailAddress;

    /**
     * 收货人
     */
    @Column(name = "receiver_name")
    private String receiverName;

    /**
     * 收货人电话
     */
    @Column(name = "receiver_phone")
    private String receiverPhone;

    /**
     * 订单金额(单位：分)
     */
    @Column(name = "total_price")
    private Long totalPrice;

    /**
     * 应付金额(单位：分)
     */
    @Column(name = "payable_fee")
    private Long payableFee;

    /**
     * 优惠金额（单位：分）
     */
    @Column(name = "preferential_fee")
    private Long preferentialFee;

    /**
     * 用户优惠券Id
     */
    @Column(name = "coupon_id")
    private Long couponId;

    /**
     * 商品数量
     */
    @Column(name = "product_num")
    private Integer productNum;

    /**
     * 运费
     */
    @Column(name = "deliver_fee")
    private Long deliverFee;

    /**
     * 支付方式 100:微信,200:现金
     */
    @Column(name = "pay_way")
    private Integer payWay;

    /**
     * 支付发起时间
     */
    @Column(name = "pay_start_time")
    private Date payStartTime;

    /**
     * 支付结束时间
     */
    @Column(name = "pay_end_time")
    private Date payEndTime;

    /**
     * 订单状态 100:待支付 200:已取消 300:已支付/待发货,400:已发货/待收货,500:已送达/待确认,600:已完成，700:已退货（不扣绩效），800:已退货（扣绩效）
     */
    @Column(name = "order_status")
    private Integer orderStatus;

    /**
     * 送货时间 100:只工作日收货（双休日，节假日不送） 200:只双休日，假日送货   300:工作日，双休日，假日均可送
     */
    @Column(name = "delivery_type")
    private Integer deliveryType;

    /**
     * 订单生成时间（创建时间）
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 订单完成时间（取消也算完成）
     */
    @Column(name = "finish_time")
    private Date finishTime;

    /**
     * 发货时间
     */
    @Column(name = "deliver_goods_time")
    private Date deliverGoodsTime;

    /**
     * 预计送达时间
     */
    @Column(name = "expected_delivery_time")
    private Date expectedDeliveryTime;

    /**
     * 附言
     */
    @Column(name = "note")
    private String note;

    /**
     * 是否可用 0：否，1：是
     */
    @Column(name = "is_available")
    private Boolean isAvailable;

    /**
     * 配送人名称
     */
    @Column(name = "delivery_name")
    private String deliveryName;

    /**
     * 配送人联系方式
     */
    @Column(name = "delivery_phone")
    private String deliveryPhone;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 订单详情集合
     */
    @OneToMany(targetEntity = OrderDetail.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<OrderDetail> orderDetails;


}