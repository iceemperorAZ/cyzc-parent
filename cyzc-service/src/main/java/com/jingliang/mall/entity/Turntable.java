package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 转盘
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@Entity
@Table(name = "tb_turntable")
@Data
public class Turntable implements Serializable {

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
     * 转盘图片
     */
    @Column(name = "img")
    private String img;

    /**
     * 描述
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 转盘单次抽取需要的金币
     */
    @Column(name = "gold")
    private Integer gold;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 更新人Id
     */
    @Column(name = "update_user_id")
    private Long updateUserId;

    /**
     * 创建人
     */
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "update_user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User updateUser;

    /**
     * 是否可用 0：否，1：是
     */
    @Column(name = "is_available")
    private Boolean isAvailable;

}