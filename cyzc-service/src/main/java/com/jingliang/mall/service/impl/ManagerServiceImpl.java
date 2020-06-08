package com.jingliang.mall.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.GoldLog;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.repository.*;
import com.jingliang.mall.service.ManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

import java.util.*;

@Service
@Slf4j
public class ManagerServiceImpl implements ManagerService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final BuyerRepository buyerRepository;
    private final GoldLogRepository goldLogRepository;
    private final OrderRepository orderRepository;
    private static String logStr;

    public ManagerServiceImpl(GroupRepository groupRepository, UserRepository userRepository, BuyerRepository buyerRepository, GoldLogRepository goldLogRepository, OrderRepository orderRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.buyerRepository = buyerRepository;
        this.goldLogRepository = goldLogRepository;
        this.orderRepository = orderRepository;
    }


    /*
     * 根据大区查询绩效
     * */
    @Override
    public List<Map<String, Object>> findAchievementsByMyself(Date startTime, Date endTime) {
        //查询所有绩效
        return groupRepository.findAchievementsByMyself(startTime, endTime);
    }

    /*
     * 根据大区下的子分组查询绩效
     * */
    @Override
    public List<Map<String, Object>> findGroupAchievement(Long parentGroupId, Date startTime, Date endTime) {
        //根据大区下的子分组查询绩效
        return groupRepository.findGroupAchievement(parentGroupId, startTime, endTime);
    }

    /*
     * 根据大区下的子分组查询绩效
     * */
    @Override
    public List<Map<String, Object>> findGroupAchievementByGroupNo(String groupNo, Date startTime, Date endTime) {
        //根据大区下的子分组查询绩效
        return groupRepository.findGroupAchievementByGroupNo(groupNo, startTime, endTime);
    }

    /*
     * 根据分区查询销售绩效
     * */
    @Override
    public List<Map<String, Object>> findUserAchievement(String groupNo, Date startTime, Date endTime) {
        //根据分区查询销售绩效
        return groupRepository.findUserAchievement(groupNo, startTime, endTime);
    }

    /*
     * 根据年份进行查询-折线图（总绩效）
     * */
    @Override
    public List<Map<String, Object>> findGroupAchievementWithTimeByYear(Long parentGroupId, Date startTime, Date endTime) {
        return groupRepository.findGroupAchievementWithTimeByYear(parentGroupId, startTime, endTime);
    }

    /*
     * 根据月份进行查询-折线图（总绩效）
     * */
    @Override
    public List<Map<String, Object>> findGroupAchievementWithTimeByMonth(Long parentGroupId, Date startTime, Date endTime) {
        return groupRepository.findGroupAchievementWithTimeByMonth(parentGroupId, startTime, endTime);
    }

    /*
     * 根据天数进行查询-折线图（总绩效）
     * */
    @Override
    public List<Map<String, Object>> findGroupAchievementWithTimeByDay(Long parentGroupId, Date startTime, Date endTime) {
        return groupRepository.findGroupAchievementWithTimeByDay(parentGroupId, startTime, endTime);
    }

    /*
     * 根据分组查询下单量
     * */
    @Override
    public List<Map<String, Object>> findOrdersTotalByGroup(Long parentGroupId, Date startTime, Date endTime) {
        return groupRepository.findOrdersTotalByGroup(parentGroupId, startTime, endTime);
    }

    /*
     * 根据年份查询订单量-折线
     * */
    @Override
    public List<Map<String, Object>> findOrdersTotalByGroupAndYear(Long parentGroupId, Date startTime, Date endTime) {
        return groupRepository.findOrdersTotalByGroupAndYear(parentGroupId, startTime, endTime);
    }

    /*
     * 根据月份查询订单量-折线
     * */
    @Override
    public List<Map<String, Object>> findOrdersTotalByGroupAndMonth(Long parentGroupId, Date startTime, Date endTime) {
        return groupRepository.findOrdersTotalByGroupAndMonth(parentGroupId, startTime, endTime);
    }

    /*
     * 根据天数查询订单量-折线
     * */
    @Override
    public List<Map<String, Object>> findOrdersTotalByGroupAndDay(Long parentGroupId, Date startTime, Date endTime) {
        return groupRepository.findOrdersTotalByGroupAndDay(parentGroupId, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> getBuyerTop30() {
        return groupRepository.getBuyerTop30();
    }

    @Override
    public List<User> findSaleByGroup(String groupNo) {
        return userRepository.findAllByIsAvailableAndGroupNo(true, groupNo);
    }

    @Override
    public List<Map<String, Object>> findGroupAchievementWithTimeByYearAndGroupNo(String groupNo, Date startTime, Date endTime) {
        return groupRepository.findGroupAchievementWithTimeByYearAndGroupNo(groupNo, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> findGroupAchievementWithTimeByMonthAndGroupNo(String groupNo, Date startTime, Date endTime) {
        return groupRepository.findGroupAchievementWithTimeByMonthAndGroupNo(groupNo, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> findGroupAchievementWithTimeByDayAndGroupNo(String groupNo, Date startTime, Date endTime) {
        return groupRepository.findGroupAchievementWithTimeByDayAndGroupNo(groupNo, startTime, endTime);
    }

    @Transactional
    @Override
    public Map<String, Integer> findGoldDontReturn(Date createTime) {
        //传参查询5.26号之后的订单，因为程序在5.26号之后发生问题
        List<Order> orders = orderRepository.findAllByCreateTimeAfterAndIsAvailableAndOrderStatusGreaterThanEqual(createTime, true, 300);
        Map<String, Integer> resultOrder = new HashMap<>();
        int returnGoldNull = 0;
        int goldLogNull = 0;
        for (Order order : orders) {
            Buyer buyer = buyerRepository.findBuyersByIdAndAndIsAvailable(order.getBuyerId(), true);
            //找到订单编号300-500的订单，查看订单是否有returnGold，若没有，加上
            if (order.getOrderStatus() >= 300 && order.getOrderStatus() <= 500 && buyer.getOrderSpecificNum() > 0) {
                if (Objects.isNull(order.getReturnGold()) || Objects.equals(order.getReturnGold(), 0)) {
                    order.setReturnGold((int) (order.getPayableFee() / 1000));
                    orderRepository.save(order);
                    logStr = JSONObject.toJSONString((int) (order.getPayableFee() / 1000));
                    log.info("未返币订单重新返币记录:{}", logStr);
                    returnGoldNull++;
                }
            }
            //找到订单状态为600的用户，查看是否有返币日志，若没有，重新给用户返币，扣减返币次数并进行日志存储
            if (Objects.equals(order.getOrderStatus(), 600) && buyer.getOrderSpecificNum() > 0) {
                if (Objects.isNull(order.getReturnGold()) || Objects.equals(order.getReturnGold(), 0)) {
                    order.setReturnGold((int) (order.getPayableFee() / 1000));
                    orderRepository.save(order);
                }
                if (goldLogRepository.findAllByPayNoAndIsAvailable(order.getOrderNo(), true).size() == 0 || Objects.isNull(goldLogRepository.findAllByPayNoAndIsAvailable(order.getOrderNo(), true))) {
                    buyer.setGold(buyer.getGold() + (int) (order.getPayableFee() / 1000));
                    buyer.setOrderSpecificNum(buyer.getOrderSpecificNum() - 1);
                    buyerRepository.save(buyer);
                    GoldLog goldLog = new GoldLog();
                    goldLog.setBuyerId(buyer.getId());
                    goldLog.setGold((int) (order.getPayableFee() / 1000));
                    goldLog.setCreateTime(new Date());
                    goldLog.setType(400);
                    goldLog.setIsAvailable(true);
                    goldLog.setMoney(order.getPayableFee().intValue());
                    goldLog.setPayNo(order.getOrderNo());
                    goldLog.setMsg("微信支付订单[" + order.getOrderNo() + "]￥" + (float) order.getPayableFee() / 100 + "元,获得" + (int) (order.getPayableFee() / 1000) + "金币");
                    goldLogRepository.save(goldLog);
                    logStr = JSONObject.toJSONString(goldLog);
                    log.info("用户返回金币记录:{}", logStr);
                    goldLogNull++;
                }
            }
        }
        resultOrder.put("未返币订单重新返币订单数：", returnGoldNull);
        resultOrder.put("订单完成用户金币未到账订单数：", goldLogNull);
        return resultOrder;
    }
}
