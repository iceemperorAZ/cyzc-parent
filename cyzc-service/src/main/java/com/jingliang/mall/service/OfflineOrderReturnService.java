package com.jingliang.mall.service;

import com.jingliang.mall.entity.OfflineOrderReturn;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * 退货表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-20 17:57:20
 */
public interface OfflineOrderReturnService {

    /**
     * 退货
     *
     * @param offlineOrderReturn1
     * @param offlineOrderReturn2
     * @param orderStatus
     * @return
     */
    Boolean returnApproval(OfflineOrderReturn offlineOrderReturn1, OfflineOrderReturn offlineOrderReturn2, Integer orderStatus);

    /**
     * 退货审批
     *
     * @param offlineOrderReturn
     * @return
     */
    Boolean save(OfflineOrderReturn offlineOrderReturn);

    /**
     * 根据条件查询退货列表
     *
     * @param specification
     * @return
     */

    List<OfflineOrderReturn> findAll(Specification<OfflineOrderReturn> specification);
}