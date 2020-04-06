package com.jingliang.mall.repository;

import com.jingliang.mall.entity.ManagerSale;
import com.jingliang.mall.repository.base.BaseRepository;

import java.util.Date;
import java.util.List;

/**
 * 商户销售绑定表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-02-17 14:21:26
 */
public interface ManagerSaleRepository extends BaseRepository<ManagerSale, Long> {

    /**
     * 根据销售Id查询绑定关系（只有一条）
     *
     *
     * @param managerId
     * @param saleId
     * @param isAvailable
     * @return
     */
    List<ManagerSale> findAllByManagerIdAndSaleIdAndIsAvailableOrderByUntyingTime(Long managerId, Long saleId, Boolean isAvailable);

    /**
     * 根据销售Id查询绑定关系（只有一条）
     *
     *
     * @param managerId
     * @param saleId
     * @return
     */
    List<ManagerSale> findAllByManagerIdAndSaleId(Long managerId, Long saleId);

    /**
     * 查询指定时间段的销售
     *
     * @param managerId
     * @param startTime
     * @param endTime
     * @param isAvailable
     * @return
     */
    List<ManagerSale> findAllByManagerIdAndCreateTimeLessThanEqualOrUntyingTimeGreaterThanEqualAndIsAvailable(Long managerId, Date startTime, Date endTime, Boolean isAvailable);
}