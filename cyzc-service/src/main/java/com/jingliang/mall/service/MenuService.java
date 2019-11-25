package com.jingliang.mall.service;

import com.jingliang.mall.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * 资源表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
public interface MenuService {

    /**
     * 查询全部资源
     *
     * @return 返回查询到的全部资源
     */
    List<Menu> findAll();

    /**
     * 根据资源url查询角色信息
     *
     * @param url 资源url
     * @return 返回角色信息集合
     */
    List<String> findAllRoleNameByUrl(String url);

    /**
     * 分页查询全部资源
     *
     * @param menuSpecification 查询条件
     * @param pageRequest       分页条件
     * @return 返回查询到的资源集合
     */
    Page<Menu> findAll(Specification<Menu> menuSpecification, PageRequest pageRequest);
}