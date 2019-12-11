package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Menu;
import com.jingliang.mall.entity.RoleMenu;
import com.jingliang.mall.repository.MenuRepository;
import com.jingliang.mall.repository.RoleMenuRepository;
import com.jingliang.mall.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final RoleMenuRepository roleMenuRepository;

    public MenuServiceImpl(MenuRepository menuRepository, RoleMenuRepository roleMenuRepository) {
        this.menuRepository = menuRepository;
        this.roleMenuRepository = roleMenuRepository;
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
    @Transactional(rollbackFor = Exception.class)
    public Menu save(Menu menu) {
        if (!menu.getIsAvailable()) {
            //删除资源的同时，解除与该资源绑定关系的角色
            List<RoleMenu> roleMenus = roleMenuRepository.findAllByMenuIdAndIsAvailable(menu.getId(), true);
            roleMenus.forEach(roleMenu -> {
                roleMenu.setUpdateTime(menu.getUpdateTime());
                roleMenu.setUpdateUserName(menu.getUpdateUserName());
                roleMenu.setUpdateUserId(menu.getUpdateUserId());
                roleMenu.setIsAvailable(false);
            });
            roleMenuRepository.saveAll(roleMenus);
        }
        return menuRepository.save(menu);
    }
}