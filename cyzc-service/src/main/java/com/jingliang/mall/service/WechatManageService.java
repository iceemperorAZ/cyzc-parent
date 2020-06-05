package com.jingliang.mall.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 微信管理服务
 *
 * @author Zhenfeng Li
 * @date 2019-12-12 14:21:29
 */
public interface WechatManageService {
    /**
     * 根据组编号统计组下的业绩
     *
     * @param groupNo
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> bossONeGroupAchievement(String groupNo, Date startTime, Date endTime);

    /**
     * 根据分组查询组下业绩
     *
     * @param parentGroupId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> bossGroupAchievement(Long parentGroupId, Date startTime, Date endTime);

    /**
     * 根据分组查询分组下的用户（销售、区域经理、老板）业绩
     *
     * @param groupNo
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> bossGroupUserAchievement(String groupNo, Date startTime, Date endTime);

    /**
     * 统计指定销售在自己组下所有商户产生的绩效
     *
     * @param groupNo
     * @param saleUserId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> bossUserBuyerAchievement(String groupNo, Long saleUserId, Date startTime, Date endTime);

    /**
     * 查询销售自己所有商户产生的绩效
     *
     * @param saleUserId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> bossSelfBuyerAchievement(Long saleUserId, Date startTime, Date endTime);

    /**
     * 查询销售自己所有商户产生的总绩效
     *
     * @param saleUserId
     * @param startTime
     * @param endTime
     * @return
     */
    Map<String, Object> bossSelfAchievement(Long saleUserId, Date startTime, Date endTime);

    /**
     * 查询指定销售在自己组下所有商户产生的总绩效
     *
     * @param groupNo
     * @param saleUserId
     * @param startTime
     * @param endTime
     * @return
     */
    Map<String, Object> bossUserAchievement(String groupNo, Long saleUserId, Date startTime, Date endTime);

    /**
     * 查询销售自己的商户所有订单产生的绩效
     *
     * @param saleUserId
     * @param buyerId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> bossSelfBuyerOrderAchievement(Long saleUserId, Long buyerId, Date startTime, Date endTime);

    /**
     * 查询指定销售在自己组下的商户所有订单产生的绩效
     *
     * @param groupNo
     * @param buyerId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> bossUserBuyerOrderAchievement(String groupNo, Long buyerId, Date startTime, Date endTime);

    /**
     * 查询订单详情产生的绩效
     *
     * @param orderId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> bossBuyerOrderDetailAchievement(Long orderId, Date startTime, Date endTime);

    /**
     * 查询分组统计各个组下的商品大类所产生的绩效
     *
     * @param groupNo
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> bossGroupProductTypeAchievement(String groupNo, Date startTime, Date endTime);

    /**
     * 查询分组统计各个组下的商品所产生的绩效
     *
     * @param groupNo
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> bossGroupProductAchievement(String groupNo, Date startTime, Date endTime);

    /**
     * 查询总商户
     *
     * @return
     */
    Integer countBuyerAll();

    /**
     * 查询总活跃商户
     *
     * @return
     */
    Integer countActiveBuyerAll();

    /**
     * 查询总待激活商户
     *
     * @return
     */
    Integer countInactiveBuyerAll();

    /**
     * 总月新增
     *
     * @param groupNo
     * @param date
     * @return
     */
    List<Map<String, Object>> monthIncrease(String groupNo, Date date);

    /**
     * 总日新增
     *
     * @param groupNo
     * @param date
     * @return
     */
    List<Map<String, Object>> dayIncrease(String groupNo, Date date);

    /**
     * 总新增
     *
     * @param groupNo
     * @return
     */
    List<Map<String, Object>> allIncrease(String groupNo);
}
