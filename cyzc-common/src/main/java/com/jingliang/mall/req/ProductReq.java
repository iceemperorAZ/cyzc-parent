package com.jingliang.mall.req;

import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
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
@ApiModel(name = "ProductReq", description = "商品")
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键集合
     */
    @ApiProperty(description = "主键集合")
    private List<Long> productIds;

    /**
     * 主键
     */
    @ApiProperty(description = "主键")
    private Long id;

    /**
     * 商品分类Id
     */
    @ApiProperty(description = "商品分类Id")
    private Long productTypeId;

    /**
     * 商品分类名称
     */
    @ApiProperty(description = "商品分类名称")
    private String productTypeName;

    /**
     * 商品排序
     */
    @ApiProperty(description = "商品排序")
    private Integer productSort;

    /**
     * 商品区Id
     */
    @ApiProperty(description = "商品区Id")
    private Long productZoneId;

    /**
     * 商品编号
     */
    @ApiProperty(description = "商品编号")
    private String productNo;

    /**
     * 商品名称
     */
    @ApiProperty(description = "商品名称")
    private String productName;

    /**
     * 商品图片uri集合
     */
    @ApiProperty(hidden = true)
    private List<String> productImgUris;
    /**
     * 商品base64图片集合
     */
    @ApiProperty(description = "商品base64图片集合")
    private List<String> productImgs = new ArrayList<>();
    /**
     * 市场价
     */
    @ApiProperty(description = "市场价")
    private Float marketPrice;

    /**
     * 市场价-开始
     */
    @ApiProperty(description = "市场价-开始")
    private Float purchasePriceStart;

    /**
     * 市场价-结束
     */
    @ApiProperty(description = "市场价-结束")
    private Float purchasePriceEnd;

    /**
     * 销价
     */
    @ApiProperty(description = "销价")
    private Float sellingPrice;

    /**
     * 销价-开始
     */
    @ApiProperty(description = "销价-开始")
    private Float sellingPriceStart;

    /**
     * 销价-结束
     */
    @ApiProperty(description = "销价-结束")
    private Float sellingPriceEnd;

    /**
     * 会员折扣率
     */
    @ApiProperty(description = "会员折扣率")
    private Float discount;

    /**
     * 会员折扣率-开始
     */
    @ApiProperty(description = "会员折扣率-开始")
    private Float discountStart;

    /**
     * 会员折扣率-结束
     */
    @ApiProperty(description = "会员折扣率-结束")
    private Float discountEnd;

    /**
     * 是否新品 0：否，1：是
     */
    @ApiProperty(description = "是否新品 0：否，1：是")
    private Boolean isNew;

    /**
     * 是否热销 0：否，1：是
     */
    @ApiProperty(description = "是否热销 0：否，1：是")
    private Boolean isHot;

    /**
     * 是否已上架 0：否，1：是
     */
    @ApiProperty(description = "是否已上架 0：否，1：是")
    private Boolean isShow;

    /**
     * 推荐 0：否，1：是
     */
    @ApiProperty(description = "推荐 0：否，1：是 ")
    private Boolean isRecommend;

    /**
     * 搜索词[暂时舍弃]
     */
    @ApiProperty(description = "搜索词[暂时舍弃]")
    private String keyword;

    /**
     * 销量
     */
    @ApiProperty(description = "销量")
    private Integer salesVolume;

    /**
     * 规格
     */
    @ApiProperty(description = "规格")
    private String specs;

    /**
     * 单位
     */
    @ApiProperty(description = "单位")
    private String unit;

    /**
     * 销量-开始
     */
    @ApiProperty(description = "销量-开始")
    private Integer salesVolumeStart;

    /**
     * 销量-结束
     */
    @ApiProperty(description = "销量-结束")
    private Integer salesVolumeEnd;

    /**
     * 商品描述
     */
    @ApiProperty(description = "商品描述")
    private String productDescribe;

    /**
     * 商品属性集合，属性和值之间使用“:”分割，多个之间使用”;”分割
     */
    @ApiProperty(description = "商品属性集合，属性和值之间使用“:”分割，多个之间使用”;”分割")
    private String attributes;

    /**
     * 审核状态 100:未提交审核，110：提交审核（审核中），120：审核通过，121：审核驳回
     */
    @ApiProperty(description = "审核状态 100:未提交审核，110：提交审核（审核中），120：审核通过，121：审核驳回")
    private Integer examineStatus;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiProperty(description = "是否可用 0：否，1：是")
    private Boolean isAvailable;

    /**
     * 折扣价
     */
    @ApiProperty(description = "折扣价")
    private String discountShow;

    public String getDiscountShow() {
        if (StringUtils.isBlank(discountShow)) {
            return "-1";
        }
        return discountShow;
    }

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
     * 上架人
     */
    @ApiProperty(description = "上架人")
    private String showUserName;

    /**
     * 上架人Id
     */
    @ApiProperty(description = "上架人Id")
    private Long showUserId;

    /**
     * 上架时间
     */
    @ApiProperty(description = "上架时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date showTime;

    /**
     * 是否即将上架
     */
    @ApiProperty(description = "是否即将上架")
    private Boolean isSoonShow;

    /**
     * 每日购买上限
     */
    @ApiProperty(description = "每日购买上限")
    private Integer limitNum;

    /**
     * 每日购买下限
     */
    @ApiProperty(description = "每日购买下限")
    private Integer minNum;

    public Boolean getIsSoonShow() {
        return isSoonShow;
    }

    public void setIsSoonShow(Boolean isSoonShow) {
        this.isSoonShow = isSoonShow;
    }

    /**
     * 重量
     */
    @ApiProperty(description = "重量")
    private Integer weight;

    /**
     * 商品详情
     */
    @ApiProperty(description = "商品详情")
    private String productDetails;

    /**
     * 商品详情图base64
     */
    @ApiProperty(description = "商品详情图base64")
    private List<String> productDetailsImgBase64s = new ArrayList<>();

    /**
     * 商品详情图url
     */
    @ApiProperty(description = "商品详情图url")
    private List<String> productDetailsImgUrls = new ArrayList<>();

    /**
     * 商品不展示的区编号
     */
    @ApiProperty(description = "商品不展示的区编号")
    private String productArea;

    public void setMarketPrice(Float marketPrice) {
        this.marketPrice = marketPrice * 100;
    }

    public void setPurchasePriceStart(Float purchasePriceStart) {
        this.purchasePriceStart = purchasePriceStart * 100;
    }

    public void setPurchasePriceEnd(Float purchasePriceEnd) {
        this.purchasePriceEnd = purchasePriceEnd * 100;
    }

    public void setSellingPrice(Float sellingPrice) {
        this.sellingPrice = sellingPrice * 100;
    }

    public void setSellingPriceStart(Float sellingPriceStart) {
        this.sellingPriceStart = sellingPriceStart * 100;
    }

    public void setSellingPriceEnd(Float sellingPriceEnd) {
        this.sellingPriceEnd = sellingPriceEnd * 100;
    }
}