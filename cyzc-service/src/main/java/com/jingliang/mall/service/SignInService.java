package com.jingliang.mall.service;

import com.jingliang.mall.entity.SignIn;

import java.time.LocalDate;

/**
 * 签到日志Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-03 10:42:56
 */
public interface SignInService {

    /**
     * 查询签到记录
     *
     * @param buyerId
     * @return
     */
    SignIn findByBuyerIdAndLastDay(Long buyerId);

    /**
     * 签到
     * @param buyerId
     * @return
     */
    SignIn signIn(Long buyerId);

    /**
     * 查询签到
     * @param buyerId
     * @return
     */
    SignIn querySignIn(Long buyerId);
}