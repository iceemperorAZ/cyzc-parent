package com.jingliang.mall.repository;

import com.jingliang.mall.entity.GiveRebate;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 赠送返利次数Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 14:57:03
 */
public interface GiveRebateRepository extends BaseRepository<GiveRebate, Long> {

    /**
     * 分页查询所有赠送金币
     *
     * @param buyerId
     * @param pageRequest
     * @return
     */
    Page<GiveRebate> findAllByBuyerIdOrderByApprovalTimeDesc(Long buyerId, Pageable pageRequest);
}