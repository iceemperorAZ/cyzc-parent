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
 * 返币次数
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 14:57:03
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(name = "GiveGoldResp", description = "赠送金币")
@Data
public class GiveRebateResp implements Serializable {

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
     * 内容
     */
    @ApiProperty(description = "内容")
    private String msg;

    /**
     * 返币次数
     */
    @ApiProperty(description = "返币次数")
    private Integer rebate;

    /**
     * 创建人Id
     */
    @ApiProperty(description = "创建人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createId;

    @ApiProperty(description = "创建人")
    private UserResp createUser;

    /**
     * 创建时间
     */
    @ApiProperty(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 审批状态(100:待审批，200：审批驳回，300：审批通过)
     */
    @ApiProperty(description = "审批状态(100:待审批，200：审批驳回，300：审批通过)")
    private Integer approval;

    /**
     * 审批人Id
     */
    @ApiProperty(description = "审批人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long approvalId;

    @ApiProperty(description = "审批人")
    private UserResp approvalUser;
    /**
     * 审批时间
     */
    @ApiProperty(description = "审批时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date approvalTime;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiProperty(description = "是否可用 0：否，1：是")
    private Boolean isAvailable;

}