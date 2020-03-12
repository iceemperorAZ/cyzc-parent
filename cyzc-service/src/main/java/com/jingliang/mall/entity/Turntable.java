package com.jingliang.mall.entity;

import java.time.LocalDateTime;
import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * 转盘
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 11:40:28
 */
@Entity
@Table(name = "tb_turntable")
@Data
public class Turntable implements Serializable {

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
	 * 概率
	 */
	@Column(name = "probability")
	private Integer probability;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 创建人Id
	 */
	@Column(name = "create_user_id")
	private Long createUserId;

	/**
	 * 是否上架
	 */
	@Column(name = "is_show")
	private Boolean isShow;

	/**
	 * 上架人Id
	 */
	@Column(name = "show_user_id")
	private Long showUserId;

	/**
	 * 上架时间
	 */
	@Column(name = "show_time")
	private LocalDateTime showTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

	/**
	 * 奖品数量
	 */
	@Column(name = "prize_num")
	private Integer prizeNum;

	/**
	 * 开始角度
	 */
	@Column(name = "start_angle")
	private Integer startAngle;

	/**
	 * 结束角度
	 */
	@Column(name = "end_angle")
	private Integer endAngle;

}