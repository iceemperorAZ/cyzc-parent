package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallBeanMapper;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.Menu;
import com.jingliang.mall.entity.RoleMenu;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.MenuReq;
import com.jingliang.mall.resp.MenuResp;
import com.jingliang.mall.service.MenuService;
import com.jingliang.mall.service.RoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
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
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final MenuService menuService;
    private final RoleMenuService roleMenuService;

    public MenuController(MenuService menuService, RoleMenuService roleMenuService) {
        this.menuService = menuService;
        this.roleMenuService = roleMenuService;
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存/更新资源")
    public MallResult<MenuResp> save(@RequestBody MenuReq menuReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", menuReq);
        if (StringUtils.isBlank(menuReq.getMenuName()) || StringUtils.isBlank(menuReq.getUrl())) {
            return MallResult.buildParamFail();
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(menuReq, user);
        MenuResp menuResp = MallBeanMapper.map(menuService.save(MallBeanMapper.map(menuReq, Menu.class)), MenuResp.class);
        log.debug("返回参数：{}", menuResp);
        return MallResult.buildSaveOk(menuResp);
    }
    @PostMapping("/delete")
    @ApiOperation(value = "删除资源")
    public MallResult<MenuResp> delete(@RequestBody MenuReq menuReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", menuReq);
        if (Objects.isNull(menuReq.getId())) {
            return MallResult.buildParamFail();
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(menuReq, user);
        menuReq.setIsAvailable(false);
        MenuResp menuResp = MallBeanMapper.map(menuService.save(MallBeanMapper.map(menuReq, Menu.class)), MenuResp.class);
        log.debug("返回参数：{}", menuResp);
        return MallResult.buildDeleteOk(menuResp);
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
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("updateTime")));
            return query.getRestriction();
        };
        Page<Menu> menuPage = menuService.findAll(menuSpecification, pageRequest);
        MallPage<MenuResp> menuRespMallPage = MallUtils.toMallPage(menuPage, MenuResp.class);
        Long roleId = menuReq.getRoleId();
        if (Objects.nonNull(roleId)) {
            List<RoleMenu> roleMenus = roleMenuService.findAllByRoleId(roleId);
            List<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
            menuRespMallPage.getContent().forEach(menuResp -> {
                if (menuIds.contains(menuResp.getId())) {
                    menuResp.setIsHave(true);
                }
            });
        }
        log.debug("返回结果：{}", menuRespMallPage);
        return MallResult.buildQueryOk(menuRespMallPage);
    }
}