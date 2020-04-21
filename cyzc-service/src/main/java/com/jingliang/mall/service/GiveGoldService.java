package com.jingliang.mall.service;

import com.jingliang.mall.entity.GiveGold;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 赠送金币Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 14:57:03
 */
public interface GiveGoldService {

    /**
     * 赠送金币
     *
     * @param user
     * @param buyerId
     * @param goldNum
     * @param msg
     * @return
     */
    GiveGold give(Long user, Long buyerId, Integer goldNum, String msg);

    /**
     * 审批
     *
     * @param userId
     * @param id
     * @return
     */
    GiveGold approval(Long userId, Long id,Integer approval);

    /**
     * 分页查询赠送记录
     *
     * @param buyerId
     * @param pageRequest
     * @return
     */
    Page<GiveGold> pageAll(Long buyerId, PageRequest pageRequest);
}