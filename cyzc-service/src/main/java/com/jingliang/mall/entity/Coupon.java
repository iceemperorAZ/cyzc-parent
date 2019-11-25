package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 优惠券
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-25 19:09:02
 */
@Table(name = "tb_coupon")
@Entity
@Data
public class Coupon implements Serializable {

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
	 * 商品区Id
	 */
	@Column(name = "product_zone_id")
	private Long productZoneId;

	/**
	 * 金额
	 */
	@Column(name = "money")
	private Double money;

	/**
	 * 优惠券剩余数量
	 */
	@Column(name = "residue_number")
	private Integer residueNumber;

	/**
	 * 优惠券总数量
	 */
	@Column(name = "total_number")
	private Integer totalNumber;

	/**
	 * 使用条件(最低使用价格)
	 */
	@Column(name = "use_condition")
	private Double useCondition;

	/**
	 * 优惠券类型  100:超级会员,200:普通用户，300:新建用户
	 */
	@Column(name = "coupon_type")
	private Integer couponType;

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
	 * 创建人名称
	 */
	@Column(name = "create_user_name")
	private String createUserName;

	/**
	 * 创建人Id
	 */
	@Column(name = "create_user_id")
	private Long createUserId;

	/**
	 * 修改人
	 */
	@Column(name = "update_user_name")
	private String updateUserName;

	/**
	 * 修改人Id
	 */
	@Column(name = "update_user_id")
	private Long updateUserId;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

}