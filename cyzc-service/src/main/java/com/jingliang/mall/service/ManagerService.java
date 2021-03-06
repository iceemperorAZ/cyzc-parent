package com.jingliang.mall.service;

import com.jingliang.mall.entity.Order;
import com.jingliang.mall.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ManagerService {

    public List<Map<String, Object>> findAchievementsByMyself(Date startTime, Date endTime);

    public List<Map<String, Object>> findGroupAchievement(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> findGroupAchievementByGroupNo(String groupNo, Date startTime, Date endTime);

    public List<Map<String, Object>> findUserAchievement(String groupNo, Date startTime, Date endTime);

    public List<Map<String, Object>> findGroupAchievementWithTimeByYear(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> findGroupAchievementWithTimeByMonth(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> findGroupAchievementWithTimeByDay(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> findOrdersTotalByGroup(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> findOrdersTotalByGroupAndYear(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> findOrdersTotalByGroupAndMonth(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> findOrdersTotalByGroupAndDay(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> getBuyerTop30();

    List<User> findSaleByGroup(String groupNo);

    List<Map<String, Object>> findAllBySaleIdAndGroupNo(String groupNo);

    List<Map<String, Object>> findGroupAchievementWithTimeByYearAndGroupNo(String groupNo, Date startTime, Date endTime);

    List<Map<String, Object>> findGroupAchievementWithTimeByMonthAndGroupNo(String groupNo, Date startTime, Date endTime);

    List<Map<String, Object>> findGroupAchievementWithTimeByDayAndGroupNo(String groupNo, Date startTime, Date endTime);

    Map<String, Integer> findGoldDontReturn(Date createTime);

    /**
     * 查询前十天-折线图（总绩效）
     *
     * @return
     */
    List<Map<String, Object>> findGroupAchievementWithTimeBy10DayLate();

    /**
     * 查询各类商品的销售情况
     *
     * @param groupNo
     * @return
     */
    List<Map<String, Object>> findProductTypeSalePrice(String groupNo);

    /**
     * 查询各类商品的销售情况
     *
     * @return
     */
    List<Map<String, Object>> findProductTypeSalePrice();

    /**
     * 查询前十天-折线图（总绩效）
     *
     * @param groupNo
     * @return
     */
    List<Map<String, Object>> findGroupAchievementWithTimeByGroupNo10DayLate(String groupNo);

}
