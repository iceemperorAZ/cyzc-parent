package com.jingliang.mall.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品库存表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 14:12:41
 */
@ApiModel(name = "SkuReq", description = "商品库存表")
@EqualsAndHashCode(callSuper = true)
@Data
public class SkuReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiProperty(description = "主键")
    private Long id;

    /**
     * 主键-开始
     */
    @ApiProperty(description = "主键-开始")
    private Long idStart;

    /**
     * 主键-结束
     */
    @ApiProperty(description = "主键-结束")
    private Long idEnd;

    /**
     * 库存编号
     */
    @ApiProperty(description = "库存编号")
    private String skuNo;

    /**
     * 商品分类Id
     */
    @ApiProperty(description = "商品分类Id")
    private Long productTypeId;

    /**
     * 商品分类Id-开始
     */
    @ApiProperty(description = "商品分类Id-开始")
    private Long productTypeIdStart;

    /**
     * 商品分类Id-结束
     */
    @ApiProperty(description = "商品分类Id-结束")
    private Long productTypeIdEnd;

    /**
     * 商品分类名称
     */
    @ApiProperty(description = "商品分类名称")
    private String productTypeName;

    /**
     * 商品Id
     */
    @ApiProperty(description = "商品Id")
    private Long productId;

    /**
     * 商品Id-开始
     */
    @ApiProperty(description = "商品Id-开始")
    private Long productIdStart;

    /**
     * 商品Id-结束
     */
    @ApiProperty(description = "商品Id-结束")
    private Long productIdEnd;

    /**
     * 商品名称
     */
    @ApiProperty(description = "商品名称")
    private String productName;

    /**
     * 历史库存总数量
     */
    @ApiProperty(description = "历史库存总数量")
    private Integer skuHistoryTotalNum;

    /**
     * 历史库存总数量-开始
     */
    @ApiProperty(description = "历史库存总数量-开始")
    private Integer skuHistoryTotalNumStart;

    /**
     * 历史库存总数量-结束
     */
    @ApiProperty(description = "历史库存总数量-结束")
    private Integer skuHistoryTotalNumEnd;

    /**
     * 线上库存总数量
     */
    @ApiProperty(description = "线上库存总数量")
    private Integer skuLineNum;

    /**
     * 线上库存总数量-开始
     */
    @ApiProperty(description = "线上库存总数量-开始")
    private Integer skuLineNumStart;

    /**
     * 线上库存总数量-结束
     */
    @ApiProperty(description = "线上库存总数量-结束")
    private Integer skuLineNumEnd;

    /**
     * 实际库存总数量
     */
    @ApiProperty(description = "实际库存总数量")
    private Integer skuRealityNum;

    /**
     * 实际库存总数量-开始
     */
    @ApiProperty(description = "实际库存总数量-开始")
    private Integer skuRealityNumStart;

    /**
     * 实际库存总数量-结束
     */
    @ApiProperty(description = "实际库存总数量-结束")
    private Integer skuRealityNumEnd;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiProperty(description = "是否可用 0：否，1：是")
    private Boolean isAvailable;

    /**
     * 创建人
     */
    @ApiProperty(description = "创建人")
    private String createUserName;

    /**
     * 创建人Id
     */
    @ApiProperty(description = "创建人Id")
    private Long createUserId;

    /**
     * 创建人Id-开始
     */
    @ApiProperty(description = "创建人Id-开始")
    private Long createUserIdStart;

    /**
     * 创建人Id-结束
     */
    @ApiProperty(description = "创建人Id-结束")
    private Long createUserIdEnd;

    /**
     * 创建时间
     */
    @ApiProperty(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建时间-开始
     */
    @ApiProperty(description = "创建时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeStart;

    /**
     * 创建时间-结束
     */
    @ApiProperty(description = "创建时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeEnd;

    /**
     * 修改人
     */
    @ApiProperty(description = "修改人")
    private String updateUserName;

    /**
     * 修改人Id
     */
    @ApiProperty(description = "修改人Id")
    private Long updateUserId;

    /**
     * 修改人Id-开始
     */
    @ApiProperty(description = "修改人Id-开始")
    private Long updateUserIdStart;

    /**
     * 修改人Id-结束
     */
    @ApiProperty(description = "修改人Id-结束")
    private Long updateUserIdEnd;

    /**
     * 修改时间
     */
    @ApiProperty(description = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 修改时间-开始
     */
    @ApiProperty(description = "修改时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTimeStart;

    /**
     * 修改时间-结束
     */
    @ApiProperty(description = "修改时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTimeEnd;

}