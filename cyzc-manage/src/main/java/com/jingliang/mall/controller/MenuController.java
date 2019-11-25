package com.jingliang.mall.controller;

import com.jingliang.mall.entity.Menu;
import com.jingliang.mall.entity.RoleMenu;
import com.jingliang.mall.req.MenuReq;
import com.jingliang.mall.service.MenuService;
import com.jingliang.mall.service.RoleMenuService;
import com.jingliang.mall.resp.MenuResp;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.MallResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 资源表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@RestController
@RequestMapping(value = "/back/menu")
@Api(tags = "资源表")
@Slf4j
public class MenuController {

    private final MenuService menuService;
    private final RoleMenuService roleMenuService;

    public MenuController(MenuService menuService, RoleMenuService roleMenuService) {
        this.menuService = menuService;
        this.roleMenuService = roleMenuService;
    }

    /**
     * 分页查询全部资源
     */
    @GetMapping("/page/all")
    @ApiOperation(value = "分页查询全部资源")
    public MallResult<MallPage<MenuResp>> pageAllProduct(MenuReq menuReq) {
        log.debug("请求参数：{}", menuReq);
        PageRequest pageRequest = PageRequest.of(menuReq.getPage(), menuReq.getPageSize());
        if (StringUtils.isNotBlank(menuReq.getClause())) {
            pageRequest = PageRequest.of(menuReq.getPage(), menuReq.getPageSize(), Sort.by(MallUtils.separateOrder(menuReq.getClause())));
        }
        Specification<Menu> menuSpecification = (Specification<Menu>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(menuReq.getMenuName())) {
                predicateList.add(cb.like(root.get("menuName"), "%" + menuReq.getMenuName() + "%"));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            return predicateList.isEmpty() ? null : cb.and(predicateList.toArray(new Predicate[0]));
        };
        Page<Menu> menuPage = menuService.findAll(menuSpecification, pageRequest);
        MallPage<MenuResp> menuRespMallPage = MallUtils.toMallPage(menuPage, MenuResp.class);
        Long roleId = menuReq.getRoleId();
        if (Objects.nonNull(roleId)) {
            List<RoleMenu> roleMenus = roleMenuService.findAllByRoleId(roleId);
            List<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
            menuRespMallPage.setContent(menuRespMallPage.getContent().stream().filter(menuResp -> {
                if (menuIds.contains(menuResp.getId())) {
                    menuResp.setIsHave(true);
                }
                return true;
            }).collect(Collectors.toList()));
        }
        log.debug("返回结果：{}", menuRespMallPage);
        return MallResult.buildQueryOk(menuRespMallPage);
    }
}