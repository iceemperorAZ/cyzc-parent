package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单详情表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-19 17:44:18
 */
@Entity
@Table(name = "tb_order_detail")
@Data
public class OrderDetail implements Serializable {

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
     * 订单编号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 订单Id
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 商品Id
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 商品售价
     */
    @Column(name = "selling_price")
    private Long sellingPrice;

    /**
     * 商品数量
     */
    @Column(name = "product_num")
    private Integer productNum;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 是否可用 0：否，1：是
     */
    @Column(name = "is_available")
    private Boolean isAvailable;

    /**
     * 商品信息
     */
    @OneToOne(targetEntity = Product.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;


    @Transient
    private Long difference;
}