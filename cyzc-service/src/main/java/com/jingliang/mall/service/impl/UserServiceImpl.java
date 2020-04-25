package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.ManagerSale;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.repository.ManagerSaleRepository;
import com.jingliang.mall.repository.UserRepository;
import com.jingliang.mall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
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
    private final ManagerSaleRepository managerSaleRepository;

    public UserServiceImpl(UserRepository userRepository, ManagerSaleRepository managerSaleRepository) {
        this.userRepository = userRepository;
        this.managerSaleRepository = managerSaleRepository;
    }

    @Override
    public Page<User> findAllUserByPage(PageRequest pageRequest) {
        return userRepository.findAllByIsAvailable(pageRequest, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User save(User user) {
        Long id = user.getId();
        user = userRepository.save(user);
        Date date = new Date();
        if (user.getManagerId() != null) {
            if (id != null) {
                List<ManagerSale> managerSales = managerSaleRepository.findAllByManagerIdAndSaleIdAndIsAvailableOrderByUntyingTime(user.getManagerId(), id, true);
                if (managerSales.size() > 0) {
                    User finalUser = user;
                    managerSales.forEach(managerSale -> {
                        managerSale.setIsAvailable(false);
                        managerSale.setUntyingTime(date);
                        managerSale.setUpdateTime(date);
                        managerSale.setUpdateUser(finalUser.getUpdateUserName());
                        managerSale.setUpdateUserId(finalUser.getUpdateUserId());
                    });
                    managerSaleRepository.saveAll(managerSales);
                }
            }
            ManagerSale managerSale = new ManagerSale();
            managerSale.setManagerId(user.getManagerId());
            managerSale.setSaleId(user.getId());
            managerSale.setCreateTime(date);
            managerSale.setIsAvailable(true);
            managerSale.setUpdateTime(date);
            managerSale.setUpdateUser(user.getUpdateUserName());
            managerSale.setUpdateUserId(user.getUpdateUserId());
            Calendar calendar = Calendar.getInstance();
            //将这个值设置的大一点
            calendar.set(Calendar.YEAR, 2300);
            managerSale.setUntyingTime(calendar.getTime());
            managerSaleRepository.save(managerSale);
        }
        return user;
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

    @Override
    public List<User> findByLevel(int level) {
        return userRepository.findAllByLevelAndIsAvailable(level, true);
    }

    @Override
    public Boolean modifyPassword(Long userId, String password) {
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        user.setPassword(password);
        userRepository.save(user);
        return true;
    }


}