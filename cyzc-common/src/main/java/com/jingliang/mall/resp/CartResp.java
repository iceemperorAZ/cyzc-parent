package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户购物车
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(name = "Cart", description = "用户购物车")
public class CartResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiProperty(description = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户Id
     */
    @ApiProperty(description = "用户Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long buyerId;

    /**
     * 商品分类Id
     */
    @ApiProperty(description = "商品分类Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productTypeId;

    /**
     * 商品Id
     */
    @ApiProperty(description = "商品Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productId;

    /**
     * 商品数量
     */
    @ApiProperty(description = "商品数量")
    private Integer productNum;

    /**
     * 添加时间(创建时间)
     */
    @ApiProperty(description = "添加时间(创建时间)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiProperty(description = "是否可用 0：否，1：是")
    private Boolean isAvailable;

    /**
     * 商品信息
     */
    @ApiProperty(description = "商品信息")
    private ProductResp product;

	/**
	 * 是否选中
	 */
	@ApiProperty(description = "是否选中（一直为选中，后期再确定是否保存如数据库）")
	private Boolean isSelect = true;

    /**
     * 商品售价
     */
    @ApiProperty(description = "商品售价")
    private Double sellingPrice;

    public Double getSellingPrice() {
        if(product!=null){
            return product.getSellingPrice();
        }
        return sellingPrice;
    }

    /**
     * 购物车数量
     */
    @ApiProperty(description = "购物车数量")
    private Integer counts;
}