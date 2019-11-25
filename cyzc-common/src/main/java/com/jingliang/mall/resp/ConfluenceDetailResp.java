package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 总汇详情
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-11-08 10:08
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "ConfluenceDetailResp", description = "总汇详情")
@Data
public class ConfluenceDetailResp implements Serializable {
    private static final long serialVersionUID = 6957783700034131569L;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 总价
     */
    @ApiModelProperty(value = "总价")
    private Double totalPrice;

    /**
     * 提成
     */
    @ApiModelProperty(value = "提成")
    private Double royalty;
}