package com.jingliang.mall.entity;

import java.util.Date;
import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * 优惠券
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-18 16:50:49
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
	 * 特定商品的id
	 */
	@Column(name = "product_id")
	private Long productId;

	/**
	 * 是否是单品优惠券，0：否，1：是
	 */
	@Column(name = "is_item")
	private Boolean isItem;

	/**
	 * 使用范围（商品分类）-1为全场通用
	 */
	@Column(name = "product_type_id")
	private Long productTypeId;

	/**
	 * 销售员
	 */
	@OneToOne(targetEntity = ProductType.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "product_type_id", referencedColumnName = "id", insertable = false, updatable = false)
	private ProductType productType;

	/**
	 * 优惠百分比
	 */
	@Column(name = "percentage")
	private Integer percentage;

	/**
	 * 可领取数
	 */
	@Column(name = "receive_num")
	private Integer receiveNum;

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
	 * 优惠券描述
	 */
	@Column(name = "coupon_describe")
	private String couponDescribe;

	/**
	 * 是否发布 0：否，1：是
	 */
	@Column(name = "is_release")
	private Boolean isRelease;

	/**
	 * 发放开始时间
	 */
	@Column(name = "provide_start_time")
	private Date provideStartTime;

	/**
	 * 发放结束时间
	 */
	@Column(name = "provide_end_time")
	private Date provideEndTime;

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

	/**
	 * 满减条件（单位：分）
	 */
	@Column(name = "full_decrement")
	private Long fullDecrement;

	/**
	 * 优惠金额（单位：分）
	 */
	@Column(name = "preferential_price")
	private Long preferentialPrice;
}