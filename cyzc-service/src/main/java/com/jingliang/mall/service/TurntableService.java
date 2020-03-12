package com.jingliang.mall.service;

import com.jingliang.mall.entity.Turntable;

/**
 * 转盘Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 11:40:28
 */
public interface TurntableService {

    /**
     * 保存转盘项
     *
     * @param turntable
     * @return
     */
    Turntable save(Turntable turntable);
}