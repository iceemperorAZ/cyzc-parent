package com.jingliang.mall.repository;

import com.jingliang.mall.entity.OfflineOrderReturn;
import com.jingliang.mall.repository.base.BaseRepository;

import java.util.List;

/**
 * 退货表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-20 17:57:20
 */
public interface OfflineOrderReturnRepository extends BaseRepository<OfflineOrderReturn, Long> {

    /**
     * 查询退货列表
     *
     * @param orderId
     * @param isAvailable
     * @return
     */
    List<OfflineOrderReturn> findAllByOrderIdAndIsAvailable(Long orderId, Boolean isAvailable);
}