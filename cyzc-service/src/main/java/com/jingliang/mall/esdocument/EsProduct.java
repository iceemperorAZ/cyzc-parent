package com.jingliang.mall.esdocument;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;

/**
 * elasticsearch中的商品实体
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-27 17:00
 */
@Document(indexName = "tob_products", type = "tob_products")
@Setting(settingPath = "/json/product-setting.json")
@Mapping(mappingPath = "/json/product-mapping.json")
@Data
public class EsProduct implements Serializable {
    private static final long serialVersionUID = 5816342944096863587L;
    /**
     * 主键
     */
    @Id
    @Field(type = FieldType.Long)
    private Long id;

    /**
     * 商品分类Id
     */
    @Field(type = FieldType.Long)
    private Long productTypeId;

    /**
     * 商品分类名称
     */
    @Field(type = FieldType.Text)
    private String productTypeName;

    /**
     * 商品编号
     */
    @Field(type = FieldType.Text)
    private String productNo;

    /**
     * 商品名称
     */
    @Field(type = FieldType.Text)
    private String productName;

    /**
     * 商品图片uri集合
     */
    @Field(type = FieldType.Text)
    private String productImgUris;

    /**
     * 市场价
     */
    @Field(type = FieldType.Double)
    private Long marketPrice;

    /**
     * 销价
     */
    @Field(type = FieldType.Double)
    private Long sellingPrice;

    /**
     * 是否新品 0：否，1：是
     */
    @Field(type = FieldType.Boolean)
    private Boolean isNew;

    /**
     * 是否热销 0：否，1：是
     */
    @Field(type = FieldType.Boolean)
    private Boolean isHot;

    /**
     * 推荐 0：否，1：是
     */
    @Field(type = FieldType.Boolean)
    private Boolean isRecommend;

    /**
     * 商品描述
     */
    @Field(type = FieldType.Text)
    private String productDescribe;

    /**
     * 商品属性集合，属性和值之间使用“:”分割，多个之间使用”;”分割
     */
    @Field(type = FieldType.Text)
    private String attributes;

}
