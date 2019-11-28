package com.jingliang.mall.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 总汇
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-11-08 10:05
 */
@Data
public class Confluence implements Serializable {
    private static final long serialVersionUID = 6684088390034885179L;
    /**
     * 主键Id
     */
    private Long id;
    /**
     * 总成交单
     */
    private Integer transaction;

    /**
     * 总价
     */
    private Long totalPrice;

    /**
     * 提成
     */
    private Long royalty;

}
