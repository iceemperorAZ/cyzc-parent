package com.jingliang.mall.req;

import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 赠送金币
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 14:57:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(name = "GiveGoldReq", description = "赠送金币")
public class GiveGoldReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiProperty(description = "主键Id")
    private Long id;

    /**
     * 主键Id-开始
     */
    @ApiProperty(description = "主键Id-开始")
    private Long idStart;

    /**
     * 主键Id-结束
     */
    @ApiProperty(description = "主键Id-结束")
    private Long idEnd;

    /**
     * 商户Id
     */
    @ApiProperty(description = "商户Id")
    private Long buyerId;

    /**
     * 商户Id-开始
     */
    @ApiProperty(description = "商户Id-开始")
    private Long buyerIdStart;

    /**
     * 商户Id-结束
     */
    @ApiProperty(description = "商户Id-结束")
    private Long buyerIdEnd;

    /**
     * 内容
     */
    @ApiProperty(description = "内容")
    private String msg;

    /**
     * 金币数
     */
    @ApiProperty(description = "金币数")
    private Integer gold;

    /**
     * 金币数-开始
     */
    @ApiProperty(description = "金币数-开始")
    private Integer goldStart;

    /**
     * 金币数-结束
     */
    @ApiProperty(description = "金币数-结束")
    private Integer goldEnd;

    /**
     * 创建人Id
     */
    @ApiProperty(description = "创建人Id")
    private Long createId;

    /**
     * 创建人Id-开始
     */
    @ApiProperty(description = "创建人Id-开始")
    private Long createIdStart;

    /**
     * 创建人Id-结束
     */
    @ApiProperty(description = "创建人Id-结束")
    private Long createIdEnd;

    /**
     * 创建时间
     */
    @ApiProperty(description = "创建时间")
    private Date createTime;

    /**
     * 是审批状态(100:待审批，200：审批驳回，300：审批通过)
     */
    @ApiProperty(description = "审批状态(100:待审批，200：审批驳回，300：审批通过)")
    private Integer isApproval;

    /**
     * 审批人Id
     */
    @ApiProperty(description = "审批人Id")
    private Long approvalId;

    /**
     * 审批人Id-开始
     */
    @ApiProperty(description = "审批人Id-开始")
    private Long approvalIdStart;

    /**
     * 审批人Id-结束
     */
    @ApiProperty(description = "审批人Id-结束")
    private Long approvalIdEnd;

    /**
     * 审批时间
     */
    @ApiProperty(description = "审批时间")
    private Date approvalTime;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiProperty(description = "是否可用 0：否，1：是")
    private Boolean isAvailable;

}