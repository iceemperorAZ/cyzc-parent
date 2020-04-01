package com.jingliang.mall.entity;

import java.util.Date;
import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * 线下订单
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-31 15:47:06
 */
@Entity
@Table(name = "tb_offline_order")
@Data
public class OfflineOrder implements Serializable {

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
	 * 商铺名称
	 */
	@Column(name = "shop_name")
	private String shopName;

	/**
	 * 客户姓名
	 */
	@Column(name = "customer_name")
	private String customerName;

	/**
	 * 客户电话
	 */
	@Column(name = "customer_phone")
	private String customerPhone;

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
	private Integer num;

	/**
	 * 单价(单位：分)
	 */
	@Column(name = "unit_price")
	private Integer unitPrice;

	/**
	 * 总价(单位：分)
	 */
	@Column(name = "total_price")
	private Integer totalPrice;

	/**
	 * 省
	 */
	@Column(name = "province")
	private String province;

	/**
	 * 市
	 */
	@Column(name = "city")
	private String city;

	/**
	 * 区/县
	 */
	@Column(name = "county")
	private String county;

	/**
	 * 客户地址
	 */
	@Column(name = "customer_address")
	private String customerAddress;

	/**
	 * 客户要求送货日期和时间
	 */
	@Column(name = "delivery_time")
	private Date deliveryTime;

	/**
	 * 业务员Id
	 */
	@Column(name = "salesman_id")
	private Long salesmanId;

	/**
	 * 业务员姓名
	 */
	@Column(name = "salesman_name")
	private String salesmanName;

	/**
	 * 业务员工号
	 */
	@Column(name = "salesman_no")
	private String salesmanNo;

	/**
	 * 业务员电话
	 */
	@Column(name = "salesman_phone")
	private String salesmanPhone;

	/**
	 * 备注
	 */
	@Column(name = "remarks")
	private String remarks;

	/**
	 * 是否可用
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 开具发票进度（100：无需发票，200：待开发票，300：已开发票）
	 */
	@Column(name = "rate")
	private Integer rate;

	/**
	 * 发票类型（100：增值税专用发票，200：增值税普通发票 ）
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 单位名称
	 */
	@Column(name = "unit_name")
	private String unitName;

	/**
	 * 纳税人识别码
	 */
	@Column(name = "Taxpayer_identification_number")
	private String taxpayerIdentificationNumber;

	/**
	 * 注册地址
	 */
	@Column(name = "registered_address")
	private String registeredAddress;

	/**
	 * 注册电话
	 */
	@Column(name = "registered_telephone")
	private String registeredTelephone;

	/**
	 * 开户银行
	 */
	@Column(name = "bank_of_deposit")
	private String bankOfDeposit;

	/**
	 * 银行账户
	 */
	@Column(name = "bank_account")
	private String bankAccount;

	/**
	 * 联系人
	 */
	@Column(name = "contacts")
	private String contacts;

	/**
	 * 联系电话
	 */
	@Column(name = "contact_number")
	private String contactNumber;

	/**
	 * 快递地址
	 */
	@Column(name = "express_address")
	private String expressAddress;

	/**
	 * 锁定
	 */
	@Column(name = "enable")
	private Boolean enable;

}