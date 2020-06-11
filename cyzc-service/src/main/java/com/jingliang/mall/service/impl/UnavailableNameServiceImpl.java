package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.UnavailableName;
import com.jingliang.mall.repository.BuyerRepository;
import lombok.extern.slf4j.Slf4j;
import com.jingliang.mall.service.UnavailableNameService;
import org.springframework.stereotype.Service;
import com.jingliang.mall.repository.UnavailableNameRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 不规范商铺名称合集ServiceImpl
 *
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-06-11 15:45:35
 */
@Service
@Slf4j
public class UnavailableNameServiceImpl implements UnavailableNameService {

    private final UnavailableNameRepository unavailableNameRepository;
    private final BuyerRepository buyerRepository;

    public UnavailableNameServiceImpl(UnavailableNameRepository unavailableNameRepository, BuyerRepository buyerRepository) {
        this.unavailableNameRepository = unavailableNameRepository;
        this.buyerRepository = buyerRepository;
    }

    /**
     * 更新不规范名称
     *
     * @return
     */
    @Transactional
    @Override
    public List<UnavailableName> update() {
        List<UnavailableName> all = unavailableNameRepository.findAll();
        List<String> names = unavailableNameRepository.findBuyerName();
        List<UnavailableName> result = new ArrayList<>();
        for (UnavailableName unavailableName : all) {
            //初始值设置标志为true，如果找到匹配的名称，标志为false，然后将新名称加入到表中
            boolean flg = true;
            for (String name : names) {
                if (Objects.equals(unavailableName.getName(), name)) {
                    flg = false;
                }
            }
            if (flg) {
                UnavailableName unavailableName1 = new UnavailableName();
                unavailableName1.setName(unavailableName.getName());
                unavailableName1.setCreateTime(new Date());
                unavailableNameRepository.save(unavailableName1);
                result.add(unavailableName1);
            }
        }
        return result;
    }

    /**
     * 查询全部不规范名称
     *
     * @return
     */
    @Override
    public List<UnavailableName> findAll() {
        return unavailableNameRepository.findAll();
    }

    /**
     * 查询用户名称是否规范
     *
     * @return
     */
    @Override
    public Boolean findNameCount(String name) {
        Integer buyerNameCount = buyerRepository.findBuyerNameCount(name);
        if (buyerNameCount > 2) {
            return false;
        }
        return true;
    }


}