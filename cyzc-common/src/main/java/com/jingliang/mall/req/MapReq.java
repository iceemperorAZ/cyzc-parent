package com.jingliang.mall.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 地图请求参数
 *
 * @author lmd
 * @date 2020/5/10
 * @company 晶粮
 */
@Data
public class MapReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 商户id
     */
    @ApiModelProperty(value = "商户id")
    private Long buyerId;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private Double longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private Double latitude;
    /**
     * 时间
     */
    @ApiModelProperty(value = "时间")
    private Date createTime;
}
