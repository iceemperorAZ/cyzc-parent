package com.jingliang.mall.repository;

import com.jingliang.mall.entity.Turntable;
import com.jingliang.mall.repository.base.BaseRepository;

import java.util.List;

/**
 * 转盘Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
public interface TurntableRepository extends BaseRepository<Turntable, Long> {

    /**
     * 查询全部
     *
     * @param isAvailable
     * @return
     */
    List<Turntable> findAllByIsAvailableOrderByGoldAsc(Boolean isAvailable);
}