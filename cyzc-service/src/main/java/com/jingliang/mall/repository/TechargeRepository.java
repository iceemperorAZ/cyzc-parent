package com.jingliang.mall.repository;

import com.jingliang.mall.entity.Techarge;
import com.jingliang.mall.repository.base.BaseRepository;

import java.util.List;

/**
 * 充值配置Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-04 10:14:25
 */
public interface TechargeRepository extends BaseRepository<Techarge, Long> {

    /**
     * 根据钱数查
     *
     * @param money
     * @param isAvailable
     * @return
     */
    Techarge findFirstByMoneyAndIsAvailable(Integer money, Boolean isAvailable);

    /**
     * 查询全部
     *
     * @param isAvailable
     * @return
     */
    List<Techarge> findAllByIsAvailableOrderByCreateTimeDesc(Boolean isAvailable);

    /**
     * 查询全部
     *
     * @param isShow
     * @param isAvailable
     * @return
     */
    List<Techarge> findAllByIsShowAndIsAvailableOrderByMoneyAsc(Boolean isShow,Boolean isAvailable);

    Techarge findFirstByIdAndIsShowAndIsAvailable(Long id, Boolean isShow,Boolean isAvailable);
}