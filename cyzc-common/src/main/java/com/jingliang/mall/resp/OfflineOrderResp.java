package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 线下订单
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-25 15:38:48
 */
@Api(value = "线下订单")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class OfflineOrderResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    @JsonSerialize(using = ToStringSerializer.class)
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
    private String num;

    /**
     * 单价(单位：分)
     */
    @ApiModelProperty(value = "单价(单位：分)")
    private String unitPrice;

    /**
     * 总价(单位：分)
     */
    @ApiModelProperty(value = "总价(单位：分)")
    private Integer totalPrice;

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
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否可用
     */
    @ApiModelProperty(value = "是否可用")
    private Boolean isAvailable;

}