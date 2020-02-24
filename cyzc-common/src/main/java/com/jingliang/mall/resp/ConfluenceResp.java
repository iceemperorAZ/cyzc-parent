package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

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
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

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
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer num;

    /**
     * 提成
     */
    @ApiModelProperty(value = "提成")
    private Double royalty;

    /**
     * 净利润
     */
    @ApiModelProperty(value = "净利润")
    private Double profit;

    public Double getTotalPrice() {
        return Objects.isNull(totalPrice) ? null : totalPrice / 100;
    }

    public Double getRoyalty() {
        return Objects.isNull(royalty) ? null : royalty / 100;
    }

    public Double getProfit() {
        return Objects.isNull(profit) ? null : profit / 100;
    }
}
