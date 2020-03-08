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
 * 赠送金币ServiceImpl
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
        giveRebate.setIsApproval(false);
        giveRebate.setIsAvailable(true);
        giveRebate.setMsg(msg);
        return giveRebateRepository.save(giveRebate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GiveRebate approval(Long userId, Long id) {
        GiveRebate giveRebate = giveRebateRepository.getOne(id);
        Buyer buyer = buyerRepository.getOne(giveRebate.getBuyerId());
        giveRebate.setIsApproval(true);
        giveRebate.setIsAvailable(true);
        giveRebate.setApprovalId(userId);
        giveRebate.setApprovalTime(new Date());
//        buyer.setRebate(buyer.getRebate() + giveRebate.getRebate());
//        buyerRepository.save(buyer);
//        RebateLog goldLog = new RebateLog();
//        goldLog.setBuyerId(giveRebate.getBuyerId());
//        goldLog.setGiveId(userId);
//        goldLog.setMoney(0);
//        goldLog.setIsAvailable(true);
//        goldLog.setRebate(giveRebate.getRebate());
//        goldLog.setMsg("系统赠送" + giveRebate.getRebate() + "金币");
//        goldLog.setType(300);
//        signInLogRepository.save(goldLog);
        return giveRebateRepository.save(giveRebate);
    }

    @Override
    public Page<GiveRebate> pageAll(Long buyerId, PageRequest pageRequest) {
        return giveRebateRepository.findAllByBuyerId(buyerId, pageRequest);
    }
}