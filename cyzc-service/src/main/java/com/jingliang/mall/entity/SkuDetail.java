package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 库存详情
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 19:00:48
 */

@Entity
@Data
@Table(name = "tb_sku_detail")
public class SkuDetail implements Serializable {

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
     * 库存Id
     */
    @Column(name = "sku_id")
    private Long skuId;


    /**
     * 库存编号
     */
    @Column(name = "sku_no")
    private String skuNo;

    /**
     * 批号
     */
    @Column(name = "batch_number")
    private String batchNumber;

    /**
     * 追加库存数量
     */
    @Column(name = "sku_append_num")
    private Integer skuAppendNum;

    /**
     * 剩余库存数量
     */
    @Column(name = "sku_residue_num")
    private Integer skuResidueNum;

    /**
     * 进价
     */
    @Column(name = "purchase_price")
    private Long purchasePrice;

    /**
     * 生产时间
     */
    @Column(name = "production_time")
    private Date productionTime;

    /**
     * 过期时间
     */
    @Column(name = "expired_time")
    private Date expiredTime;

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