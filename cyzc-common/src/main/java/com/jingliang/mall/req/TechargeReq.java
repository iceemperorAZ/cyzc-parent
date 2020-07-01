package com.jingliang.mall.req;

import com.citrsw.annatation.ApiModelProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
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
@ApiModel(name = "TechargeReq", description = "充值配置")
@EqualsAndHashCode(callSuper = true)
@Data
public class TechargeReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiProperty(description = "主键Id")
    private Long id;

    /**
     * 金额
     */
    @ApiProperty(description = "金额")
    private Integer money;

    /**
     * 金币
     */
    @ApiProperty(description = "金币")
    private Integer gold;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiProperty(description = "是否可用 0：否，1：是")
    private Boolean isAvailable;

    /**
     * 上架
     */
    @ApiProperty(description = "上架")
    private Boolean isShow;

    /**
     * 创建时间
     */
    @ApiProperty(description = "创建时间")
    private Date createTime;

    /**
     * 上架人Id
     */
    @ApiProperty(description = "上架人Id")
    private Long showId;

    /**
     * 上架时间
     */
    @ApiProperty(description = "上架时间")
    private Date showTime;

    /**
     * 创建人Id
     */
    @ApiProperty(description = "创建人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;
}