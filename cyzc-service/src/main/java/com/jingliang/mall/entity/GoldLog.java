package com.jingliang.mall.entity;

import com.citrsw.annatation.ApiProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 签到日志
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-03 13:04:43
 */
@Entity
@Table(name = "tb_gold_log")
@Data
public class GoldLog implements Serializable {

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
     * 商户
     */
    @OneToOne(targetEntity = Buyer.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Buyer buyer;

    /**
     * 日志内容
     */
    @Column(name = "msg")
    private String msg;

    /**
     * 签到时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 是否可用 0：否，1：是
     */
    @Column(name = "is_available")
    private Boolean isAvailable;

    /**
     * 获得金币方式 100:签到，200：充值，300:赠送，400：下单返利，500：退货返还，600:抽奖
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 充值/支付金额(单位：分)
     */
    @Column(name = "money")
    private Integer money;

    /**
     * 获得金币
     */
    @Column(name = "gold")
    private Integer gold;

    /**
     * 微信支付单号
     */
    @Column(name = "pay_no")
    private String payNo;

    /**
     * 赠送操作员工Id
     */
    @Column(name = "give_id")
    private Long giveId;


}