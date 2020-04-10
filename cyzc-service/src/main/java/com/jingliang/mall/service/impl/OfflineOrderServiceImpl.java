package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.OfflineOrder;
import com.jingliang.mall.repository.OfflineOrderRepository;
import com.jingliang.mall.service.OfflineOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OfflineOrder> downExcel(Specification<OfflineOrder> specification) {
        List<OfflineOrder> offlineOrders = offlineOrderRepository.findAll(specification);
        //导出的同时锁定所有导出的订单
        offlineOrders.forEach(offlineOrder -> offlineOrder.setEnable(true));
        return offlineOrderRepository.saveAll(offlineOrders);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unlock(Long id) {
        OfflineOrder offlineOrder = offlineOrderRepository.findById(id).orElse(null);
        assert offlineOrder != null;
        offlineOrder.setEnable(false);
        offlineOrderRepository.save(offlineOrder);
        return true;
    }

    @Override
    public Boolean success(Long id) {
        OfflineOrder offlineOrder = offlineOrderRepository.findById(id).orElse(null);
        assert offlineOrder != null;
        offlineOrder.setRate(300);
        offlineOrderRepository.save(offlineOrder);
        return true;
    }

    @Override
    public List<OfflineOrder> financeDown(Specification<OfflineOrder> specification) {
        return offlineOrderRepository.findAll(specification);
    }

    @Override
    public OfflineOrder delete(Long userId, Long offlineOrderId) {
        OfflineOrder offlineOrder = offlineOrderRepository.findById(offlineOrderId).orElse(null);
        assert offlineOrder != null;
        if (userId.equals(offlineOrder.getSalesmanId())) {
            offlineOrder.setIsAvailable(false);
            return offlineOrderRepository.save(offlineOrder);
        }
        return null;
    }
}