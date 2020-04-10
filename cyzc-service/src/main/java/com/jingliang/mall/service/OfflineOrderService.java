package com.jingliang.mall.service;

import com.jingliang.mall.entity.OfflineOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

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

    /**
     * 根据条件查询全部
     *
     * @param specification
     * @return
     */
    List<OfflineOrder> downExcel(Specification<OfflineOrder> specification);

    /**
     * 解锁订单
     *
     * @param id
     * @return
     */
    Boolean unlock(Long id);

    /**
     * 修改发票开具进度为完成
     *
     * @param id
     * @return
     */
    Boolean success(Long id);

    /**
     * 不锁定查询
     * @param specification
     * @return
     */
    List<OfflineOrder> financeDown(Specification<OfflineOrder> specification);

    /**
     * 删除
     * @param userId
     * @param offlineOrderId
     * @return
     */
    OfflineOrder delete(Long userId, Long offlineOrderId);
}