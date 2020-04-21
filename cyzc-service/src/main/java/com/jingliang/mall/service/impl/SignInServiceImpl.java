package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.GoldLog;
import com.jingliang.mall.entity.SignIn;
import com.jingliang.mall.repository.BuyerRepository;
import com.jingliang.mall.repository.GoldLogRepository;
import com.jingliang.mall.repository.SignInRepository;
import com.jingliang.mall.service.SignInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 签到日志ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-03 10:42:56
 */
@Service
@Slf4j
public class SignInServiceImpl implements SignInService {

    private final SignInRepository signInRepository;
    private final GoldLogRepository signInLogRepository;
    private final BuyerRepository buyerRepository;

    public SignInServiceImpl(SignInRepository signInRepository, GoldLogRepository signInLogRepository, BuyerRepository buyerRepository) {
        this.signInRepository = signInRepository;
        this.signInLogRepository = signInLogRepository;
        this.buyerRepository = buyerRepository;
    }

    @Override
    public SignIn findByBuyerIdAndLastDay(Long buyerId) {
        return signInRepository.findByBuyerIdOrderByLastDateDesc(buyerId);
    }

    @Override
    public SignIn signIn(Long buyerId) {
        List<Integer> singInList = Arrays.asList(5, 5, 5, 5, 5, 5, 10);
        LocalDate localDate = LocalDate.now();
        SignIn signIn = signInRepository.findByBuyerIdOrderByLastDateDesc(buyerId);
        Buyer buyer = buyerRepository.findAllByIdAndIsAvailable(buyerId, true);
        if (signIn == null) {
            //表示头一次签到
            signIn = new SignIn();
            signIn.setBuyerId(buyerId);
            signIn.setDayNum(1);
            signIn.setIsAvailable(true);
            signIn.setLastDate(new Date());
            buyer.setGold(buyer.getGold() == null ? singInList.get(0) : buyer.getGold() + singInList.get(0));
            buyerRepository.save(buyer);
            //记录签到日志
            GoldLog goldLog = new GoldLog();
            goldLog.setBuyerId(buyerId);
            goldLog.setCreateTime(new Date());
            goldLog.setType(100);
            goldLog.setIsAvailable(true);
            goldLog.setMoney(0);
            goldLog.setGold(singInList.get(0));
            goldLog.setMsg("签到获得" + singInList.get(0) + "金币");
            signInLogRepository.save(goldLog);
            return signInRepository.save(signIn);
        }
        //表示之前签到过
        localDate = localDate.plusDays(-1);
        if (localDate.isEqual(signIn.getLastDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            //表示前一天签到过
            //连续签到+1 如果大于7天就从新开始计算
            signIn.setDayNum((signIn.getDayNum() + 1) > singInList.size() ? 1 : signIn.getDayNum() + 1);
            signIn.setLastDate(new Date());
            buyer.setGold(buyer.getGold() + singInList.get(signIn.getDayNum() - 1));
            buyerRepository.save(buyer);
            //记录签到日志
            GoldLog goldLog = new GoldLog();
            goldLog.setBuyerId(buyerId);
            goldLog.setCreateTime(new Date());
            goldLog.setType(100);
            goldLog.setIsAvailable(true);
            goldLog.setMoney(0);
            goldLog.setGold(singInList.get(signIn.getDayNum() - 1));
            goldLog.setMsg("签到获得" + singInList.get(signIn.getDayNum() - 1) + "金币");
            signInLogRepository.save(goldLog);
            return signInRepository.save(signIn);
        }
        //非连续签到 从新开始计算
        signIn.setDayNum(1);
        signIn.setIsAvailable(true);
        signIn.setLastDate(new Date());
        buyer.setGold(buyer.getGold() + singInList.get(0));
        buyerRepository.save(buyer);
        //记录签到日志
        GoldLog goldLog = new GoldLog();
        goldLog.setBuyerId(buyerId);
        goldLog.setCreateTime(new Date());
        goldLog.setIsAvailable(true);
        goldLog.setType(100);
        goldLog.setMoney(0);
        goldLog.setGold(singInList.get(signIn.getDayNum() - 1));
        goldLog.setMsg("签到获得" + singInList.get(0) + "金币");
        signInLogRepository.save(goldLog);
        return signInRepository.save(signIn);
    }

    @Override
    public SignIn querySignIn(Long buyerId) {
        return signInRepository.findFirstByBuyerIdAndLastDateGreaterThanEqual(buyerId, Date.from(LocalDate.now().plusDays(-1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
}