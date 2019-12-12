package com.jingliang.mall.service;

import com.jingliang.mall.bean.ConfluenceDetail;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.User;

import java.util.Date;

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
     */
    public ConfluenceDetail userPerformanceSummary(User user, Date startTime, Date endTime);

    /**
     * 查询商户在指定时间内的绩效
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    public ConfluenceDetail buyerPerformanceSummary(Buyer buyer, Date startTime, Date endTime);
}
