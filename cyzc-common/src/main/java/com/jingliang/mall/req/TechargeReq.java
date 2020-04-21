package com.jingliang.mall.req;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 充值配置
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 10:14:25
 */
@ApiModel(value = "TechargeReq", description = "充值配置")
@EqualsAndHashCode(callSuper = true)
@Data
public class TechargeReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    private Long id;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private Integer money;

    /**
     * 金币
     */
    @ApiModelProperty(value = "金币")
    private Integer gold;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiModelProperty(value = "是否可用 0：否，1：是")
    private Boolean isAvailable;

    /**
     * 上架
     */
    @ApiModelProperty(value = "上架")
    private Boolean isShow;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 上架人Id
     */
    @ApiModelProperty(value = "上架人Id")
    private Long showId;

    /**
     * 上架时间
     */
    @ApiModelProperty(name = "上架时间")
    private Date showTime;

    /**
     * 创建人Id
     */
    @ApiModelProperty(value = "创建人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;
}