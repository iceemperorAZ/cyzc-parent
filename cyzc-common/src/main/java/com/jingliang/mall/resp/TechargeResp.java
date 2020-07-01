package com.jingliang.mall.resp;

import com.citrsw.annatation.ApiModelProperty;
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
 * 充值配置
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 10:14:25
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(name = "TechargeResp", description = "充值配置")
@Data
public class TechargeResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiProperty(description = "主键Id")
    @JsonSerialize(using = ToStringSerializer.class)
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date showTime;

    /**
     * 创建人Id
     */
    @ApiProperty(description = "创建人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    /**
     * 上架人
     */
    @ApiProperty(description = "上架人")
    private UserResp showUser;

    /**
     * 创建人
     */
    @ApiProperty(description = "创建人")
    private UserResp createUser;


}