package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * 签到日志
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-03 10:42:56
 */
@Entity
@Table(name = "tb_sign_in")
@Data
public class SignIn implements Serializable {

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
     * 连续签到天数
     */
    @Column(name = "day_num")
    private Integer dayNum;

    /**
     * 最后一次签到日期
     */
    @Column(name = "last_date")
    private Date lastDate;

    /**
     * 是否可用 0：否，1：是
     */
    @Column(name = "is_available")
    private Boolean isAvailable;

}