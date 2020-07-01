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
 * 金币获取日志
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-03 13:04:43
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(name = "GoldLogResp", description = "金币获取日志")
@Data
public class GoldLogResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiProperty(description = "主键Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 商户Id
     */
    @ApiProperty(description = "商户Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long buyerId;

    /**
     * 商户
     */
    @ApiProperty(description = "商户")
    private BuyerResp buyer;

    /**
     * 日志内容
     */
    @ApiProperty(description = "日志内容")
    private String msg;

    /**
     * 签到时间
     */
    @ApiProperty(description = "签到时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiProperty(description = "是否可用 0：否，1：是")
    private Boolean isAvailable;

    /**
     * 充值金额
     */
    @ApiProperty(description = "充值金额")
    private Integer money;

    /**
     * 获得金币
     */
    @ApiProperty(description = "获得金币")
    private Integer gold;
}