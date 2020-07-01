package com.jingliang.mall.req;

import com.citrsw.annatation.ApiProperty;
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
    @ApiProperty(description = "商户id")
    private Long buyerId;
    /**
     * 地址
     */
    @ApiProperty(description = "地址")
    private String address;
    /**
     * 经度
     */
    @ApiProperty(description = "经度")
    private Double longitude;
    /**
     * 纬度
     */
    @ApiProperty(description = "纬度")
    private Double latitude;
    /**
     * 时间
     */
    @ApiProperty(description = "时间")
    private Date createTime;
}
