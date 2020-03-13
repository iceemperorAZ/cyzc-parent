package com.jingliang.mall.repository;

import com.jingliang.mall.repository.base.BaseRepository;
import com.jingliang.mall.entity.Turntable;

/**
 * 转盘Repository
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
public interface TurntableRepository extends BaseRepository<Turntable, Long> {

    Turntable findFirstByGoldAndIsAvailable(Integer gold, boolean isAvailable);
}