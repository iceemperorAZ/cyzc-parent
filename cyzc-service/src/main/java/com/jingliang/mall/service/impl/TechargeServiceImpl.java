package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Techarge;
import com.jingliang.mall.repository.TechargeRepository;
import com.jingliang.mall.service.TechargeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 充值配置ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 10:14:25
 */
@Service
@Slf4j
public class TechargeServiceImpl implements TechargeService {

    private final TechargeRepository techargeRepository;

    public TechargeServiceImpl(TechargeRepository techargeRepository) {
        this.techargeRepository = techargeRepository;
    }

    @Override
    public Techarge save(Techarge techarge) {
        techarge.setIsShow(false);
        return techargeRepository.save(techarge);
    }

    @Override
    public List<Techarge> findAll() {
        return techargeRepository.findAllByIsAvailableOrderByCreateTimeDesc(true);
    }

    @Override
    public Techarge findByMoney(Integer totalFee) {
        return techargeRepository.findFirstByMoneyAndIsAvailable(totalFee, true);
    }

    @Override
    public Techarge show(Long userId, Techarge techarge) {
        techarge = techargeRepository.findAllByIdAndIsAvailable(techarge.getId(), true);
        techarge.setShowTime(new Date());
        techarge.setShowId(userId);
        techarge.setIsShow(true);
        return techargeRepository.save(techarge);
    }

    @Override
    public Techarge hide(Long userId, Techarge techarge) {
        techarge = techargeRepository.findAllByIdAndIsAvailable(techarge.getId(), true);
        techarge.setShowTime(new Date());
        techarge.setShowId(userId);
        techarge.setIsShow(false);
        return techargeRepository.save(techarge);
    }

    @Override
    public Techarge delete(Long userId, Techarge techarge) {
        techarge = techargeRepository.findAllByIdAndIsAvailable(techarge.getId(), true);
        techarge.setShowTime(new Date());
        techarge.setShowId(userId);
        techarge.setIsShow(false);
        techarge.setIsAvailable(false);
        return techargeRepository.save(techarge);
    }

    @Override
    public List<Techarge> findAllShow() {
        return techargeRepository.findAllByIsShowAndIsAvailableOrderByMoneyAsc(true, true);
    }

    @Override
    public Techarge findById(Long id) {
        return techargeRepository.findFirstByIdAndIsShowAndIsAvailable(id, true, true);
    }
}