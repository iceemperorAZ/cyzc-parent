package com.jingliang.mall.service.impl;

import com.jingliang.mall.repository.GroupRepository;
import com.jingliang.mall.service.BuyerManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service
@Slf4j
public class BuyerManageServiceImpl implements BuyerManageService {

    private final GroupRepository groupRepository;

    public BuyerManageServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * 通过组编号查询商户总数
     *
     * @param startTime
     * @param endTime
     * @param groupNo
     * @return
     */
    @Override
    public List<Map<String, Object>> dateAndGroupNoAchievement(Date startTime, Date endTime, String groupNo) {
        return groupRepository.dateAndGroupNoAchievement(startTime, endTime, groupNo);
    }

    /**
     * 查询各组下的商户总数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<Map<String, Object>> dateAndParentGroupIdAchievement(Date startTime, Date endTime, Long parentGroupId) {
        return groupRepository.dateAndParentGroupIdAchievement(startTime, endTime, parentGroupId);
    }

    /**
     * 查询组内销售名下的商户数
     *
     * @param startTime
     * @param endTime
     * @param groupNo
     * @return
     */
    @Override
    public List<Map<String, Object>> userByGroupNoAchievement(Date startTime, Date endTime, String groupNo) {
        return groupRepository.userByGroupNoAchievement(startTime, endTime, groupNo);
    }

    /**
     * 根据父id查询子区在当前年的用户量
     *
     * @param startTime
     * @param endTime
     * @param parentGroupId
     * @return
     */
    @Transactional
    @Override
    public List<Map<String, Object>> yearByDateAndParentGroupIdAchievement(Date startTime, Date endTime, Long parentGroupId) {
        return groupRepository.yearByDateAndParentGroupIdAchievement(startTime, endTime, parentGroupId);
    }

    /**
     * 根据父id查询子区每月的用户量
     *
     * @param startTime
     * @param endTime
     * @param parentGroupId
     * @return
     */
    @Override
    public List<Map<String, Object>> monthByDateAndParentGroupIdAchievement(Date startTime, Date endTime, Long parentGroupId) {
        return groupRepository.monthByDateAndParentGroupIdAchievement(startTime, endTime, parentGroupId);
    }

    /**
     * 根据父id查询子区每天的用户量
     *
     * @param startTime
     * @param endTime
     * @param parentGroupId
     * @return
     */
    @Override
    public List<Map<String, Object>> daysByDateAndParentGroupIdAchievement(Date startTime, Date endTime, Long parentGroupId) {
        return groupRepository.daysByDateAndParentGroupIdAchievement(startTime, endTime, parentGroupId);
    }
}
