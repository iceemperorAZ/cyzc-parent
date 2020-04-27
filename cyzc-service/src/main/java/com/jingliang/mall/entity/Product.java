package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 14:12:41
 */
@Table(name = "tb_product")
@Entity
@Data
public class Product implements Serializable {

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
     * 商品排序
     */
    @Column(name = "product_sort")
    private Integer productSort;

    /**
     * 商品区Id
     */
    @Column(name = "product_zone_id")
    private Long productZoneId;

    /**
     * 商品编号
     */
    @Column(name = "product_no")
    private String productNo;

    /**
     * 商品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 商品图片uri集合
     */
    @Column(name = "product_img_uris")
    private String productImgUris;

    /**
     * 市场价
     */
    @Column(name = "market_price")
    private Long marketPrice;

    /**
     * 销价
     */
    @Column(name = "selling_price")
    private Long sellingPrice;

    /**
     * 会员折扣率
     */
    @Column(name = "discount")
    private Integer discount;

    /**
     * 是否新品 0：否，1：是
     */
    //TODO isNew临时调整为已售空
    @Column(name = "is_new")
    private Boolean isNew;

    /**
     * 是否热销 0：否，1：是
     */
    @Column(name = "is_hot")
    private Boolean isHot;

    /**
     * 是否已上架 0：否，1：是
     */
    @Column(name = "is_show")
    private Boolean isShow;

    /**
     * 推荐 0：否，1：是
     */
    @Column(name = "is_recommend")
    private Boolean isRecommend;

    /**
     * 搜索词Id集合 多个之间以","分割
     */
    @Column(name = "keyword_ids")
    private String keywordIds;

    /**
     * 销量
     */
    @Column(name = "sales_volume")
    private Integer salesVolume;

    /**
     * 规格
     */
    @Column(name = "specs")
    private String specs;

    /**
     * 单位
     */
    @Column(name = "unit")
    private String unit;

    /**
     * 商品描述
     */
    @Column(name = "product_describe")
    private String productDescribe;

    /**
     * 商品属性集合，属性和值之间使用“:”分割，多个之间使用”;”分割
     */
    @Column(name = "attributes")
    private String attributes;

    /**
     * 审核状态 100:未提交审核，110：提交审核（审核中），120：审核通过，121：审核驳回
     */
    @Column(name = "examine_status")
    private Integer examineStatus;

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

    /**
     * 上架人
     */
    @Column(name = "show_user_name")
    private String showUserName;

    /**
     * 上架人Id
     */
    @Column(name = "show_user_id")
    private Long showUserId;

    /**
     * 上架时间
     */
    @Column(name = "show_time")
    private Date showTime;

    /**
     * 折扣价
     */
    @Column(name = "discount_show")
    private String discountShow;

    /**
     * 是否即将上架
     */
    @Column(name = "is_soon_show")
    //TODO isSoonShow临时调整为已抢光
    private Boolean isSoonShow;

    public Boolean getIsSoonShow() {
        return isSoonShow;
    }

    public void setIsSoonShow(Boolean isSoonShow) {
        this.isSoonShow = isSoonShow;
    }

    /**
     * 重量
     */
    @Column(name = "weight")
    private Integer weight;


    /**
     * 单用户每天购买上限制
     */
    @Column(name = "limit_num")
    private Integer limitNum;
}