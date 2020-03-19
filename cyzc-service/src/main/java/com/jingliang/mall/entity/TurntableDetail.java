package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 转盘详情
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@Entity
@Data
@Table(name = "tb_turntable_detail")
public class TurntableDetail implements Serializable {

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
     * 转盘Id
     */
    @Column(name = "turntable_id")
    private Long turntableId;

    /**
     * 转盘图片
     */
    @Column(name = "img")
    private String img;

    /**
     * 奖品Id
     */
    @Column(name = "prize_id")
    private Long prizeId;

    /**
     * 商品信息
     */
    @OneToOne(targetEntity = Product.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "prize_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;

    /**
     * 奖品名称
     */
    @Column(name = "prize_name")
    private String prizeName;

    /**
     * 类型(100:谢谢惠顾,200:金币，300:返币次数，400：商品[为商品时需配置奖品Id],)
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 概率
     */
    @Column(name = "probability")
    private Integer probability;

    /**
     * 奖品最多被抽到的次数
     */
    @Column(name = "prize_num")
    private Integer prizeNum;

    /**
     * 奖品基数
     */
    @Column(name = "base_num")
    private Integer baseNum;

    /**
     * 开始角度
     */
    @Column(name = "start_angle")
    private Integer startAngle;

    /**
     * 结束角度
     */
    @Column(name = "end_angle")
    private Integer endAngle;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人Id
     */
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 上架人
     */
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "create_user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User createUser;

    /**
     * 是否上架
     */
    @Column(name = "is_show")
    private Boolean isShow;

    /**
     * 上架人Id
     */
    @Column(name = "show_user_id")
    private Long showUserId;

    /**
     * 上架人
     */
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "show_user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User showUser;

    /**
     * 上架时间
     */
    @Column(name = "show_time")
    private Date showTime;

    /**
     * 是否可用 0：否，1：是
     */
    @Column(name = "is_available")
    private Boolean isAvailable;

}