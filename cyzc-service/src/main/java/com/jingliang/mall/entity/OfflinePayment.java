package com.jingliang.mall.entity;

import java.util.Date;
import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * 线下支付
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-06 15:30:50
 */
@Entity
@Data
@Table(name = "tb_offline_payment")
public class OfflinePayment implements Serializable {

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
	 * 订单Id
	 */
	@Column(name = "order_id")
	private Long orderId;

	/**
	 * 订单编号
	 */
	@Column(name = "order_no")
	private String orderNo;

	/**
	 * 比如：xx银行，微信，支付宝，等等
	 */
	@Column(name = "pay_way")
	private String payWay;

	/**
	 * 凭证urls,多个之间用,分割(最多5张)
	 */
	@Column(name = "urls")
	private String urls;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 是否可用 0：否，1：是
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

	/**
	 * 创建人
	 */
	@Column(name = "create_user_name")
	private String createUserName;

	/**
	 * 创建人Id
	 */
	@Column(name = "create_user_Id")
	private Long createUserId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

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