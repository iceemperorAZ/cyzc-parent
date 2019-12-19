package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户优惠券使用限制
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-19 09:17:25
 */
@Entity
@Table(name = "tb_buyer_coupon_limit")
@Data
public class BuyerCouponLimit implements Serializable {

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
     * 商户Id
     */
    @Column(name = "buyer_id")
    private Long buyerId;

    /**
     * 商品分类Id
     */
    @Column(name = "product_type_id")
    private Long productTypeId;

    /**
     * 商品分类
     */
    @OneToOne(targetEntity = ProductType.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    @Where(clause = "is_available = 1")
    private ProductType productType;

    /**
     * 限制张数
     */
    @Column(name = "use_limit")
    private Integer useLimit;

    /**
     * 是否可用 0：否，1：是
     */
    @Column(name = "is_available")
    private Boolean isAvailable;

    /**
     * 创建人
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 创建人Id
     */
    @Column(name = "create_user_Id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

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
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

}