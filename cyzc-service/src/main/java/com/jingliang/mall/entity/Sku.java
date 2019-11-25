package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品库存表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 14:12:41
 */
@Entity
@Table(name = "tb_sku")
@Data
public class Sku implements Serializable {

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
     * 库存编号
     */
    @Column(name = "sku_no")
    private String skuNo;

    /**
     * 商品分类Id
     */
    @Column(name = "product_type_id")
    private Long productTypeId;

    /**
     * 商品分类名称
     */
    @Column(name = "product_type_name")
    private String productTypeName;

    /**
     * 商品Id
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 商品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 历史库存总数量
     */
    @Column(name = "sku_history_total_num")
    private Integer skuHistoryTotalNum;

    /**
     * 线上库存总数量
     */
    @Column(name = "sku_line_num")
    private Integer skuLineNum;

    /**
     * 实际库存总数量
     */
    @Column(name = "sku_reality_num")
    private Integer skuRealityNum;

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