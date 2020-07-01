package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户优惠券
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-18 16:50:49
 */
@ApiModel(name = "BuyerCouponResp", description = "用户优惠券")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BuyerCouponResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiProperty(description = "主键Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 优惠券Id
     */
    @ApiProperty(description = "优惠券Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long couponId;

    /**
     * 使用范围（商品分类）
     */
    @ApiProperty(description = "使用范围（商品分类）")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productTypeId;

    /**
     * 用户Id
     */
    @ApiProperty(description = "用户Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long buyerId;

    /**
     * 优惠百分比
     */
    @ApiProperty(description = "优惠百分比")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long percentage;

    /**
     * 开始时间
     */
    @ApiProperty(description = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 到期时间
     */
    @ApiProperty(description = "到期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTime;

    /**
     * 优惠券描述
     */
    @ApiProperty(description = "优惠券描述")
    private String couponDescribe;

    /**
     * 剩余可用张数
     */
    @ApiProperty(description = "剩余可用张数")
    private Integer receiveNum;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiProperty(description = "是否可用 0：否，1：是")
    private Boolean isAvailable;

    /**
     * 领取时间/赠送时间(创建时间)
     */
    @ApiProperty(description = "领取时间/赠送时间(创建时间)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人Id(系统：-1)
     */
    @ApiProperty(description = "创建人Id(系统：-1)")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    /**
     * 创建人(系统：系统)
     */
    @ApiProperty(description = "创建人(系统：系统)")
    private String createUser;

    /**
     * 状态  -1:未开始，100：未使用，200：已使用，300：已失效
     */
    @ApiProperty(description = " -1:未开始，100：未使用，200：已使用，300：已失效")
    private Integer status;
    public Integer getStatus() {
        //在生效期内
        if (receiveNum <= 0) {
            //已经使用过了
            return 200;
        }
        Date date = new Date();
        int start = startTime.compareTo(date);
        int end = expirationTime.compareTo(date);
        if (start > 0) {
            return -1;
        }
        if (end > 0) {
            return 100;
        }
        return 300;
    }

    /**
     * 状态 100：可使用，200：已使用，300：已失效
     */
    @ApiProperty(description = " -1:未开始，100：可使用，200：已使用，300：已失效")
    private String statusView;
    public String getStatusView() {
        if (receiveNum <= 0) {
            //已经使用过了
            return "已使用";
        }
        Date date = new Date();
        int start = startTime.compareTo(date);
        int end = expirationTime.compareTo(date);
        if (start > 0) {
            return "未开始";
        }
        if (end > 0) {
            //在生效期内
            return "去使用";
        }
        return "已失效";
    }

    /**
     * 商品分类
     */
    @ApiProperty(description = "商品分类")
    private ProductTypeResp productType;
}