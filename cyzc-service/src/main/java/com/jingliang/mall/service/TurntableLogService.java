package com.jingliang.mall.service;

import com.jingliang.mall.entity.TurntableLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 转盘日志Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
public interface TurntableLogService {

    /**
     * 分页查询
     *
     * @param buyerId
     * @param pageRequest
     * @return
     */
    Page<TurntableLog> pageAll(Long buyerId, PageRequest pageRequest);

    /**
     * 查询前100条中奖纪录
     *
     * @param pageRequest
     * @return
     */
    List<TurntableLog> prizeAll(PageRequest pageRequest);
}