package com.jingliang.mall.service.impl;

import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.Turntable;
import com.jingliang.mall.entity.TurntableDetail;
import com.jingliang.mall.exception.TurntableException;
import com.jingliang.mall.repository.BuyerRepository;
import com.jingliang.mall.repository.TurntableDetailRepository;
import com.jingliang.mall.repository.TurntableLogRepository;
import com.jingliang.mall.repository.TurntableRepository;
import com.jingliang.mall.service.TurntableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 转盘ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@Service
@Slf4j
public class TurntableServiceImpl implements TurntableService {

    private final TurntableRepository turntableRepository;
    private final BuyerRepository buyerRepository;
    private final TurntableDetailRepository turntableDetailRepository;
    private final TurntableLogRepository turntableLogRepository;

    public TurntableServiceImpl(TurntableRepository turntableRepository, BuyerRepository buyerRepository, TurntableDetailRepository turntableDetailRepository, TurntableLogRepository turntableLogRepository) {
        this.turntableRepository = turntableRepository;
        this.buyerRepository = buyerRepository;
        this.turntableDetailRepository = turntableDetailRepository;
        this.turntableLogRepository = turntableLogRepository;
    }

    @Override
    public Turntable save(Turntable turntable) {
        return turntableRepository.save(turntable);
    }

    @Override
    public Turntable findById(Long id) {
        return turntableRepository.findAllByIdAndIsAvailable(id, true);
    }

    @Override
    public List<Turntable> findAll() {
        return turntableRepository.findAllByIsAvailable(true);
    }

    @Override
    public void delete(Long id, Long userId) {
        Turntable turntable = turntableRepository.getOne(id);
        turntable.setIsAvailable(false);
        turntable.setUpdateTime(new Date());
        turntable.setUpdateUserId(userId);
        turntableRepository.save(turntable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TurntableDetail extract(Long id, Long buyerId) {
        Turntable turntable = turntableRepository.getOne(id);
        Buyer buyer = buyerRepository.findAllByIdAndIsAvailable(buyerId, true);
        Integer gold = buyer.getGold();
        //获取转盘所需要的金币数
        if (gold < turntable.getGold()) {
            //金币不够不能抽奖
            throw new TurntableException("金币数量不足！");
        }
        List<TurntableDetail> turntableDetails = turntableDetailRepository.findAllByTurntableIdAndIsAvailable(id, true);
        TurntableDetail turntableDetail1 = MallUtils.weightRandom(turntableDetails.stream().parallel().collect(Collectors.toMap(TurntableDetail::getId, turntableDetail -> turntableDetail)));
        //把抽到的商品数量减1
        turntableDetail1.setPrizeNum(turntableDetail1.getPrizeNum() - 1);
        turntableDetailRepository.save(turntableDetail1);
        //减少用户金币
        buyer.setGold(buyer.getGold());
        buyerRepository.save(buyer);
        //记录抽奖日志

        return turntableDetail1;
    }
}