package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 总汇详情
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-11-08 10:08
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(name = "ConfluenceDetailResp", description = "总汇详情")
@Data
public class ConfluenceDetailResp implements Serializable {
    private static final long serialVersionUID = 6957783700034131569L;

    /**
     * 主键Id
     */
    @ApiProperty(description = "主键Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 名称
     */
    @ApiProperty(description = "名称")
    private String name;

    /**
     * 总价
     */
    @ApiProperty(description = "总价")
    private Double totalPrice;

    /**
     * 提成
     */
    @ApiProperty(description = "提成")
    private Double royalty;

    /**
     * 数量
     */
    @ApiProperty(description = "数量")
    private Integer num;

    /**
     * 净利润
     */
    @ApiProperty(description = "净利润")
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