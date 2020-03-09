package com.jingliang.mall.service;

import com.jingliang.mall.entity.Techarge;

import java.util.List;

/**
 * 充值配置Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 10:14:25
 */
public interface TechargeService {

    /**
     * 保存/更新
     *
     * @param techarge
     * @return
     */
    Techarge save(Techarge techarge);

    /**
     * 查询全部
     *
     * @return
     */
    List<Techarge> findAll();

    /**
     * 根据金币数查询
     *
     * @param totalFee
     * @return
     */
    Techarge findByMoney(Integer totalFee);

    /**
     * 审批
     *
     * @param userId
     * @param techarge
     * @return
     */
    Techarge show(Long userId, Techarge techarge);

    /**
     * 审批
     *
     * @param userId
     * @param techarge
     * @return
     */
    Techarge hide(Long userId, Techarge techarge);

    /**
     * 审批
     *
     * @param userId
     * @param techarge
     * @return
     */
    Techarge delete(Long userId, Techarge techarge);

    /**
     * 查询全部审批通过的
     *
     * @return
     */
    List<Techarge> findAllShow();

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    Techarge findById(Long id);
}