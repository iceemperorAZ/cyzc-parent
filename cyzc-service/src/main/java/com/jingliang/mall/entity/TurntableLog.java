package com.jingliang.mall.entity;

import java.time.LocalDateTime;
import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * 转盘日志
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 11:40:28
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
	 * 消息内容
	 */
	@Column(name = "msg")
	private String msg;

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
	 * 类型(100:谢谢惠顾,200:金币，300:返币次数，400：商品[为商品时需配置奖品Id],)
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

}