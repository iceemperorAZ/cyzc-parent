package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 会员表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-17 15:56:31
 */
@Table(name = "tb_buyer")
@Entity
@Data
public class Buyer implements Serializable {

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
     * 店铺名称
     */
    @Column(name = "shop_name")
    private String shopName;

    /**
     * 商户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 登录名
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 第三方唯一标识
     */
    @Column(name = "unique_id")
    private String uniqueId;

    /**
     * 手机号
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @Column(name = "mail")
    private String mail;

    /**
     * 头像
     */
    @Column(name = "head_uri")
    private String headUri;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 会员积分
     */
    @Column(name = "member_integral")
    private Integer memberIntegral;

    /**
     * 超级到期时间
     */
    @Column(name = "expiration_time")
    private Date expirationTime;

    /**
     * 会员类型
     */
    @Column(name = "member_type")
    private Integer memberType;

    /**
     * 注册来源
     */
    @Column(name = "register_source")
    private String registerSource;

    /**
     * 注册时间(创建时间)
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 推荐人Id
     */
    @Column(name = "recommend_user_id")
    private Long recommendUserId;

    /**
     * 销售员Id
     */
    @Column(name = "sale_user_id")
    private Long saleUserId;

    /**
     * 销售员
     */
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "sale_user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    /**
     * 是否可用 0：否，1：是
     */
    @Column(name = "is_available")
    private Boolean isAvailable;

    /**
     * 是否封停 0：否，1：是
     */
    @Column(name = "is_seal_up")
    private Boolean isSealUp;

    /**
     * 是否新用户
     */
    @Column(name = "is_new")
    private Boolean isNew;

    @Transient
    private String token;

    @Transient
    private String sessionKey;

}