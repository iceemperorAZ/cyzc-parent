package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品库存表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 15:07:32
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "SkuResp", description = "商品库存表")
@Data
public class SkuResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 库存编号
     */
    @ApiModelProperty(value = "库存编号")
    private String skuNo;

    /**
     * 商品分类Id
     */
    @ApiModelProperty(value = "商品分类Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productTypeId;

    /**
     * 商品分类名称
     */
    @ApiModelProperty(value = "商品分类名称")
    private String productTypeName;

    /**
     * 商品Id
     */
    @ApiModelProperty(value = "商品Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 历史库存总数量
     */
    @ApiModelProperty(value = "历史库存总数量")
    private Integer skuHistoryTotalNum;

    /**
     * 线上库存总数量
     */
    @ApiModelProperty(value = "线上库存总数量")
    private Integer skuLineNum;

    /**
     * 实际库存总数量
     */
    @ApiModelProperty(value = "实际库存总数量")
    private Integer skuRealityNum;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiModelProperty(value = "是否可用 0：否，1：是")
    private Boolean isAvailable;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createUserName;

    /**
     * 创建人Id
     */
    @ApiModelProperty(value = "创建人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")

    private String updateUserName;

    /**
     * 修改人Id
     */
    @ApiModelProperty(value = "修改人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 补差
     */
    @ApiModelProperty(value = "补差")
    public Integer getDifferenceValue() {
        //与@Value("${product.sku.init.invented.num}")一致
        return Math.max((skuLineNum - 800000) * -1, 0);
    }

    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息")
    private ProductResp product;
}