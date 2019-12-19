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
 * 用户优惠券使用限制
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-19 09:17:25
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(value = "BuyerCouponLimitResp", description = "用户优惠券使用限制")
public class BuyerCouponLimitResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 商户Id
     */
    @ApiModelProperty(value = "商户Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long buyerId;

    /**
     * 商品分类Id
     */
    @ApiModelProperty(value = "商品分类Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productTypeId;

    /**
     * 限制张数
     */
    @ApiModelProperty(value = "限制张数")
    private Integer useLimit;

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

    @ApiModelProperty(value = "商品分类")
    private ProductTypeResp productType;

}