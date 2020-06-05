package com.jingliang.mall.service.impl;

import com.jingliang.mall.repository.OrderRepository;
import com.jingliang.mall.service.WechatManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 微信管理服务
 *
 * @author Zhenfeng Li
 * @date 2019-12-12 14:21:42
 */
@Service
@Slf4j
public class WechatManageServiceImpl implements WechatManageService {

    private final OrderRepository orderRepository;

    public WechatManageServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Map<String, Object>> bossONeGroupAchievement(String groupNO, Date startTime, Date endTime) {
        return orderRepository.bossONeGroupAchievement(groupNO, startTime, endTime);
    }


    @Override
    public List<Map<String, Object>> bossGroupAchievement(Long parentGroupId, Date startTime, Date endTime) {
        return orderRepository.bossGroupAchievement(parentGroupId, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> bossGroupUserAchievement(String groupNo, Date startTime, Date endTime) {
        return orderRepository.bossGroupUserAchievement(groupNo, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> bossUserBuyerAchievement(String groupNo, Long saleUserId, Date startTime, Date endTime) {
        return orderRepository.bossUserBuyerAchievement(groupNo, saleUserId, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> bossSelfBuyerAchievement(Long saleUserId, Date startTime, Date endTime) {
        return orderRepository.bossSelfBuyerAchievement(saleUserId, startTime, endTime);
    }

    @Override
    public Map<String, Object> bossSelfAchievement(Long saleUserId, Date startTime, Date endTime) {
        return orderRepository.bossSelfAchievement(saleUserId, startTime, endTime);
    }

    @Override
    public Map<String, Object> bossUserAchievement(String groupNo, Long saleUserId, Date startTime, Date endTime) {
        return orderRepository.bossUserAchievement(groupNo, saleUserId, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> bossSelfBuyerOrderAchievement(Long saleUserId, Long buyerId, Date startTime, Date endTime) {
        return orderRepository.bossSelfBuyerOrderAchievement(saleUserId, buyerId, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> bossUserBuyerOrderAchievement(String groupNo, Long buyerId, Date startTime, Date endTime) {
        return orderRepository.bossUserBuyerOrderAchievement(groupNo, buyerId, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> bossBuyerOrderDetailAchievement(Long orderId, Date startTime, Date endTime) {
        return orderRepository.bossBuyerOrderDetailAchievement(orderId, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> bossGroupProductTypeAchievement(String groupNo, Date startTime, Date endTime) {
        return orderRepository.bossGroupProductTypeAchievement(groupNo, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> bossGroupProductAchievement(String groupNo, Date startTime, Date endTime) {
        return orderRepository.bossGroupProductAchievement(groupNo, startTime, endTime);
    }

    @Override
    public Integer countBuyerAll() {
        return orderRepository.countBuyerAll();
    }

    @Override
    public Integer countActiveBuyerAll() {
        return orderRepository.countActiveBuyerAll();
    }

    @Override
    public Integer countInactiveBuyerAll() {
        return orderRepository.countInactiveBuyerAll();
    }

    @Override
    public List<Map<String, Object>> monthIncrease(String groupNo, Date date) {
        return orderRepository.monthIncrease(groupNo, date);
    }

    @Override
    public List<Map<String, Object>> dayIncrease(String groupNo, Date date) {
        return orderRepository.dayIncrease(groupNo, date);
    }

    @Override
    public List<Map<String, Object>> allIncrease(String groupNo) {
        return orderRepository.allIncrease(groupNo);
    }
}
