package com.jingliang.mall.req;

import java.util.Date;
import lombok.Data;
import io.swagger.annotations.Api;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 线下订单
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-31 15:47:06
 */
@Api(value = "线下订单")
@Data
public class OfflineOrderReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiModelProperty(value = "主键Id")
	private Long id;

	/**
	 * 商铺名称
	 */
	@ApiModelProperty(value = "商铺名称")
	private String shopName;

	/**
	 * 客户姓名
	 */
	@ApiModelProperty(value = "客户姓名")
	private String customerName;

	/**
	 * 客户电话
	 */
	@ApiModelProperty(value = "客户电话")
	private String customerPhone;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String productName;

	/**
	 * 商品规格
	 */
	@ApiModelProperty(value = "商品规格")
	private String productSpecification;

	/**
	 * 单位
	 */
	@ApiModelProperty(value = "单位")
	private String company;

	/**
	 * 数量
	 */
	@ApiModelProperty(value = "数量")
	private Integer num;

	/**
	 * 单价(单位：分)
	 */
	@ApiModelProperty(value = "单价(单位：分)")
	private Integer unitPrice;

	/**
	 * 总价(单位：分)
	 */
	@ApiModelProperty(value = "总价(单位：分)")
	private Integer totalPrice;

	/**
	 * 省
	 */
	@ApiModelProperty(value = "省")
	private String province;

	/**
	 * 市
	 */
	@ApiModelProperty(value = "市")
	private String city;

	/**
	 * 区/县
	 */
	@ApiModelProperty(value = "区/县")
	private String county;

	/**
	 * 客户地址
	 */
	@ApiModelProperty(value = "客户地址")
	private String customerAddress;

	/**
	 * 客户要求送货日期和时间
	 */
	@ApiModelProperty(value = "客户要求送货日期和时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date deliveryTime;

	/**
	 * 业务员Id
	 */
	@ApiModelProperty(value = "业务员Id")
	private Long salesmanId;

	/**
	 * 业务员姓名
	 */
	@ApiModelProperty(value = "业务员姓名")
	private String salesmanName;

	/**
	 * 业务员工号
	 */
	@ApiModelProperty(value = "业务员工号")
	private String salesmanNo;

	/**
	 * 业务员电话
	 */
	@ApiModelProperty(value = "业务员电话")
	private String salesmanPhone;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remarks;

	/**
	 * 是否可用
	 */
	@ApiModelProperty(value = "是否可用")
	private Boolean isAvailable;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 开具发票进度（100：无需发票，200：待开发票，300：已开发票）
	 */
	@ApiModelProperty(value = "开具发票进度（100：无需发票，200：待开发票，300：已开发票）")
	private Integer rate;

	/**
	 * 发票类型（100：增值税专用发票，200：增值税普通发票 ）
	 */
	@ApiModelProperty(value = "发票类型（100：增值税专用发票，200：增值税普通发票 ）")
	private Integer type;

	/**
	 * 单位名称
	 */
	@ApiModelProperty(value = "单位名称")
	private String unitName;

	/**
	 * 纳税人识别码
	 */
	@ApiModelProperty(value = "纳税人识别码")
	private String taxpayerIdentificationNumber;

	/**
	 * 注册地址
	 */
	@ApiModelProperty(value = "注册地址")
	private String registeredAddress;

	/**
	 * 注册电话
	 */
	@ApiModelProperty(value = "注册电话")
	private String registeredTelephone;

	/**
	 * 开户银行
	 */
	@ApiModelProperty(value = "开户银行")
	private String bankOfDeposit;

	/**
	 * 银行账户
	 */
	@ApiModelProperty(value = "银行账户")
	private String bankAccount;

	/**
	 * 联系人
	 */
	@ApiModelProperty(value = "联系人")
	private String contacts;

	/**
	 * 联系电话
	 */
	@ApiModelProperty(value = "联系电话")
	private String contactNumber;

	/**
	 * 快递地址
	 */
	@ApiModelProperty(value = "快递地址")
	private String expressAddress;

	/**
	 * 锁定
	 */
	@ApiModelProperty(value = "锁定")
	private Boolean enable;

}