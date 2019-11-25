package com.jingliang.mall.common;

/**
 * 审核状态定义
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-24 9:54
 */
public enum ExamineStatus {
    /**
     * 未提交
     */
    NOT_SUBMITTED(100),
    /**
     * 已提交
     */
    SUBMITTED(110),
    /**
     * 通过
     */
    PASS(120),
    /**
     * 驳回
     */
    DISMISS(121);
    /**
     * 值
     */
    private Integer value;

    private ExamineStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
