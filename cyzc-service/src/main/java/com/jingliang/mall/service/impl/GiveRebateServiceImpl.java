package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.GiveRebate;
import com.jingliang.mall.repository.BuyerRepository;
import com.jingliang.mall.repository.GiveRebateRepository;
import com.jingliang.mall.service.GiveRebateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 赠赠送返利次数ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 14:57:03
 */
@Service
@Slf4j
public class GiveRebateServiceImpl implements GiveRebateService {

    private final GiveRebateRepository giveRebateRepository;
    private final BuyerRepository buyerRepository;

    public GiveRebateServiceImpl(GiveRebateRepository giveRebateRepository, BuyerRepository buyerRepository) {
        this.giveRebateRepository = giveRebateRepository;
        this.buyerRepository = buyerRepository;
    }

    @Override
    public GiveRebate give(Long userId, Long buyerId, Integer rebateNum, String msg) {
        GiveRebate giveRebate = new GiveRebate();
        giveRebate.setBuyerId(buyerId);
        giveRebate.setCreateId(userId);
        giveRebate.setRebate(rebateNum);
        giveRebate.setCreateTime(new Date());
        giveRebate.setApproval(100);
        giveRebate.setIsAvailable(true);
        giveRebate.setMsg(msg);
        return giveRebateRepository.save(giveRebate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GiveRebate approval(Long userId, Long id, Integer approval) {
        GiveRebate giveRebate = giveRebateRepository.getOne(id);
        if (approval == 300) {
            Buyer buyer = buyerRepository.getOne(giveRebate.getBuyerId());
            buyer.setOrderSpecificNum(buyer.getOrderSpecificNum() + giveRebate.getRebate());
        }
        giveRebate.setApproval(approval);
        giveRebate.setIsAvailable(true);
        giveRebate.setApprovalId(userId);
        giveRebate.setApprovalTime(new Date());
        return giveRebateRepository.save(giveRebate);
    }

    @Override
    public Page<GiveRebate> pageAll(Long buyerId, PageRequest pageRequest) {
        return giveRebateRepository.findAllByBuyerIdOrderByCreateTimeDesc(buyerId, pageRequest);
    }
}