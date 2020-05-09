package com.jingliang.mall.repository;

import com.jingliang.mall.entity.User;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 员工表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
public interface UserRepository extends BaseRepository<User, Long> {
    /**
     * 根据员工编号查询可用用户信息
     *
     * @param userNo      员工编号
     * @param isAvailable 是否可用
     * @return 返回查询到的用户信息z
     */
    public User findFirstByUserNoAndIsAvailable(String userNo, Boolean isAvailable);

    /**
     * 根据会员Id查询员工信息
     *
     * @param buyerId     会员Id
     * @param isAvailable 是否可用
     * @return 员工Id
     */
    User findByBuyerIdAndIsAvailable(Long buyerId, Boolean isAvailable);

    /**
     * 根据用户名和密码查询可用用户信息
     *
     * @param loginName   登录名
     * @param isAvailable 是否可用
     * @return 返回查询到的用户信息
     */
    public User findFirstByLoginNameAndIsAvailable(String loginName, Boolean isAvailable);

    /**
     * 根据等级查询
     *
     * @param level
     * @param isAvailable
     * @return
     */
    List<User> findAllByLevelAndIsAvailable(Integer level, Boolean isAvailable);

    /**
     * 根据组编号分页查询用户
     *
     * @param groupNo 组编号
     * @return
     */
    public Page<User> findAllByGroupNoAndIsAvailable(String groupNo, Pageable pageable, Boolean isAvailable);

    /**
     * 根据用户id
     *
     * @param id          用户id
     * @param isAvailable 是否可用
     * @return
     */
    public User findFirstByIdAndIsAvailable(Long id, Boolean isAvailable);

    /**
     * 根据分组编号查询
     *
     * @param b
     * @param groupNo
     * @return
     */
    List<User> findAllByIsAvailableAndGroupNoLikeOrderByGroupNoAsc(boolean isAvailable, String groupNo);

    /**
     * 查询所有未分组的用户
     *
     * @param b
     * @param groupNo
     * @return
     */
    List<User> findAllByIsAvailableAndGroupNo(boolean isAvailable, String groupNo);

    /**
     * 分配用户到组
     *
     * @param groupNo
     * @param userIds
     */
    @Modifying
    @Query(value = "UPDATE User u SET u.groupNo=:groupNo WHERE u.id IN(:userIds)")
    void distribution(String groupNo, List<Long> userIds);

    /**
     * 移除用户到未分配
     *
     * @param userIds
     */
    @Modifying
    @Query(value = "UPDATE User u SET u.groupNo='-1' WHERE u.id IN(:userIds)")
    void removeToUngrouped(List<Long> userIds);

    /**
     * 统计组下的成员
     *
     * @param groupNo
     * @param isAvailable
     * @return
     */
    Integer countByGroupNoAndIsAvailable(String groupNo, boolean isAvailable);
}