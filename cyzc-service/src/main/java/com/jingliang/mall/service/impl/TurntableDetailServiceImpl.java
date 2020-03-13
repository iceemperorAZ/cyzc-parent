package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.TurntableDetail;
import com.jingliang.mall.repository.TurntableDetailRepository;
import com.jingliang.mall.service.TurntableDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 转盘详情ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@Service
@Slf4j
public class TurntableDetailServiceImpl implements TurntableDetailService {

    private final TurntableDetailRepository turntableDetailRepository;

    public TurntableDetailServiceImpl(TurntableDetailRepository turntableDetailRepository) {
        this.turntableDetailRepository = turntableDetailRepository;
    }

    @Override
    public TurntableDetail save(TurntableDetail turntableDetail) {
        return turntableDetailRepository.save(turntableDetail);
    }

    @Override
    public List<TurntableDetail> findAll(Long turntableId) {
        return turntableDetailRepository.findAllByTurntableIdAndIsAvailable(turntableId, true);
    }

    @Override
    public void delete(Long id, Long userId) {
        TurntableDetail turntableDetail = turntableDetailRepository.getOne(id);
        turntableDetail.setIsAvailable(false);
        turntableDetailRepository.save(turntableDetail);
    }
}