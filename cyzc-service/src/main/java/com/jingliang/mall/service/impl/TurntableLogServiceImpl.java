package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.TurntableLog;
import com.jingliang.mall.repository.TurntableLogRepository;
import com.jingliang.mall.service.TurntableLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 转盘日志ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@Service
@Slf4j
public class TurntableLogServiceImpl implements TurntableLogService {

    private final TurntableLogRepository turntableLogRepository;

    public TurntableLogServiceImpl(TurntableLogRepository turntableLogRepository) {
        this.turntableLogRepository = turntableLogRepository;
    }

    @Override
    public Page<TurntableLog> pageAll(Long buyerId, PageRequest pageRequest) {
        return turntableLogRepository.findAllByBuyerIdAndIsAvailableOrderByCreateTimeDesc(buyerId, true, pageRequest);
    }

    @Override
    public List<TurntableLog> prizeAll(PageRequest pageRequest) {
        Page<TurntableLog> turntableLogs = turntableLogRepository.findAllByTypeGreaterThanAndIsAvailable(100, true, pageRequest);
        if (turntableLogs.getSize() > 0) {
            return turntableLogs.getContent();
        }
        return new ArrayList<>();
    }
}