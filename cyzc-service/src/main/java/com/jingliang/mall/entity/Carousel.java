package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 轮播图配置
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-25 14:09:52
 */
@Entity
@Table(name = "tb_carousel")
@Data
public class Carousel implements Serializable {

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
     * 顺序号
     */
	@Column(name = "carousel_order")
    private Integer carouselOrder;

    /**
     * 图片url
     */
    @Column(name = "img_uri")
    private String imgUri;

    /**
     * 类型 100:仅展示，200：外网连接，300：商品，其他待定
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 描述
     */
    @Column(name = "carousel_describe")
    private String carouselDescribe;

    /**
     * 内容
     */
    @Column(name = "content")
    private String content;

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