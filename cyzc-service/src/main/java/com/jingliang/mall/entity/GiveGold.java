package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 赠送金币
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 14:57:03
 */
@Entity
@Table(name = "tb_give_gold")
@Data
public class GiveGold implements Serializable {

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
     * 商户Id
     */
    @Column(name = "buyer_id")
    private Long buyerId;

    /**
     * 内容
     */
    @Column(name = "msg")
    private String msg;

    /**
     * 金币数
     */
    @Column(name = "gold")
    private Integer gold;

    /**
     * 创建人Id
     */
    @Column(name = "create_id")
    private Long createId;

    /**
     * 创建人I
     */
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "create_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User createUser;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 审批状态(100:待审批，200：审批驳回，300：审批通过)
     */
    @Column(name = "approval")
    private Integer approval;

    /**
     * 审批人Id
     */
    @Column(name = "approval_id")
    private Long approvalId;

    /**
     * 创建人I
     */
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "approval_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User approvalUser;

    /**
     * 审批时间
     */
    @Column(name = "approval_time")
    private Date approvalTime;

    /**
     * 是否可用 0：否，1：是
     */
    @Column(name = "is_available")
    private Boolean isAvailable;

}