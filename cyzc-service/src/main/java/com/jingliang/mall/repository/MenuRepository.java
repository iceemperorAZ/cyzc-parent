package com.jingliang.mall.repository;

import com.jingliang.mall.entity.Menu;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 资源表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
public interface MenuRepository extends BaseRepository<Menu, Long> {

    @Modifying
    @Query("select r.roleName from Menu as m join RoleMenu as rm on m.id=rm.menuId join Role as r on r.id=rm.roleId where m.url=:url")
    List<String> findAllRoleNameByUrl(String url);

}