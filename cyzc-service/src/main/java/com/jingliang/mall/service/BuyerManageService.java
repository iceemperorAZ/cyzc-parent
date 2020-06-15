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

    /**
     * 根据父id查询子区每年的用户量
     *
     * @param startTime
     * @param endTime
     * @param groupNo
     * @return
     */
    List<Map<String, Object>> yearByDateAndGroupNoAchievement(Date startTime, Date endTime, String groupNo);

    /**
     * 根据父id查询子区每月的用户量
     *
     * @param startTime
     * @param endTime
     * @param groupNo
     * @return
     */
    List<Map<String, Object>> monthByDateAndGroupNoAchievement(Date startTime, Date endTime, String groupNo);

    /**
     * 根据父id查询子区每天的用户量
     *
     * @param startTime
     * @param endTime
     * @param groupNo
     * @return
     */
    List<Map<String, Object>> daysByDateAndGroupNoAchievement(Date startTime, Date endTime, String groupNo);

    /**
     * 销售新增商户统计
     *
     * @return
     */
    List<Map<String, String>> countsByUserId();

    /**
     * 当前年销售下单量top
     *
     * @param startTime
     * @param endTime
     * @param topNum
     * @return
     */
    List<Map<String, Object>> topOfOrderCountsByUser(Date startTime, Date endTime, Integer topNum);

    /**
     * 当前年商品销售量top
     *
     * @param startTime
     * @param endTime
     * @param topNum
     * @return
     */
    List<Map<String, Object>> yeartopOfProductCountsByOrder(Date startTime, Date endTime, Integer topNum);

    /**
     * 当前月商品销售量top
     *
     * @param startTime
     * @param endTime
     * @param topNum
     * @return
     */
    List<Map<String, Object>> monthtopOfProductCountsByOrder(Date startTime, Date endTime, Integer topNum);

    /**
     * 查询所有可用商户
     *
     * @return
     */
    List<Map<String, Object>> searchAllBuyer();

    /*
     * *
     * 查询所有未绑定销售的商户
     * * */
    List<Map<String, Object>> searchBuyerDontHaveSale();

    /*
     * *
     * 查询所有绑定销售的商户
     * * */
    List<Map<String, Object>> searchBuyerHaveSale();

    /**
     * 查询销售下的商户定位
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> findBuyerAddressByUserId(Long userId);

    /**
     * 月新增商户数
     *
     * @return
     */
    List<Map<String, Object>> findBuyerCountsToMonth();

    /**
     * 日新增商户数
     *
     * @return
     */
    List<Map<String, Object>> findBuyerCountsToDay();

}
