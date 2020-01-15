package com.jingliang.mall.repository;

import com.jingliang.mall.entity.ContactRecord;
import com.jingliang.mall.repository.base.BaseRepository;

import java.util.List;

/**
 * 商户联系记录Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-01-15 10:17:00
 */
public interface ContactRecordRepository extends BaseRepository<ContactRecord, Long> {

    /**
     * 通过商户id查询
     *
     * @param buyerId
     * @param isAvailable
     * @return
     */
    List<ContactRecord> findAllByBuyerIdAndIsAvailable(Long buyerId, boolean isAvailable);
}