package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.GoldLog;
import com.jingliang.mall.repository.BuyerRepository;
import com.jingliang.mall.repository.GoldLogRepository;
import com.jingliang.mall.service.GoldLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 签到日志ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-03 13:04:43
 */
@Service
@Slf4j
public class GoldLogServiceImpl implements GoldLogService {

    private final GoldLogRepository signInLogRepository;
    private final BuyerRepository buyerRepository;

    public GoldLogServiceImpl(GoldLogRepository signInLogRepository, BuyerRepository buyerRepository) {
        this.signInLogRepository = signInLogRepository;
        this.buyerRepository = buyerRepository;
    }

    @Override
    public Page<GoldLog> findByBuyerId(Long buyerId, PageRequest pageRequest) {
        return signInLogRepository.findAllByBuyerIdOrderByCreateTimeDesc(buyerId, pageRequest);
    }

    @Override
    public Page<GoldLog> findAll(Specification<GoldLog> specification, PageRequest pageRequest) {
        return signInLogRepository.findAll(specification, pageRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(GoldLog goldLog) {
        if (goldLog.getIsAvailable()) {
            Buyer buyer = buyerRepository.getOne(goldLog.getBuyerId());
            buyer.setGold(buyer.getGold() + goldLog.getGold());
            buyerRepository.save(buyer);
        }
        signInLogRepository.save(goldLog);
    }

    @Override
    public GoldLog findByPayNo(String orderNo) {
        return signInLogRepository.findFirstByPayNo(orderNo);
    }
}