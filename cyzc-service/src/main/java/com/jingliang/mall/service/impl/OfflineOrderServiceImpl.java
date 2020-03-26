package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.OfflineOrder;
import com.jingliang.mall.repository.OfflineOrderRepository;
import com.jingliang.mall.service.OfflineOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * 线下订单ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-25 10:05:44
 */
@Service
@Slf4j
public class OfflineOrderServiceImpl implements OfflineOrderService {

    private final OfflineOrderRepository offlineOrderRepository;

    public OfflineOrderServiceImpl(OfflineOrderRepository offlineOrderRepository) {
        this.offlineOrderRepository = offlineOrderRepository;
    }

    @Override
    public OfflineOrder save(OfflineOrder offlineOrder) {
        return offlineOrderRepository.save(offlineOrder);
    }

    @Override
    public Page<OfflineOrder> pageAll(Specification<OfflineOrder> specification, PageRequest pageRequest) {
        return offlineOrderRepository.findAll(specification, pageRequest);
    }
}