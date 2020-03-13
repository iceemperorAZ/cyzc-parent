package com.jingliang.mall.repository;

import com.jingliang.mall.entity.TurntableDetail;
import com.jingliang.mall.repository.base.BaseRepository;

import java.util.List;

/**
 * 转盘详情Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
public interface TurntableDetailRepository extends BaseRepository<TurntableDetail, Long> {

    /**
     * 根据转盘Id查询
     *
     * @param turntableId
     * @param isAvailable
     * @return
     */
    List<TurntableDetail> findAllByTurntableIdAndIsAvailable(Long turntableId, Boolean isAvailable);

}