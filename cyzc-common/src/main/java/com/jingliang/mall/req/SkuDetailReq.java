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
 * 库存详情
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 19:00:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(name = "SkuDetailReq", description = "库存详情")
public class SkuDetailReq extends BaseReq implements Serializable {

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
     * 库存Id
     */
    @ApiProperty(description = "库存Id")
    private Long skuId;

    /**
     * 库存Id-开始
     */
    @ApiProperty(description = "库存Id-开始")
    private Long skuIdStart;

    /**
     * 库存Id-结束
     */
    @ApiProperty(description = "库存Id-结束")
    private Long skuIdEnd;

    /**
     * 库存编号
     */
    @ApiProperty(description = "库存编号")
    private String skuNo;

    /**
     * 批号
     */
    @ApiProperty(description = "批号")
    private String batchNumber;

    /**
     * 追加库存数量
     */
    @ApiProperty(description = "追加库存数量")
    private Integer skuAppendNum;

    /**
     * 追加库存数量-开始
     */
    @ApiProperty(description = "追加库存数量-开始")
    private Integer skuAppendNumStart;

    /**
     * 追加库存数量-结束
     */
    @ApiProperty(description = "追加库存数量-结束")
    private Integer skuAppendNumEnd;

    /**
     * 剩余库存数量
     */
    @ApiProperty(description = "剩余库存数量")
    private Integer skuResidueNum;

    /**
     * 剩余库存数量-开始
     */
    @ApiProperty(description = "剩余库存数量-开始")
    private Integer skuResidueNumStart;

    /**
     * 剩余库存数量-结束
     */
    @ApiProperty(description = "剩余库存数量-结束")
    private Integer skuResidueNumEnd;

    /**
     * 进价
     */
    @ApiProperty(description = "进价")
    private Double purchasePrice;

    /**
     * 进价-开始
     */
    @ApiProperty(description = "进价-开始")
    private Double purchasePriceStart;

    /**
     * 进价-结束
     */
    @ApiProperty(description = "进价-结束")
    private Double purchasePriceEnd;

    /**
     * 生产时间
     */
    @ApiProperty(description = "生产时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date productionTime;

    /**
     * 生产时间-开始
     */
    @ApiProperty(description = "生产时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date productionTimeStart;

    /**
     * 生产时间-结束
     */
    @ApiProperty(description = "生产时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date productionTimeEnd;

    /**
     * 过期时间
     */
    @ApiProperty(description = "过期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expiredTime;

    /**
     * 过期时间-开始
     */
    @ApiProperty(description = "过期时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expiredTimeStart;

    /**
     * 过期时间-结束
     */
    @ApiProperty(description = "过期时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expiredTimeEnd;

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

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice * 100;
    }

    public void setPurchasePriceStart(Double purchasePriceStart) {
        this.purchasePriceStart = purchasePriceStart * 100;
    }

    public void setPurchasePriceEnd(Double purchasePriceEnd) {
        this.purchasePriceEnd = purchasePriceEnd * 100;
    }
}