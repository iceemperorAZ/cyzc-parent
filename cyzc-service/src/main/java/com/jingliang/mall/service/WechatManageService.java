package com.jingliang.mall.service;

import com.jingliang.mall.bean.ConfluenceDetail;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.User;

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
     * 查询销售员在指定时间内的绩效总汇
     *
     * @param user      指定员工
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public ConfluenceDetail userPerformanceSummary(User user, Date startTime, Date endTime);

    /**
     * 查询商户在指定时间内的绩效
     *
     * @param buyer     商户
     * @param user      销售
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public ConfluenceDetail buyerPerformanceSummary(Buyer buyer, User user, Date startTime, Date endTime);

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
}
