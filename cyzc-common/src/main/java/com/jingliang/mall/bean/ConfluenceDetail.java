package com.jingliang.mall.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 总汇详情
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-11-08 10:08
 */
@Data
public class ConfluenceDetail implements Serializable {
    private static final long serialVersionUID = 6957783700034131569L;
    /**
     * 主键Id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 总价
     */
    private Long totalPrice;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 提成
     */
    private Long royalty;

    /**
     * 净利润
     */
    private Long profit;
}