package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 商品表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-09 09:14:37
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(value = "ProductResp", description = "商品表")
public class ProductResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 商品分类Id
     */
    @ApiModelProperty(value = "商品分类Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productTypeId;

    /**
     * 商品排序
     */
    @ApiModelProperty(value = "商品排序")
    private Integer productSort;

    /**
     * 商品分类名称
     */
    @ApiModelProperty(value = "商品分类名称")
    private String productTypeName;

    /**
     * 商品区Id
     */
    @ApiModelProperty(value = "商品区Id")
    @JsonSerialize(using = ToStringSerializer.class)
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
    @ApiModelProperty(value = "商品图片uri集合")
    private List<String> productImgUriList;

    /**
     * 市场价
     */
    @ApiModelProperty(value = "市场价")
    private Double marketPrice;

    /**
     * 销价
     */
    @ApiModelProperty(value = "销价")
    private Double sellingPrice;

    /**
     * 会员折扣率
     */
    @ApiModelProperty(value = "会员折扣率")
    private Integer discount;

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
     * 搜索关键词
     */
    @ApiModelProperty(value = "搜索关键词")
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
     * 是否可用 0：否，1：是
     */
    @ApiModelProperty(value = "是否可用 0：否，1：是")
    private Boolean isAvailable;

    /**
     * 折扣价
     */
    @ApiModelProperty(value = "折扣价")
    private String discountShow;

    public String getDiscountShow() {
        if ("-1".equals(discountShow)) {
            return "";
        }
        return discountShow;
    }

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createUserName;

    /**
     * 创建人Id
     */
    @ApiModelProperty(value = "创建人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateUserName;

    /**
     * 修改人Id
     */
    @ApiModelProperty(value = "修改人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 上架人
     */
    @ApiModelProperty(value = "上架人")
    private String showUserName;

    /**
     * 上架人Id
     */
    @ApiModelProperty(value = "上架人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long showUserId;

    /**
     * 上架时间
     */
    @ApiModelProperty(value = "上架时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date showTime;

    /**
     * 是否即将上架
     */
    @ApiModelProperty(value = "是否即将上架")
    private Boolean isSoonShow;

    public Boolean getIsSoonShow() {
        return isSoonShow;
    }

    public void setIsSoonShow(Boolean isSoonShow) {
        this.isSoonShow = isSoonShow;
    }

    /**
     * 重量
     */
    @ApiModelProperty(value = "重量")
    private Integer weight;

    /**
     * 每日购买上限
     */
    @ApiModelProperty(value = "每日购买上限")
    private Integer limitNum;

    /**
     * 每日购买下限
     */
    @ApiModelProperty(value = "每日购买下限")
    private Integer minNum;

    /**
     * 商品详情
     */
    @ApiModelProperty(value = "商品详情")
    private String productDetails;

    /**
     * 商品详情图url
     */
    @ApiModelProperty(value = "商品详情图url")
    private List<String>  productDetailsImgUrlList;

    /**
     * 商品图片字符串转集合
     */
    public void setProductImgUris(String productImgUris) {
        this.productImgUriList = StringUtils.isBlank(productImgUris) ? null : Arrays.asList(productImgUris.split(";"));
    }
    /**
     * 商品详情图片字符串转集合
     */
    public void setProductDetailsImgUrls(String productDetailsImgUrls) {
        this.productDetailsImgUrlList = StringUtils.isBlank(productDetailsImgUrls) ? null : Arrays.asList(productDetailsImgUrls.split(";"));
    }
    public Double getMarketPrice() {
        return marketPrice / 100;
    }

    public Double getSellingPrice() {
        return sellingPrice / 100;
    }
}