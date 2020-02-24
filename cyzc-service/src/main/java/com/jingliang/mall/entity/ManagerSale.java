package com.jingliang.mall.entity;

import java.util.Date;
import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * 商户销售绑定表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-02-17 14:21:26
 */
@Entity
@Table(name = "tb_manager_sale")
@Data
public class ManagerSale implements Serializable {

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
	 * 区域经理Id
	 */
	@Column(name = "manager_id")
	private Long managerId;

	/**
	 * 销售Id
	 */
	@Column(name = "sale_id")
	private Long saleId;

	/**
	 * 绑定时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 解绑时间
	 */
	@Column(name = "untying_time")
	private Date untyingTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

	/**
	 * 更新人Id
	 */
	@Column(name = "update_user_id")
	private Long updateUserId;

	/**
	 * 更新人
	 */
	@Column(name = "update_user")
	private String updateUser;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 销售员
	 */
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "sale_id", referencedColumnName = "id", insertable = false, updatable = false)
	private User user;

}