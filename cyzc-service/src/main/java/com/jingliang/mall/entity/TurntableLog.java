package com.jingliang.mall.entity;

import java.util.Date;
import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * 转盘日志
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-01 09:39:37
 */
@Table(name = "tb_turntable_log")
@Entity
@Data
public class TurntableLog implements Serializable {

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
	 * 商户
	 */
	@OneToOne(targetEntity = Buyer.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "buyer_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Buyer buyer;

	/**
	 * 消息内容
	 */
	@Column(name = "msg")
	private String msg;

	/**
	 * 类型(100:谢谢惠顾,200:金币，300:返币次数，400：商品[为商品时需配置奖品Id],)
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 奖品Id
	 */
	@Column(name = "prize_id")
	private Long prizeId;

	/**
	 * 奖品名称
	 */
	@Column(name = "prize_name")
	private String prizeName;

	/**
	 * 奖品数量
	 */
	@Column(name = "prize_num")
	private Integer prizeNum;

	/**
	 * 消耗金币数量
	 */
	@Column(name = "gold")
	private Integer gold;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

}