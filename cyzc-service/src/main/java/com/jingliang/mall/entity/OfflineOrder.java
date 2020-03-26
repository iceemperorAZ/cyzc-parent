package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 线下订单
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-24 14:49:09
 */
@Entity
@Table(name = "tb_offline_order")
@Data
public class OfflineOrder implements Serializable {

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
     * 商铺名称
     */
    @Column(name = "shop_name")
    private String shopName;

    /**
     * 客户姓名
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 客户电话
     */
    @Column(name = "customer_phone")
    private String customerPhone;

    /**
     * 商品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 商品规格
     */
    @Column(name = "product_specification")
    private String productSpecification;

    /**
     * 单位
     */
    @Column(name = "company")
    private String company;

    /**
     * 数量
     */
    @Column(name = "num")
    private String num;

    /**
     * 单价(单位：分)
     */
    @Column(name = "unit_price")
    private String unitPrice;

    /**
     * 总价(单位：分)
     */
    @Column(name = "total_price")
    private Integer totalPrice;

    /**
     * 客户地址
     */
    @Column(name = "customer_address")
    private String customerAddress;

    /**
     * 客户要求送货日期和时间
     */
    @Column(name = "delivery_time")
    private Date deliveryTime;

    /**
     * 业务员Id
     */
    @Column(name = "salesman_id")
    private Long salesmanId;

    /**
     * 业务员姓名
     */
    @Column(name = "salesman_name")
    private String salesmanName;

    /**
     * 业务员工号
     */
    @Column(name = "salesman_no")
    private String salesmanNo;

    /**
     * 业务员电话
     */
    @Column(name = "salesman_phone")
    private String salesmanPhone;

    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;

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

}