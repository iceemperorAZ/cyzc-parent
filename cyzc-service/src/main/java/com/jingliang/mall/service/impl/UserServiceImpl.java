package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.User;
import com.jingliang.mall.repository.UserRepository;
import com.jingliang.mall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 员工表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> findAllUserByPage(PageRequest pageRequest) {
        return userRepository.findAllByIsAvailable(pageRequest, true);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByUserNo(String userNo) {
        return userRepository.findFirstByUserNoAndIsAvailable(userNo, true);
    }

    @Override
    public User findByBuyerId(Long buyerId) {
        return userRepository.findByBuyerIdAndIsAvailable(buyerId, true);
    }

    @Override
    public User findUserByLoginName(String loginName) {
        return userRepository.findFirstByLoginNameAndIsAvailable(loginName, true);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAllByIsAvailable(true);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findAllByIdAndIsAvailable(id, true);
    }

    @Override
    public Page<User> findAll(Specification<User> userSpecification, PageRequest pageRequest) {
        return userRepository.findAll(userSpecification, pageRequest);
    }
}