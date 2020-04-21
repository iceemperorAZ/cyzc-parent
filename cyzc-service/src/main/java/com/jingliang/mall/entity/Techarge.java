package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 充值配置
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 10:14:25
 */
@Entity
@Data
@Table(name = "tb_techarge")
public class Techarge implements Serializable {

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
     * 金额
     */
    @Column(name = "money")
    private Integer money;

    /**
     * 金币
     */
    @Column(name = "gold")
    private Integer gold;

    /**
     * 是否可用 0：否，1：是
     */
    @Column(name = "is_available")
    private Boolean isAvailable;

    /**
     * 上架
     */
    @Column(name = "is_show")
    private Boolean isShow;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 上架人Id
     */
    @Column(name = "show_id")
    private Long showId;

    /**
     * 创建人
     */
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "show_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User showUser;

    /**
     * 上架时间
     */
    @Column(name = "show_time")
    private Date showTime;

    /**
     * 创建人Id
     */
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建人
     */
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "create_user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User createUser;

}