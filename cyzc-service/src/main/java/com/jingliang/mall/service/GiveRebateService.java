package com.jingliang.mall.service;

import com.jingliang.mall.entity.GiveRebate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 赠送返利次数Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 14:57:03
 */
public interface GiveRebateService {

    /**
     * 赠送金币
     *
     * @param user
     * @param buyerId
     * @param rebateNum
     * @param msg
     * @return
     */
    GiveRebate give(Long user, Long buyerId, Integer rebateNum, String msg);

    /**
     * 审批
     *
     * @param userId
     * @param id
     * @param approval
     * @return
     */
    GiveRebate approval(Long userId, Long id, Integer approval);

    /**
     * 分页查询赠送记录
     *
     * @param buyerId
     * @param pageRequest
     * @return
     */
    Page<GiveRebate> pageAll(Long buyerId, PageRequest pageRequest);
}