package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 转盘详情
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@ApiModel(value = "TurntableDetailResp", description = "转盘详情")
@Data
public class TurntableDetailResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 转盘Id
     */
    @ApiModelProperty(value = "转盘Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long turntableId;

    /**
     * 奖品Id
     */
    @ApiModelProperty(value = "奖品Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prizeId;

    /**
     * 商品奖品
     */
    @ApiModelProperty(value = "商品奖品")
    private ProductResp product;

    /**
     * 奖品最多被抽到的次数
     */
    @ApiModelProperty(value = "奖品最多被抽到的次数")
    private Integer prizeNum;

    /**
     * 奖品基数
     */
    @ApiModelProperty(value = "奖品基数")
    private Integer baseNum;

    /**
     * 奖品名称
     */
    @ApiModelProperty(value = "奖品名称")
    private String prizeName;

    /**
     * 类型(100:谢谢惠顾,200:金币，300:返币次数，400：商品[为商品时需配置奖品Id],)
     */
    @ApiModelProperty(value = "类型(100:谢谢惠顾,200:金币，300:返币次数，400：商品[为商品时需配置奖品Id],)")
    private Integer type;

    /**
     * 概率
     */
    @ApiModelProperty(value = "概率")
    private Integer probability;

    /**
     * 开始角度
     */
    @ApiModelProperty(value = "开始角度")
    private Integer startAngle;

    /**
     * 结束角度
     */
    @ApiModelProperty(value = "结束角度")
    private Integer endAngle;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人Id
     */
    @ApiModelProperty(value = "创建人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private UserResp createUser;

    /**
     * 是否上架
     */
    @ApiModelProperty(value = "是否上架")
    private Boolean isShow;

    /**
     * 上架人Id
     */
    @ApiModelProperty(value = "上架人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long showUserId;

    /**
     * 上架人
     */
    @ApiModelProperty(value = "上架人")
    private UserResp showUser;

    /**
     * 上架时间
     */
    @ApiModelProperty(value = "上架时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date showTime;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String img;
    ;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiModelProperty(value = "是否可用 0：否，1：是")
    private Boolean isAvailable;

}