package com.jingliang.mall.service;

import com.jingliang.mall.entity.Turntable;
import com.jingliang.mall.entity.TurntableDetail;
import com.jingliang.mall.entity.User;

import java.util.List;

/**
 * 转盘详情Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
public interface TurntableDetailService {

    /**
     * 保存
     *
     * @param turntableDetail
     * @return
     */
    TurntableDetail save(TurntableDetail turntableDetail);

    /**
     * 根据转盘Id查询转盘详情
     *
     * @param turntableId
     * @return
     */
    List<TurntableDetail> findAllByShow(Long turntableId);

    /**
     * 根据转盘Id查询转盘详情
     *
     * @param turntableId
     * @return
     */
    List<TurntableDetail> findAll(Long turntableId);

    /**
     * 根据主键删除
     *
     * @param id
     * @param userId
     */
    void delete(Long id, Long userId);

    /**
     * 上下架
     *
     * @param user
     * @param turntableDetail
     * @return
     */
    TurntableDetail show(User user, TurntableDetail turntableDetail);

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    TurntableDetail findById(Long id);
}