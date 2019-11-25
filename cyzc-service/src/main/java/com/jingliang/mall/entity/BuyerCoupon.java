package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户优惠券
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-28 13:36:23
 */
@Entity
@Table(name = "tb_buyer_coupon")
@Data
public class BuyerCoupon implements Serializable {

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
	 * 优惠券Id
	 */
	@Column(name = "coupon_id")
	private Long couponId;

	/**
	 * 用户Id
	 */
	@Column(name = "buyer_id")
	private Long buyerId;

	/**
	 * 金额
	 */
	@Column(name = "money")
	private Double money;

	/**
	 * 使用条件(最低使用价格)
	 */
	@Column(name = "use_condition")
	private Double useCondition;

	/**
	 * 开始时间
	 */
	@Column(name = "start_time")
	private Date startTime;

	/**
	 * 到期时间
	 */
	@Column(name = "expiration_time")
	private Date expirationTime;

	/**
	 * 使用范围（商品分类）
	 */
	@Column(name = "product_type_id")
	private Long productTypeId;

	/**
	 * 优惠券描述
	 */
	@Column(name = "coupon_describe")
	private String couponDescribe;

	/**
	 * 是否已使用 0：否，1：是
	 */
	@Column(name = "is_used")
	private Boolean isUsed;

	/**
	 * 是否可用 0：否，1：是
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

	/**
	 * 领取时间(创建时间)
	 */
	@Column(name = "create_time")
	private Date createTime;

}