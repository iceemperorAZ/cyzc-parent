package com.jingliang.mall.repository;

import com.jingliang.mall.entity.User;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 员工表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
public interface UserRepository extends BaseRepository<User, Long>,PagingAndSortingRepository<User, Long> {
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
     * @param id 用户id
     * @param isAvailable 是否可用
     * @return
     */
    public User findFirstByIdAndIsAvailable(Long id,Boolean isAvailable);
}