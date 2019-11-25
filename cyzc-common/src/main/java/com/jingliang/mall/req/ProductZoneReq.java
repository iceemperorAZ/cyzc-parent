package com.jingliang.mall.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商品区表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-29 19:46:43
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ProductZoneReq", description = "商品区")
@Data
public class ProductZoneReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    private Long id;

    /**
     * 主键Id-开始
     */
    @ApiModelProperty(value = "主键Id-开始")
    private Long idStart;

    /**
     * 主键Id-结束
     */
    @ApiModelProperty(value = "主键Id-结束")
    private Long idEnd;

    /**
     * 商品区名称
     */
    @ApiModelProperty(value = "商品区名称")
    private String productZoneName;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 排序-开始
     */
    @ApiModelProperty(value = "排序-开始")
    private Integer sortStart;

    /**
     * 排序-结束
     */
    @ApiModelProperty(value = "排序-结束")
    private Integer sortEnd;

    /**
     * 类型 100：商品，200：优惠券
     */
    @ApiModelProperty(value = "类型 100：商品，200：优惠券")
    private Integer type;

    /**
     * 类型 100：商品，200：优惠券-开始
     */
    @ApiModelProperty(value = "类型 100：商品，200：优惠券-开始")
    private Integer typeStart;

    /**
     * 类型 100：商品，200：优惠券-结束
     */
    @ApiModelProperty(value = "类型 100：商品，200：优惠券-结束")
    private Integer typeEnd;

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
    private Long createUserId;

    /**
     * 创建人Id-开始
     */
    @ApiModelProperty(value = "创建人Id-开始")
    private Long createUserIdStart;

    /**
     * 创建人Id-结束
     */
    @ApiModelProperty(value = "创建人Id-结束")
    private Long createUserIdEnd;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建时间-开始
     */
    @ApiModelProperty(value = "创建时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeStart;

    /**
     * 创建时间-结束
     */
    @ApiModelProperty(value = "创建时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeEnd;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateUserName;

    /**
     * 修改人Id
     */
    @ApiModelProperty(value = "修改人Id")
    private Long updateUserId;

    /**
     * 修改人Id-开始
     */
    @ApiModelProperty(value = "修改人Id-开始")
    private Long updateUserIdStart;

    /**
     * 修改人Id-结束
     */
    @ApiModelProperty(value = "修改人Id-结束")
    private Long updateUserIdEnd;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 修改时间-开始
     */
    @ApiModelProperty(value = "修改时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTimeStart;

    /**
     * 修改时间-结束
     */
    @ApiModelProperty(value = "修改时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTimeEnd;

    /**
     * 商品区目标Id列表
     */
    @ApiModelProperty(value = "商品区目标Id列表")
    private List<Long> targetIds;


}