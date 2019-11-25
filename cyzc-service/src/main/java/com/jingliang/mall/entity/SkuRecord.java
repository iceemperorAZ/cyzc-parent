package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 库存记录单
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-11 11:56:20
 */
@Entity
@Table(name = "tb_sku_record")
@Data
public class SkuRecord implements Serializable {

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
	 * 商品名称
	 */
	@Column(name = "product_name")
	private String productName;

	/**
	 * 商品Id
	 */
	@Column(name = "product_id")
	private Long productId;

	/**
	 * 库存Id
	 */
	@Column(name = "sku_id")
	private Long skuId;

	/**
	 * 库存编号
	 */
	@Column(name = "sku_no")
	private String skuNo;

	/**
	 * 库存详情Id
	 */
	@Column(name = "sku_detail_id")
	private Long skuDetailId;

	/**
	 * 库存详情批号
	 */
	@Column(name = "batch_num")
	private String batchNum;

	/**
	 * 类型 100：入库，200：出库
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 描述
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 数量
	 */
	@Column(name = "num")
	private Integer num;

	/**
	 * 状态  100：待确认，200：通过，300：驳回
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 审批意见
	 */
	@Column(name = "approve_opinion")
	private String approveOpinion;

	/**
	 * 审批人Id
	 */
	@Column(name = "approve_user_id")
	private Long approveUserId;

	/**
	 * 审批人
	 */
	@Column(name = "approve_user_name")
	private String approveUserName;

	/**
	 * 审批时间
	 */
	@Column(name = "approve_time")
	private Date approveTime;

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