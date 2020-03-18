package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 转盘
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@ApiModel(value = "TurntableResp", description = "转盘")
@Data
public class TurntableResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 转盘图片
     */
    @ApiModelProperty(value = "转盘图片")
    private String img;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String remark;

    /**
     * 转盘单次抽取需要的金币
     */
    @ApiModelProperty(value = "转盘单次抽取需要的金币")
    private Integer gold;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 更新人Id
     */
    @ApiModelProperty(value = "更新人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateUserId;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private UserResp updateUser;

    /**
     * 是否可用 0：否，1：是
     */
    @ApiModelProperty(value = "是否可用 0：否，1：是")
    private Boolean isAvailable;

}