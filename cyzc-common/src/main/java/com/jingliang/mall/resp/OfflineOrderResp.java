package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 线下订单
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-31 15:47:06
 */
@Api(description = "线下订单")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class OfflineOrderResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiProperty(description = "主键Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 商铺名称
     */
    @ApiProperty(description = "商铺名称")
    private String shopName;

    /**
     * 客户姓名
     */
    @ApiProperty(description = "客户姓名")
    private String customerName;

    /**
     * 客户电话
     */
    @ApiProperty(description = "客户电话")
    private String customerPhone;

    /**
     * 商品名称
     */
    @ApiProperty(description = "商品名称")
    private String productName;

    /**
     * 商品规格
     */
    @ApiProperty(description = "商品规格")
    private String productSpecification;

    /**
     * 单位
     */
    @ApiProperty(description = "单位")
    private String company;

    /**
     * 数量
     */
    @ApiProperty(description = "数量")
    private String num;

    /**
     * 单价(单位：分)
     */
    @ApiProperty(description = "单价(单位：分)")
    private String unitPrice;

    /**
     * 总价(单位：分)
     */
    @ApiProperty(description = "总价(单位：分)")
    private String totalPrice;

    /**
     * 省
     */
    @ApiProperty(description = "省")
    private String province;

    /**
     * 市
     */
    @ApiProperty(description = "市")
    private String city;

    /**
     * 区/县
     */
    @ApiProperty(description = "区/县")
    private String county;

    /**
     * 客户地址
     */
    @ApiProperty(description = "客户地址")
    private String customerAddress;

    /**
     * 客户要求送货日期和时间
     */
    @ApiProperty(description = "客户要求送货日期和时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliveryTime;

    /**
     * 业务员Id
     */
    @ApiProperty(description = "业务员Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long salesmanId;

    /**
     * 业务员姓名
     */
    @ApiProperty(description = "业务员姓名")
    private String salesmanName;

    /**
     * 业务员工号
     */
    @ApiProperty(description = "业务员工号")
    private String salesmanNo;

    /**
     * 业务员电话
     */
    @ApiProperty(description = "业务员电话")
    private String salesmanPhone;

    /**
     * 备注
     */
    @ApiProperty(description = "备注")
    private String remarks;

    /**
     * 是否可用
     */
    @ApiProperty(description = "是否可用")
    private Boolean isAvailable;

    /**
     * 创建时间
     */
    @ApiProperty(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 开具发票进度（100：无需发票，200：待开发票，300：已开发票）
     */
    @ApiProperty(description = "开具发票进度（100：无需发票，200：待开发票，300：已开发票）")
    private Integer rate;

    /**
     * 发票类型（100：增值税专用发票，200：增值税普通发票 ）
     */
    @ApiProperty(description = "发票类型（100：增值税专用发票，200：增值税普通发票 ）")
    private Integer type;

    /**
     * 单位名称
     */
    @ApiProperty(description = "单位名称")
    private String unitName;

    /**
     * 纳税人识别码
     */
    @ApiProperty(description = "纳税人识别码")
    private String taxpayerIdentificationNumber;

    /**
     * 注册地址
     */
    @ApiProperty(description = "注册地址")
    private String registeredAddress;

    /**
     * 注册电话
     */
    @ApiProperty(description = "注册电话")
    private String registeredTelephone;

    /**
     * 开户银行
     */
    @ApiProperty(description = "开户银行")
    private String bankOfDeposit;

    /**
     * 银行账户
     */
    @ApiProperty(description = "银行账户")
    private String bankAccount;

    /**
     * 联系人
     */
    @ApiProperty(description = "联系人")
    private String contacts;

    /**
     * 联系电话
     */
    @ApiProperty(description = "联系电话")
    private String contactNumber;

    /**
     * 快递地址
     */
    @ApiProperty(description = "快递地址")
    private String expressAddress;

    /**
     * 锁定
     */
    @ApiProperty(description = "锁定")
    private Boolean enable;

    /**
     * 大区
     */
    @ApiProperty(description = "大区")
    private String region;

    /**
     * 订单状态
     */
    @ApiProperty(description = "订单状态")
    private String orderStatus;

}