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
 * 赠送金币
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 14:57:03
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "GiveGoldResp", description = "赠送金币")
@Data
public class GiveGoldResp implements Serializable {

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
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String msg;

    /**
     * 金币数
     */
    @ApiModelProperty(value = "金币数")
    private Integer gold;

    /**
     * 创建人Id
     */
    @ApiModelProperty(value = "创建人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createId;

    @ApiModelProperty(value = "创建人")
    private UserResp createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 审批状态(100:待审批，200：审批驳回，300：审批通过)
     */
    @ApiModelProperty(value = "审批状态(100:待审批，200：审批驳回，300：审批通过)")
    private Integer isApproval;

    /**
     * 审批人Id
     */
    @ApiModelProperty(value = "审批人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long approvalId;

    @ApiModelProperty(value = "审批人")
    private UserResp approvalUser;
    /**
     * 审批时间
     */
    @ApiModelProperty(value = "审批时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date approvalTime;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiModelProperty(value = "是否可用 0：否，1：是")
    private Boolean isAvailable;

}