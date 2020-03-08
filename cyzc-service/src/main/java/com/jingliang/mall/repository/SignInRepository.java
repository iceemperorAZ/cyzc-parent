package com.jingliang.mall.repository;

import com.jingliang.mall.entity.SignIn;
import com.jingliang.mall.repository.base.BaseRepository;

import java.util.Date;

/**
 * 签到日志Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-03 10:42:56
 */
public interface SignInRepository extends BaseRepository<SignIn, Long> {

    /**
     * 查询签到
     *
     * @param buyerId
     * @return
     */
    SignIn findByBuyerIdOrderByLastDateDesc(Long buyerId);

    /**
     * 查询签到
     *
     * @param buyerId
     * @param lastDate
     * @return
     */
    SignIn findFirstByBuyerIdAndLastDateGreaterThanEqual(Long buyerId, Date lastDate);

}