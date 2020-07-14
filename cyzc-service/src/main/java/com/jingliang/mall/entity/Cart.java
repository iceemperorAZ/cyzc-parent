package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户购物车
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
@Entity
@Data
@Table(name = "tb_cart")
public class Cart implements Serializable {

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
     * 用户Id
     */
    @Column(name = "buyer_id")
    private Long buyerId;

    /**
     * 商品分类Id
     */
    @Column(name = "product_type_id")
    private Long productTypeId;

    /**
     * 商品Id
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 商品数量
     */
    @Column(name = "product_num")
    private Integer productNum;

    /**
     * 添加时间(创建时间)
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

}