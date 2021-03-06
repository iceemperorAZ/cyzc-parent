package com.jingliang.mall.entity;

import lombok.Data;
import lombok.experimental.Accessors;
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
@Accessors(chain = true)
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
     * 商户
     */
    @OneToOne(targetEntity = Buyer.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Buyer buyer;

    /**
     * 收货地址：省
     */
    @Column(name = "detail_address_province")
    private String detailAddressProvince;

    /**
     * 收货地址：市
     */
    @Column(name = "detail_address_city")
    private String detailAddressCity;

    /**
     * 收货地址：区
     */
    @Column(name = "detail_address_area")
    private String detailAddressArea;

    /**
     * 收货地址：街道
     */
    @Column(name = "detail_address_street")
    private String detailAddressStreet;

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
     * 使用优惠券Id集合 多个之间使用,分割
     */
    @Column(name = "coupon_ids")
    private String couponIds;

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
     * 支付方式 100:微信,200:其他
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
     * 修改人
     */
    @Column(name = "update_user_name")
    private String updateUserName;

    /**
     * 修改人Id
     */
    @Column(name = "update_user_id")
    private Long updateUserId;

    /**
     * 发货仓库
     */
    @Column(name = "storehouse")
    private String storehouse;

    /**
     * 是否使用金币
     */
    @Column(name = "is_gold")
    private Boolean isGold;

    /**
     * 使用金币数
     */
    @Column(name = "gold")
    private Integer gold;

    /**
     * 返金币数
     */
    @Column(name = "return_gold")
    private Integer returnGold;

    /**
     * 所在分组编号
     */
    @Column(name = "group_no")
    private String groupNo;

    /**
     * 销售人员Id
     */
    @Column(name = "sale_user_id")
    private Long saleUserId;


    /**
     * 销售员
     */
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "sale_user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User sale;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 提成比例（千分）
     */
    @Column(name = "ratio")
    private Integer ratio;

    /**
     * 订单详情集合
     */
    @OneToMany(targetEntity = OrderDetail.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<OrderDetail> orderDetails;


}