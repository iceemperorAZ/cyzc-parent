package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 会员收货地址表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-23 16:51:26
 */
@Entity
@Table(name = "tb_buyer_address")
@Data
public class BuyerAddress implements Serializable {

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
	 * 会员Id
	 */
	@Column(name = "buyer_id")
	private Long buyerId;

	/**
	 * 省编码
	 */
	@Column(name = "province_code")
	private String provinceCode;

    /**
     * 省信息
     */
    @OneToOne(targetEntity = Region.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "province_code", referencedColumnName = "code", insertable = false, updatable = false)
    private Region province;

    /**
     * 市编码
     */
    @Column(name = "city_code")
    private String cityCode;

    /**
     * 市信息
     */
    @OneToOne(targetEntity = Region.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "city_code", referencedColumnName = "code", insertable = false, updatable = false)
    private Region city;

    /**
     * 区/县编码
     */
    @Column(name = "area_code")
    private String areaCode;

    /**
     * 市信息
     */
    @OneToOne(targetEntity = Region.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "area_code", referencedColumnName = "code", insertable = false, updatable = false)
    private Region area;

    /**
     * 详细地址
     */
    @Column(name = "detailed_address")
    private String detailedAddress;

	/**
	 * 是否默认 0：否，1：是
	 */
	@Column(name = "is_default")
	private Boolean isDefault;

	/**
	 * 是否可用 0：否，1：是
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 收货人名称
	 */
	@Column(name = "consignee_name")
	private String consigneeName;

	/**
	 * 收货人电话
	 */
	@Column(name = "phone")
	private String phone;

	/**
	 * 邮编
	 */
	@Column(name = "post_code")
	private String postCode;

}