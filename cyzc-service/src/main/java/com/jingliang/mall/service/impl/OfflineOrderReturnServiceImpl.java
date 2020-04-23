package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.OfflineOrder;
import com.jingliang.mall.entity.OfflineOrderReturn;
import com.jingliang.mall.repository.OfflineOrderRepository;
import com.jingliang.mall.repository.OfflineOrderReturnRepository;
import com.jingliang.mall.service.OfflineOrderReturnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 退货表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-20 17:57:20
 */
@Service
@Slf4j
public class OfflineOrderReturnServiceImpl implements OfflineOrderReturnService {

    private final OfflineOrderRepository offlineOrderRepository;
    private final OfflineOrderReturnRepository offlineOrderReturnRepository;

    public OfflineOrderReturnServiceImpl(OfflineOrderRepository offlineOrderRepository, OfflineOrderReturnRepository offlineOrderReturnRepository) {
        this.offlineOrderRepository = offlineOrderRepository;
        this.offlineOrderReturnRepository = offlineOrderReturnRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean returnApproval(OfflineOrderReturn offlineOrderReturn1, OfflineOrderReturn offlineOrderReturn2, Integer orderStatus) {
        OfflineOrder offlineOrder = offlineOrderRepository.findById(offlineOrderReturn1.getOrderId()).orElse(null);
        if (offlineOrder == null) {
            return false;
        }
        //调整为对应的状态
        offlineOrder.setOrderStatus(orderStatus);
        offlineOrder.setProductName(offlineOrderReturn1.getProductName());
        offlineOrder.setProductSpecification(offlineOrderReturn1.getProductSpecification());
        offlineOrder.setCompany(offlineOrderReturn1.getCompany());
        offlineOrder.setNum(offlineOrderReturn1.getNum());
        offlineOrder.setUnitPrice(offlineOrderReturn1.getUnitPrice());
        offlineOrder.setTotalPrice(offlineOrderReturn1.getTotalPrice());
        offlineOrderRepository.save(offlineOrder);
        offlineOrderReturn2.setOrderStatus(100);
        offlineOrderReturnRepository.save(offlineOrderReturn2);
        return true;
    }

    @Override
    public Boolean save(OfflineOrderReturn offlineOrderReturn) {
        offlineOrderReturnRepository.save(offlineOrderReturn);
        return true;
    }

    @Override
    public List<OfflineOrderReturn> findAll(Specification<OfflineOrderReturn> specification) {
        return offlineOrderReturnRepository.findAll(specification);
    }
}