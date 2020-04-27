package com.jingliang.mall.entity;

import java.util.Date;
import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * 退货表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-20 17:57:20
 */
@Entity
@Table(name = "tb_offline_order_return")
@Data
public class OfflineOrderReturn implements Serializable {

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
	 * 原订单Id
	 */
	@Column(name = "order_id")
	private Long orderId;

	/**
	 * 商品名称
	 */
	@Column(name = "product_name")
	private String productName;

	/**
	 * 商品规格
	 */
	@Column(name = "product_specification")
	private String productSpecification;

	/**
	 * 单位
	 */
	@Column(name = "company")
	private String company;

	/**
	 * 数量
	 */
	@Column(name = "num")
	private String num;

	/**
	 * 单价(单位：分)
	 */
	@Column(name = "unit_price")
	private String unitPrice;

	/**
	 * 总价(单位：分)
	 */
	@Column(name = "total_price")
	private String totalPrice;

	/**
	 * 状态(100：退货待审批，201：驳回，200：通过/退款待审批，301：驳回，300：退货中，400：退货完成/退款中，500：退款完成/结束)
	 */
	@Column(name = "order_status")
	private Integer orderStatus;

	/**
	 * 操作人Id
	 */
	@Column(name = "create_user_id")
	private Long createUserId;

	/**
	 * 操作时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 退款人Id
	 */
	@Column(name = "refund_user_id")
	private Long refundUserId;

	/**
	 * 退款时间
	 */
	@Column(name = "refund_time")
	private Date refundTime;

	/**
	 * 退货原因
	 */
	@Column(name = "reason")
	private String reason;

	/**
	 * 退货审批人Id
	 */
	@Column(name = "return_goods_id")
	private Long returnGoodsId;

	/**
	 * 退货审批意见
	 */
	@Column(name = "return_goods_opinion")
	private String returnGoodsOpinion;

	/**
	 * 退款审批人Id
	 */
	@Column(name = "refund_id")
	private Long refundId;

	/**
	 * 退款审批意见
	 */
	@Column(name = "refund_opinion")
	private String refundOpinion;

	/**
	 * 是否可用
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

}