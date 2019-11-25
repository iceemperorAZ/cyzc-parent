package com.jingliang.mall.service;

import com.jingliang.mall.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * 员工表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
public interface UserService {
    /**
     * 分页查询全部用户
     *
     * @param pageRequest 分页条件
     * @return 返回用户信息集合及分页信息
     */
    Page<User> findAllUserByPage(PageRequest pageRequest);

    /**
     * 保存用户
     *
     * @param user 用户信息
     * @return 返回保存后的用户信息
     */
    User save(User user);

    /**
     * 根据员工编号查询员工信息
     *
     * @param userNo 员工编号
     * @return 返回员工信息
     */
    User findByUserNo(String userNo);

    /**
     * 根据绑定会员Id查询员工信息
     *
     * @param buyerId 会员Id
     * @return 返回查询到的员工信息
     */
    User findByBuyerId(Long buyerId);

    /**
     * 根据登录名查询用户信息
     *
     * @param loginName 登录名
     * @return 用户信息
     */
    User findUserByLoginName(String loginName);

    /**
     * 查询所有员工
     *
     * @return 返回用户信息集合
     */
    List<User> findAll();

    /**
     * 根据主键Id查询员工信息
     *
     * @param id 主键Id
     * @return 返回查询到的员工信息
     */
    User findById(Long id);

    /**
     * 分页查询全部用户
     *
     * @param userSpecification 查询条件
     * @param pageRequest       分页条件
     * @return 返回差存到的用户列表
     */

    Page<User> findAll(Specification<User> userSpecification, PageRequest pageRequest);
}