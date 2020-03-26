package com.jingliang.mall.service;

import com.jingliang.mall.entity.OfflineOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * 线下订单Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-25 10:05:44
 */
public interface OfflineOrderService {

    /**
     * 保存/修改
     *
     * @param offlineOrder
     * @return
     */
    OfflineOrder save(OfflineOrder offlineOrder);

    /**
     * 分页查询全部
     *
     * @param specification
     * @param pageRequest
     * @return
     */
    Page<OfflineOrder> pageAll(Specification<OfflineOrder> specification, PageRequest pageRequest);
}