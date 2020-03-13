package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Turntable;
import com.jingliang.mall.repository.TurntableRepository;
import com.jingliang.mall.service.TurntableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    public TurntableServiceImpl(TurntableRepository turntableRepository) {
        this.turntableRepository = turntableRepository;
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
}