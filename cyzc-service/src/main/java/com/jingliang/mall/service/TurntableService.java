package com.jingliang.mall.service;

import com.jingliang.mall.entity.Turntable;
import com.jingliang.mall.entity.TurntableDetail;

import java.util.List;

/**
 * 转盘Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
public interface TurntableService {

    /**
     * 保存
     *
     * @param turntable
     * @return
     */
    Turntable save(Turntable turntable);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    Turntable findById(Long id);

    /**
     * 查询全部
     *
     * @return
     */
    List<Turntable> findAll();

    /**
     * 删除
     *
     * @param id
     * @param userId
     */
    void delete(Long id, Long userId);

    /**
     * 抽奖
     *
     * @param id
     * @param buyerId
     * @return
     */
    TurntableDetail extract(Long id, Long buyerId);
}