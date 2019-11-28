package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Menu;
import com.jingliang.mall.repository.MenuRepository;
import com.jingliang.mall.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资源表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAllByIsAvailable(true);
    }

    @Override
    public List<String> findAllRoleNameByUrl(String url) {
        return menuRepository.findAllRoleNameByUrl(url);
    }

    @Override
    public Page<Menu> findAll(Specification<Menu> menuSpecification, PageRequest pageRequest) {
        return menuRepository.findAll(menuSpecification, pageRequest);
    }

    @Override
    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }
}