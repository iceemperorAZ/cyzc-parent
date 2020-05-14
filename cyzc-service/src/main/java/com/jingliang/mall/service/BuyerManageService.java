package com.jingliang.mall.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商户管理
 *
 * @author lmd
 * @date 2020/5/11
 * @company 晶粮
 */
public interface BuyerManageService {


    /**
     * 通过组编号查询商户总数
     *
     * @param startTime
     * @param endTime
     * @param groupNo
     * @return
     */
    List<Map<String, Object>> dateAndGroupNoAchievement(Date startTime, Date endTime, String groupNo);

    /**
     * 通过父组id查询商户数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> dateAndParentGroupIdAchievement(Date startTime, Date endTime, Long parentGroupId);

    /**
     * 查询组内销售名下的商户数
     *
     * @param startTime
     * @param endTime
     * @param groupNo
     * @return
     */
    List<Map<String, Object>> userByGroupNoAchievement(Date startTime, Date endTime, String groupNo);

    /**
     * 根据父id查询子区每年的用户量
     *
     * @param startTime
     * @param endTime
     * @param parentGroupId
     * @return
     */
    List<Map<String, Object>> yearByDateAndParentGroupIdAchievement(Date startTime, Date endTime, Long parentGroupId);

    /**
     * 根据父id查询子区每月的用户量
     *
     * @param startTime
     * @param endTime
     * @param parentGroupId
     * @return
     */
    List<Map<String, Object>> monthByDateAndParentGroupIdAchievement(Date startTime, Date endTime, Long parentGroupId);

    /**
     * 根据父id查询子区每天的用户量
     *
     * @param startTime
     * @param endTime
     * @param parentGroupId
     * @return
     */
    List<Map<String, Object>> daysByDateAndParentGroupIdAchievement(Date startTime, Date endTime, Long parentGroupId);
}
