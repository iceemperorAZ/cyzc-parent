package com.jingliang.mall.service;

import com.jingliang.mall.entity.ContactRecord;

import java.util.List;

/**
 * 商户联系记录Service
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-01-15 10:17:00
 */
public interface ContactRecordService {

    /**
     * 保存
     * @param contactRecord
     * @return
     */
    ContactRecord save(ContactRecord contactRecord);

    /**
     * 根据商户Id查询联系结果集合
     * @param buyerId
     * @return
     */
    List<ContactRecord> findByBuyerId(Long buyerId);
}