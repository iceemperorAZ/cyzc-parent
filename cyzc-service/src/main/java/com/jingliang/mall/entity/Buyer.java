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
    private User sale;

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

    /**
     * 最后一次下单时间
     */
    @Column(name = "last_order_time")
    private Date lastOrderTime;

    /**
     * 金币
     */
    @Column(name = "gold")
    private Integer gold;

    /**
     * 返利剩余次数
     */
    @Column(name = "order_specific_num")
    private Integer orderSpecificNum;

    /**
     * 会员等级（100：普通，200：银牌，300：金牌）
     */
    @Column(name = "member_level")
    private Integer memberLevel;

    /**
     * 商铺详细地址
     */
    @Column(name = "ship_addr")
    private String shipAddress;

    /**
     * 是否可以货到付款 0：否，1：是
     */
    @Column(name = "is_cash_on_delivery")
    private Boolean isCashOnDelivery;

    /**
     * 后台用户修改时间
     */
    @Column(name = "back_update_time")
    private Date backUpdateTime;

    /**
     * 后台修改人id
     */
    @Column(name = "back_update_user_id")
    private Long backUpdateUserId;

    /**
     * 商铺图片url集合
     */
    @Column(name = "buyer_img_urls")
    private String buyerImdUrls;

    /**
     * 审核人id
     */
    @Column(name = "review_user_id")
    private Long reviewUserId;

    /**
     * 审核时间
     */
    @Column(name = "review_time")
    private Date reviewTime;

    /**
     * 审核意见
     */
    @Column(name = "review_msg")
    private String reviewMsg;

    /**
     * 商户状态：100待审核，300审核通过
     */
    @Column(name = "buyer_status")
    private Integer buyerStatus;

    @Transient
    private String token;

    @Transient
    private String sessionKey;

}