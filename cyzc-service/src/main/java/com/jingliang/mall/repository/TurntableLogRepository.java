package com.jingliang.mall.repository;

import com.jingliang.mall.entity.TurntableLog;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 转盘日志Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
public interface TurntableLogRepository extends BaseRepository<TurntableLog, Long> {

    /**
     * 分页查询日志
     *
     * @param buyerId
     * @param isAvailable
     * @param pageable
     * @return
     */
    Page<TurntableLog> findAllByBuyerIdAndIsAvailableOrderByCreateTimeDesc(Long buyerId, Boolean isAvailable, Pageable pageable);

}