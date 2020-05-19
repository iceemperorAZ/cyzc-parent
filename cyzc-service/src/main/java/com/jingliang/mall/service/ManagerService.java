package com.jingliang.mall.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ManagerService {

    public List<Map<String, Object>> findAchievementsByMyself(Date startTime, Date endTime);

    public List<Map<String, Object>> findGroupAchievement(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> findUserAchievement(String groupNo, Date startTime, Date endTime);

    public List<Map<String, Object>> findGroupAchievementWithTimeByYear(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> findGroupAchievementWithTimeByMonth(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> findGroupAchievementWithTimeByDay(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> findOrdersTotalByGroup(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> findOrdersTotalByGroupAndYear(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> findOrdersTotalByGroupAndMonth(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> findOrdersTotalByGroupAndDay(Long parentGroupId, Date startTime, Date endTime);

    public List<Map<String, Object>> getBuyerTop30();
}
