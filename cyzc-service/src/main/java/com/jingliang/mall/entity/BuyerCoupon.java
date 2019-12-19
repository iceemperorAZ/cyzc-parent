package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户优惠券
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-18 16:50:49
 */
@Entity
@Table(name = "tb_buyer_coupon")
@Data
public class BuyerCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.jingliang.mall.common.IdGenerator")
    @Id
    private Long id;

    /**
     * 优惠券Id
     */
    @Column(name = "coupon_id")
    private Long couponId;

    /**
     * 使用范围（商品分类）
     */
    @Column(name = "product_type_id")
    private Long productTypeId;

    /**
     * 商品分类
     */
    @OneToOne(targetEntity = ProductType.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ProductType productType;

    /**
     * 用户Id
     */
    @Column(name = "buyer_id")
    private Long buyerId;

    /**
     * 优惠百分比
     */
    @Column(name = "percentage")
    private Long percentage;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 到期时间
     */
    @Column(name = "expiration_time")
    private Date expirationTime;

    /**
     * 优惠券描述
     */
    @Column(name = "coupon_describe")
    private String couponDescribe;

    /**
     * 剩余可用张数
     */
    @Column(name = "receive_num")
    private Integer receiveNum;

    /**
     * 是否可用 0：否，1：是
     */
    @Column(name = "is_available")
    private Boolean isAvailable;

    /**
     * 领取时间/赠送时间(创建时间)
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人Id(系统：-1)
     */
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建人(系统：系统)
     */
    @Column(name = "create_user")
    private String createUser;

}