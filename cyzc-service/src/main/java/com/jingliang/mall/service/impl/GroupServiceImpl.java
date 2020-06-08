package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Group;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.repository.GroupRepository;
import com.jingliang.mall.repository.OrderRepository;
import com.jingliang.mall.repository.UserRepository;
import com.jingliang.mall.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组ServiceImpl
 *
 * @author Mengde Liu
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-24 11:18:08
 * @date 2020-04-23 10:14:57
 */
@Service
@Slf4j
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public GroupServiceImpl(GroupRepository groupRepository, OrderRepository orderRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    /**
     * 查询所有可用的组
     *
     * @return
     */
    @Override
    public List<Group> findGroupAll() {
        List<Group> list = groupRepository.findGroupsByIsAvailableOrderByCreateTimeAsc(true);
        System.out.println(list);
        return list;
    }

    @Override
    public Group findByGroupNameAndParentId(String groupName, Long parentGroupId) {
        return groupRepository.findFirstByGroupNameAndParentGroupIdAndIsAvailable(groupName, parentGroupId, true);
    }

    @Override
    public List<Group> likeSearch(String search) {
        Specification<Group> groupSpecification = (Specification<Group>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.like(root.get("groupName"), "%" + search + "%"));
            predicates.add(cb.like(root.get("groupNo"), search + "%"));
            query.where(cb.and(cb.equal(root.get("isAvailable"), true), cb.or(predicates.toArray(new Predicate[0]))));
            return query.getRestriction();
        };
        return groupRepository.findAll(groupSpecification);
    }

    @Override
    public Group findByGroupById(Long id) {
        return groupRepository.findAllByIdAndIsAvailable(id, true);
    }

    @Override
    public Group findByGroupNo(String groupNo) {
        return groupRepository.findFirstByGroupNoAndIsAvailable(groupNo, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mergeGroups(String groupNo, List<String> mergeGroupNos) {
        //1.查询被合并组、销售和它的的所有订单（包括子组）
        List<Order> mergeOrders = new ArrayList<>();
        List<Group> mergeGroups = new ArrayList<>();
        List<User> mergeUsers = new ArrayList<>();
        for (String mergeGroupNo : mergeGroupNos) {
            List<Order> orders = orderRepository.findAllByIsAvailableAndGroupNoLike(true, mergeGroupNo.replaceAll("0*$", "") + "%");
            List<Group> groups = groupRepository.findAllByGroupNoLikeAndIsAvailable(mergeGroupNo.replaceAll("0*$", "") + "%", true);
            List<User> users = userRepository.findAllByIsAvailableAndGroupNoLikeOrderByGroupNoAscLevelDesc(true, mergeGroupNo.replaceAll("0*$", "") + "%");
            mergeOrders.addAll(orders);
            mergeGroups.addAll(groups);
            mergeUsers.addAll(users);
        }
        //2.把查询到的订单统一合并到指定组
        mergeOrders = mergeOrders.stream().map(order -> order.setGroupNo(groupNo)).collect(Collectors.toList());
        orderRepository.saveAll(mergeOrders);
        //3.把查询到的销售统一合并到指定组
        mergeUsers = mergeUsers.stream().map(user -> user.setGroupNo(groupNo)).collect(Collectors.toList());
        userRepository.saveAll(mergeUsers);
        //4.删除被合并的组
        mergeGroups = mergeGroups.stream().map(group -> group.setIsAvailable(false)).collect(Collectors.toList());
        groupRepository.saveAll(mergeGroups);

    }

    @Override
    public Group getFatherGroup() {
        Group fatherGroup = groupRepository.findByParentGroupId(-1L);
        return groupRepository.findGroupByParentGroupIdAndIsAvailable(-1L, fatherGroup.getIsAvailable());
    }

    @Override
    public List<Group> getGroupWithFather(Long parentGroupId, Boolean isAvailable) {
        return groupRepository.findGroupsByParentGroupIdAndIsAvailableOrderByCreateTime(parentGroupId, true);
    }

    /**
     * 根据父组ID编号查询可用组信息
     *
     * @param group 组对象
     * @return 返回查询到的组信息z
     */
    @Override
    public Group save(Group group) {
        //查询父组，修改为非叶子节点
        if (group.getId() == null) {
            //只在新增的时候进行设置
            Group parentGroup = groupRepository.findAllByIdAndIsAvailable(group.getParentGroupId(), true);
            parentGroup.setChild(false);
            groupRepository.save(parentGroup);
        }
        return groupRepository.save(group);
    }

    /**
     * 根据父组ID编号查询可用组信息
     *
     * @param parentGroupId 父组ID
     * @return 返回查询到的组信息z
     */
    @Override
    public Group findFartherGroup(Long parentGroupId) {
        return groupRepository.findFirstGroupById(parentGroupId);
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }


}