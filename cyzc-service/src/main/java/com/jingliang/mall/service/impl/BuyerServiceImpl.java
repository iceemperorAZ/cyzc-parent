package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.GoldLog;
import com.jingliang.mall.repository.BuyerRepository;
import com.jingliang.mall.repository.GoldLogRepository;
import com.jingliang.mall.service.BuyerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 09:12:45
 */
@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    private final BuyerRepository buyerRepository;
    private final GoldLogRepository goldLogRepository;

    public BuyerServiceImpl(BuyerRepository buyerRepository, GoldLogRepository goldLogRepository) {
        this.buyerRepository = buyerRepository;
        this.goldLogRepository = goldLogRepository;
    }

    @Override
    public Buyer findByUniqueId(String uniqueId) {
        return buyerRepository.findFirstByUniqueIdAndIsAvailable(uniqueId, true);
    }

    @Override
    public Buyer save(Buyer buyer) {
        if (buyer.getId() == null) {
            GoldLog goldLog = new GoldLog();
            goldLog.setBuyerId(buyer.getId());
            goldLog.setIsAvailable(true);
            goldLog.setGold(10);
            goldLog.setMsg("新用户注册，赠送10金币。");
            goldLog.setCreateTime(new Date());
            goldLog.setType(300);
            goldLogRepository.save(goldLog);
            buyer.setGold(10);
        }
        return buyerRepository.save(buyer);
    }

    @Override
    public Page<Buyer> findAll(Specification<Buyer> buyerSpecification, PageRequest pageRequest) {
        return buyerRepository.findAll(buyerSpecification, pageRequest);
    }

    @Override
    public Buyer findById(Long id) {
        return buyerRepository.findAllByIdAndIsAvailable(id, true);
    }

    @Override
    public List<Buyer> findAllBySaleUserId(Long saleUserId) {
        return buyerRepository.findAllBySaleUserIdAndIsAvailable(saleUserId, true);
    }

    @Override
    public Page<Buyer> findAllBySaleUserId(Long saleUserId, PageRequest pageRequest) {
        return buyerRepository.findAllBySaleUserIdAndIsAvailable(saleUserId, true, pageRequest);
    }

    @Override
    public Page<Buyer> findAllBySaleId(Long saleUserId, PageRequest pageRequest) {
        return buyerRepository.findAllBySaleIdAndIsAvailable(saleUserId, true, pageRequest);
    }

    @Override
    public List<Buyer> findAll(Specification<Buyer> buyerSpecification) {
        return buyerRepository.findAll(buyerSpecification);
    }

}