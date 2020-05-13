package com.jingliang.mall.repository;

import com.jingliang.mall.entity.AddressUserHistory;
import com.jingliang.mall.repository.base.BaseRepository;

import java.util.List;

/**
 * 经纬度记录表Repository
 *
 * @author Mengde Liu
 * @version 1.0.0
 * @date 2020-04-28 13:30:05
 */
public interface AddressUserHistoryRepository extends BaseRepository<AddressUserHistory, Long> {

    /**
     * 根据buyerId查询经纬度记录
     *
     * @param userId
     * @param isAvailable
     * @return
     */
    List<AddressUserHistory> findAllByUserIdAndIsAvailableOrderByCreateTimeAsc(Long userId, Boolean isAvailable);
}