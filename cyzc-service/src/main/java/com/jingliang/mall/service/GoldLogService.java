package com.jingliang.mall.service;

import com.jingliang.mall.entity.GoldLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * 签到日志Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-03 13:04:43
 */
public interface GoldLogService {

    /**
     * 分页查询签到记录
     *
     * @param buyerId
     * @param pageRequest
     * @return
     */
    Page<GoldLog> findByBuyerId(Long buyerId, PageRequest pageRequest);

    /**
     * 分页查询金币获取记录
     *
     * @param specification
     * @param pageRequest
     * @return
     */
    Page<GoldLog> findAll(Specification<GoldLog> specification, PageRequest pageRequest);

    /**
     * 保存/更新获得金币记录
     *
     * @param goldLog
     */
    void save(GoldLog goldLog);

    /***
     * 查询日志中的支付单号
     * @param orderNo
     * @return
     */
    GoldLog findByPayNo(String orderNo);
}