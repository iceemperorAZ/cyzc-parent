package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * 签到日志
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-03 10:42:56
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(value = "SignInResp", description = "签到日志")
public class SignInResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 商户Id
     */
    @ApiModelProperty(value = "商户Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long buyerId;

    /**
     * 连续签到天数
     */
    @ApiModelProperty(value = "连续签到天数")
    private Integer dayNum;

    /**
     * 最后一次签到日期
     */
    @ApiModelProperty(value = "最后一次签到日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastDate;

    public Boolean isToday() {
        return LocalDate.now().equals(lastDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    /**
     * 是否可用 0：否，1：是
     */
    @ApiModelProperty(value = "是否可用 0：否，1：是")
    private Boolean isAvailable;


}