package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 经纬度记录表
 *
 * @author Mengde Liu
 * @version 1.0.0
 * @date 2020-04-28 13:30:05
 */
@Entity
@Table(name = "tb_address_user_history")
@Data
public class AddressUserHistory implements Serializable {

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
     * 用户
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 存入时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * lng经度
     */
    @Column(name = "longitude")
    private Double longitude;

    /**
     * lat纬度
     */
    @Column(name = "latitude")
    private Double latitude;

    /**
     * 地址信息
     */
    @Column(name = "address")
    private String address;

    /**
     * 是否可用 0：否，1：是
     */
    @Column(name = "is_available")
    private Boolean isAvailable;
    /**
     * 等级
     */
    @Column(name = "level")
    private Integer level;
}