package com.jingliang.mall.entity;

import java.util.Date;
import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * 商户联系记录
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-01-15 10:17:00
 */
@Entity
@Table(name = "tb_contact_record")
@Data
public class ContactRecord implements Serializable {

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
	 * 联系结果
	 */
	@Column(name = "result")
	private Integer result;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 销售Id
	 */
	@Column(name = "saler_id")
	private Long salerId;

	/**
	 * 联系时间(创建时间)
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 创建人
	 */
	@Column(name = "create_user_name")
	private String createUserName;

	/**
	 * 创建人Id
	 */
	@Column(name = "create_user_id")
	private Long createUserId;

	/**
	 * 是否可用 0：否，1：是
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

}