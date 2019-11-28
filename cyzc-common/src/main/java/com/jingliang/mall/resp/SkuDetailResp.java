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
 * 库存详情
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 19:00:48
 */
@ApiModel(value = "SkuDetailResp", description = "库存详情")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SkuDetailResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

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
     * 库存Id
     */
    @ApiModelProperty(value = "库存Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long skuId;

    /**
     * 库存编号
     */
    @ApiModelProperty(value = "库存编号")
    private String skuNo;

    /**
     * 批号
     */
    @ApiModelProperty(value = "批号")
    private String batchNumber;

    /**
     * 追加库存数量
     */
    @ApiModelProperty(value = "追加库存数量")
    private Integer skuAppendNum;

    /**
     * 剩余库存数量
     */
    @ApiModelProperty(value = "剩余库存数量")
    private Integer skuResidueNum;

    /**
     * 进价
     */
    @ApiModelProperty(value = "进价")
    private Double purchasePrice;

    /**
     * 生产时间
     */
    @ApiModelProperty(value = "生产时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date productionTime;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expiredTime;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public Double getPurchasePrice() {
        return purchasePrice / 100;
    }
}