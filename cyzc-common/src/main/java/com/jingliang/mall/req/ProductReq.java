package com.jingliang.mall.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-23 23:35:15
 */
@ApiModel(value = "ProductReq", description = "商品")
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键集合
     */
    @ApiModelProperty(value = "主键集合")
    private List<Long> productIds;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 主键-开始
     */
    @ApiModelProperty(value = "主键-开始")
    private Long idStart;

    /**
     * 主键-结束
     */
    @ApiModelProperty(value = "主键-结束")
    private Long idEnd;

    /**
     * 商品分类Id
     */
    @ApiModelProperty(value = "商品分类Id")
    private Long productTypeId;

    /**
     * 商品分类Id-开始
     */
    @ApiModelProperty(value = "商品分类Id-开始")
    private Long productTypeIdStart;

    /**
     * 商品分类Id-结束
     */
    @ApiModelProperty(value = "商品分类Id-结束")
    private Long productTypeIdEnd;

    /**
     * 商品分类名称
     */
    @ApiModelProperty(value = "商品分类名称")
    private String productTypeName;

    /**
     * 商品区Id
     */
    @ApiModelProperty(value = "product_zone_id")
    private Long productZoneId;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String productNo;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 商品图片uri集合
     */
    @ApiModelProperty(hidden = true)
    private String productImgUris;
    /**
     * 商品base64图片集合
     */
    @ApiModelProperty(value = "商品base64图片集合")
    private List<String> productImgs = new ArrayList<>();
    /**
     * 市场价
     */
    @ApiModelProperty(value = "市场价")
    private Double marketPrice;

    /**
     * 市场价-开始
     */
    @ApiModelProperty(value = "市场价-开始")
    private Double purchasePriceStart;

    /**
     * 市场价-结束
     */
    @ApiModelProperty(value = "市场价-结束")
    private Double purchasePriceEnd;

    /**
     * 销价
     */
    @ApiModelProperty(value = "销价")
    private Double sellingPrice;

    /**
     * 销价-开始
     */
    @ApiModelProperty(value = "销价-开始")
    private Double sellingPriceStart;

    /**
     * 销价-结束
     */
    @ApiModelProperty(value = "销价-结束")
    private Double sellingPriceEnd;

    /**
     * 会员折扣率
     */
    @ApiModelProperty(value = "会员折扣率")
    private Double discount;

    /**
     * 会员折扣率-开始
     */
    @ApiModelProperty(value = "会员折扣率-开始")
    private Double discountStart;

    /**
     * 会员折扣率-结束
     */
    @ApiModelProperty(value = "会员折扣率-结束")
    private Double discountEnd;

    /**
     * 是否新品 0：否，1：是
     */
    @ApiModelProperty(value = "是否新品 0：否，1：是")
    private Boolean isNew;

    /**
     * 是否热销 0：否，1：是
     */
    @ApiModelProperty(value = "是否热销 0：否，1：是")
    private Boolean isHot;

    /**
     * 是否已上架 0：否，1：是
     */
    @ApiModelProperty(value = "是否已上架 0：否，1：是")
    private Boolean isShow;

    /**
     * 推荐 0：否，1：是
     */
    @ApiModelProperty(value = "推荐 0：否，1：是 ")
    private Boolean isRecommend;

    /**
     * 搜索词[暂时舍弃]
     */
    @ApiModelProperty(value = "搜索词[暂时舍弃]")
    private String keyword;

    /**
     * 销量
     */
    @ApiModelProperty(value = "销量")
    private Integer salesVolume;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String specs;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 销量-开始
     */
    @ApiModelProperty(value = "销量-开始")
    private Integer salesVolumeStart;

    /**
     * 销量-结束
     */
    @ApiModelProperty(value = "销量-结束")
    private Integer salesVolumeEnd;

    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述")
    private String productDescribe;

    /**
     * 商品属性集合，属性和值之间使用“:”分割，多个之间使用”;”分割
     */
    @ApiModelProperty(value = "商品属性集合，属性和值之间使用“:”分割，多个之间使用”;”分割")
    private String attributes;

    /**
     * 审核状态 100:未提交审核，110：提交审核（审核中），120：审核通过，121：审核驳回
     */
    @ApiModelProperty(value = "审核状态 100:未提交审核，110：提交审核（审核中），120：审核通过，121：审核驳回")
    private Integer examineStatus;

    /**
     * 审核状态 100:未提交审核，110：提交审核（审核中），120：审核通过，121：审核驳回-开始
     */
    @ApiModelProperty(value = "审核状态 100:未提交审核，110：提交审核（审核中），120：审核通过，121：审核驳回-开始")
    private Integer examineStatusStart;

    /**
     * 审核状态 100:未提交审核，110：提交审核（审核中），120：审核通过，121：审核驳回-结束
     */
    @ApiModelProperty(value = "审核状态 100:未提交审核，110：提交审核（审核中），120：审核通过，121：审核驳回-结束")
    private Integer examineStatusEnd;

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
     * 上架人
     */
    @ApiModelProperty(value = "上架人")
    private String showUserName;

    /**
     * 上架人Id
     */
    @ApiModelProperty(value = "上架人Id")
    private Long showUserId;

    /**
     * 上架时间
     */
    @ApiModelProperty(value = "上架时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date showTime;

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice * 100;
    }

    public void setPurchasePriceStart(Double purchasePriceStart) {
        this.purchasePriceStart = purchasePriceStart * 100;
    }

    public void setPurchasePriceEnd(Double purchasePriceEnd) {
        this.purchasePriceEnd = purchasePriceEnd * 100;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice * 100;
    }

    public void setSellingPriceStart(Double sellingPriceStart) {
        this.sellingPriceStart = sellingPriceStart * 100;
    }

    public void setSellingPriceEnd(Double sellingPriceEnd) {
        this.sellingPriceEnd = sellingPriceEnd * 100;
    }
}