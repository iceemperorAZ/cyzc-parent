package com.jingliang.mall.service.impl;

import com.jingliang.mall.repository.BuyerRepository;
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
    private final BuyerRepository buyerRepository;

    public BuyerManageServiceImpl(GroupRepository groupRepository, BuyerRepository buyerRepository) {
        this.groupRepository = groupRepository;
        this.buyerRepository = buyerRepository;
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

    @Override
    public List<Map<String, Object>> yearByDateAndGroupNoAchievement(Date startTime, Date endTime, String groupNo) {
        return groupRepository.yearByDateAndGroupNoAchievement(startTime, endTime, groupNo);
    }

    @Override
    public List<Map<String, Object>> monthByDateAndGroupNoAchievement(Date startTime, Date endTime, String groupNo) {
        return groupRepository.monthByDateAndGroupNoAchievement(startTime, endTime, groupNo);
    }

    @Override
    public List<Map<String, Object>> daysByDateAndGroupNoAchievement(Date startTime, Date endTime, String groupNo) {
        return groupRepository.daysByDateAndGroupNoAchievement(startTime, endTime, groupNo);
    }

    /**
     * 销售新增商户统计
     *
     * @return
     */
    @Override
    public List<Map<String, String>> countsByUserId() {
        return buyerRepository.countsByUserId(new Date());
    }

    @Override
    public List<Map<String, Object>> topOfOrderCountsByUser(Date startTime, Date endTime, Integer topNum) {
        return buyerRepository.topOfOrderCountsByUser(startTime, endTime, topNum);
    }

    @Override
    public List<Map<String, Object>> yeartopOfProductCountsByOrder(Date startTime, Date endTime, Integer topNum) {
        return buyerRepository.yeartopOfProductCountsByOrder(startTime, endTime, topNum);
    }

    @Override
    public List<Map<String, Object>> monthtopOfProductCountsByOrder(Date startTime, Date endTime, Integer topNum) {
        return buyerRepository.monthtopOfProductCountsByOrder(startTime, endTime, topNum);
    }

    @Override
    public List<Map<String, Object>> searchAllBuyer() {
        return buyerRepository.searchAllBuyer();
    }

    @Override
    public List<Map<String, Object>> searchBuyerDontHaveSale() {
        return buyerRepository.searchBuyerDontHaveSale();
    }
    @Override
    public List<Map<String, Object>> searchBuyerHaveSale(){
        return buyerRepository.searchBuyerHaveSale();
    }

    @Override
    public List<Map<String, Object>> findBuyerAddressByUserId(Long userId) {
        return buyerRepository.findBuyerAddressByUserId(userId);
    }
}
