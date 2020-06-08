package com.jingliang.mall.repository;

import com.jingliang.mall.entity.GoldLog;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 签到日志Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-03 13:04:43
 */
public interface GoldLogRepository extends BaseRepository<GoldLog, Long> {

    /**
     * 分页查询签到记录
     *
     * @param buyerId
     * @param pageable
     * @return
     */
    Page<GoldLog> findAllByBuyerIdOrderByCreateTimeDesc(Long buyerId, Pageable pageable);

    /**
     * 根据支付
     * @param payNo
     * @return
     */
    GoldLog findFirstByPayNo(String payNo);

    /**
     * 根据用户返币记录查询订单有无返币记录
     *
     * @param payNo
     * @param isAvailable
     * @return
     */
    List<GoldLog> findAllByPayNoAndIsAvailable(String payNo,Boolean isAvailable);
}