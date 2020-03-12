package com.jingliang.mall.repository;

import com.jingliang.mall.entity.GiveGold;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 赠送金币Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 14:57:03
 */
public interface GiveGoldRepository extends BaseRepository<GiveGold, Long> {

    /**
     * 分页查询所有赠送金币
     *
     * @param buyerId
     * @param pageRequest
     * @return
     */
    Page<GiveGold> findAllByBuyerIdOrderByApprovalTimeDesc(Long buyerId, Pageable pageRequest);
}