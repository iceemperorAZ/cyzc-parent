package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单详情表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-19 17:44:18
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "OrderDetailResp", description = "订单详情表")
@Data
public class OrderDetailResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    /**
     * 订单Id
     */
    @ApiModelProperty(value = "订单Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;

    /**
     * 商品Id
     */
    @ApiModelProperty(value = "商品Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productId;

    /**
     * 商品售价
     */
    @ApiModelProperty(value = "商品售价")
    private Double sellingPrice;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer productNum;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiModelProperty(value = "是否可用 0：否，1：是")
    private Boolean isAvailable;

    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息")
    private ProductResp product;

    /**
     * 订单详情总价
     */
    @ApiModelProperty(value = "订单详情总价")
    public Double getTotalPrice() {
        return (sellingPrice * productNum) / 100;
    }

    public Double getSellingPrice() {
        return sellingPrice / 100;
    }
}