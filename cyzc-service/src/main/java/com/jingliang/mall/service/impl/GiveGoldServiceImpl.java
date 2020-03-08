package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.GiveGold;
import com.jingliang.mall.entity.GoldLog;
import com.jingliang.mall.repository.BuyerRepository;
import com.jingliang.mall.repository.GiveGoldRepository;
import com.jingliang.mall.repository.GoldLogRepository;
import com.jingliang.mall.service.GiveGoldService;
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
public class GiveGoldServiceImpl implements GiveGoldService {

    private final GiveGoldRepository giveGoldRepository;
    private final BuyerRepository buyerRepository;
    private final GoldLogRepository signInLogRepository;

    public GiveGoldServiceImpl(GiveGoldRepository giveGoldRepository, BuyerRepository buyerRepository, GoldLogRepository signInLogRepository) {
        this.giveGoldRepository = giveGoldRepository;
        this.buyerRepository = buyerRepository;
        this.signInLogRepository = signInLogRepository;
    }

    @Override
    public GiveGold give(Long userId, Long buyerId, Integer goldNum, String msg) {
        GiveGold giveGold = new GiveGold();
        giveGold.setBuyerId(buyerId);
        giveGold.setCreateId(userId);
        giveGold.setGold(goldNum);
        giveGold.setCreateTime(new Date());
        giveGold.setIsApproval(false);
        giveGold.setIsAvailable(true);
        giveGold.setMsg(msg);
        return giveGoldRepository.save(giveGold);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GiveGold approval(Long userId, Long id) {
        GiveGold giveGold = giveGoldRepository.getOne(id);
        Buyer buyer = buyerRepository.getOne(giveGold.getBuyerId());
        giveGold.setIsApproval(true);
        giveGold.setIsAvailable(true);
        giveGold.setApprovalId(userId);
        giveGold.setApprovalTime(new Date());
        buyer.setGold(buyer.getGold() + giveGold.getGold());
        buyerRepository.save(buyer);
        GoldLog goldLog = new GoldLog();
        goldLog.setBuyerId(giveGold.getBuyerId());
        goldLog.setGiveId(userId);
        goldLog.setMoney(0);
        goldLog.setIsAvailable(true);
        goldLog.setGold(giveGold.getGold());
        goldLog.setMsg("系统赠送" + giveGold.getGold() + "金币");
        goldLog.setType(300);
        signInLogRepository.save(goldLog);
        return giveGoldRepository.save(giveGold);
    }

    @Override
    public Page<GiveGold> pageAll(Long buyerId, PageRequest pageRequest) {
        return giveGoldRepository.findAllByBuyerId(buyerId, pageRequest);
    }
}