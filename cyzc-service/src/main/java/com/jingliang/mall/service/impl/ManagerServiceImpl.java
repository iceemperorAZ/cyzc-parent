package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.User;
import com.jingliang.mall.repository.GroupRepository;
import com.jingliang.mall.repository.UserRepository;
import com.jingliang.mall.service.ManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ManagerServiceImpl implements ManagerService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public ManagerServiceImpl(GroupRepository groupRepository, UserRepository userRepository){
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }


    /*
    * 根据大区查询绩效
    * */
    @Override
    public List<Map<String, Object>> findAchievementsByMyself(Date startTime, Date endTime) {
        //查询所有绩效
        return groupRepository.findAchievementsByMyself(startTime,endTime);
    }

    /*
    * 根据大区下的子分组查询绩效
    * */
    @Override
    public List<Map<String, Object>> findGroupAchievement(Long parentGroupId, Date startTime, Date endTime) {
        //根据大区下的子分组查询绩效
        return groupRepository.findGroupAchievement(parentGroupId,startTime,endTime);
    }

    /*
     * 根据大区下的子分组查询绩效
     * */
    @Override
    public List<Map<String, Object>> findGroupAchievementByGroupNo(String groupNo, Date startTime, Date endTime) {
        //根据大区下的子分组查询绩效
        return groupRepository.findGroupAchievementByGroupNo(groupNo,startTime,endTime);
    }

    /*
     * 根据分区查询销售绩效
     * */
    @Override
    public List<Map<String, Object>> findUserAchievement(String groupNo, Date startTime, Date endTime) {
        //根据分区查询销售绩效
        return groupRepository.findUserAchievement(groupNo,startTime,endTime);
    }

    /*
     * 根据年份进行查询-折线图（总绩效）
     * */
    @Override
    public List<Map<String, Object>> findGroupAchievementWithTimeByYear(Long parentGroupId,Date startTime, Date endTime) {
        return groupRepository.findGroupAchievementWithTimeByYear(parentGroupId,startTime,endTime);
    }

    /*
     * 根据月份进行查询-折线图（总绩效）
     * */
    @Override
    public List<Map<String, Object>> findGroupAchievementWithTimeByMonth(Long parentGroupId,Date startTime, Date endTime) {
        return groupRepository.findGroupAchievementWithTimeByMonth(parentGroupId,startTime,endTime);
    }

    /*
     * 根据天数进行查询-折线图（总绩效）
     * */
    @Override
    public List<Map<String, Object>> findGroupAchievementWithTimeByDay(Long parentGroupId,Date startTime, Date endTime) {
        return groupRepository.findGroupAchievementWithTimeByDay(parentGroupId,startTime,endTime);
    }

    /*
    * 根据分组查询下单量
    * */
    @Override
    public List<Map<String, Object>> findOrdersTotalByGroup(Long parentGroupId, Date startTime, Date endTime) {
        return groupRepository.findOrdersTotalByGroup(parentGroupId,startTime,endTime);
    }

    /*
     * 根据年份查询订单量-折线
     * */
    @Override
    public List<Map<String, Object>> findOrdersTotalByGroupAndYear(Long parentGroupId, Date startTime, Date endTime) {
        return groupRepository.findOrdersTotalByGroupAndYear(parentGroupId,startTime,endTime);
    }

    /*
     * 根据月份查询订单量-折线
     * */
    @Override
    public List<Map<String, Object>> findOrdersTotalByGroupAndMonth(Long parentGroupId, Date startTime, Date endTime) {
        return groupRepository.findOrdersTotalByGroupAndMonth(parentGroupId,startTime,endTime);
    }

    /*
     * 根据天数查询订单量-折线
     * */
    @Override
    public List<Map<String, Object>> findOrdersTotalByGroupAndDay(Long parentGroupId, Date startTime, Date endTime) {
        return groupRepository.findOrdersTotalByGroupAndDay(parentGroupId,startTime,endTime);
    }

    @Override
    public List<Map<String, Object>> getBuyerTop30() {
        return groupRepository.getBuyerTop30();
    }

    @Override
    public List<User> findSaleByGroup(String groupNo) {
        return userRepository.findAllByIsAvailableAndGroupNo(true,groupNo);
    }

    @Override
    public List<Map<String, Object>> findGroupAchievementWithTimeByYearAndGroupNo(String groupNo, Date startTime, Date endTime) {
        return groupRepository.findGroupAchievementWithTimeByYearAndGroupNo(groupNo,startTime,endTime);
    }

    @Override
    public List<Map<String, Object>> findGroupAchievementWithTimeByMonthAndGroupNo(String groupNo, Date startTime, Date endTime) {
        return groupRepository.findGroupAchievementWithTimeByMonthAndGroupNo(groupNo,startTime,endTime);
    }

    @Override
    public List<Map<String, Object>> findGroupAchievementWithTimeByDayAndGroupNo(String groupNo, Date startTime, Date endTime) {
        return groupRepository.findGroupAchievementWithTimeByDayAndGroupNo(groupNo,startTime,endTime);
    }
}
