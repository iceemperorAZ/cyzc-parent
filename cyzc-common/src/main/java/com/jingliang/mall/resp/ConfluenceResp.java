package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 总汇
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-11-08 10:05
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "ConfluenceResp", description = "总汇")
@Data
public class ConfluenceResp implements Serializable {
    private static final long serialVersionUID = 6684088390034885179L;
    /**
     * 总成交单
     */
    @ApiModelProperty(value = "总成交单")
    private Integer transaction;

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
