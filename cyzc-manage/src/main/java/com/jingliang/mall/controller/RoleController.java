package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.Role;
import com.jingliang.mall.entity.UserRole;
import com.jingliang.mall.req.RoleReq;
import com.jingliang.mall.resp.RoleResp;
import com.jingliang.mall.service.RoleService;
import com.jingliang.mall.service.UserRoleService;
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
 * 角色表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@RestController
@Slf4j
@RequestMapping(value = "/back/role")
@Api(tags = "角色表")
public class RoleController {

    private final RoleService roleService;
    private final UserRoleService userRoleService;

    public RoleController(RoleService roleService, UserRoleService userRoleService) {
        this.roleService = roleService;
        this.userRoleService = userRoleService;
    }

    /**
     * 分页查询全部角色
     */
    @GetMapping("/page/all")
    @ApiOperation(value = "分页查询全部角色")
    public MallResult<MallPage<RoleResp>> pageAllProduct(RoleReq roleReq) {
        log.debug("请求参数：{}", roleReq);
        PageRequest pageRequest = PageRequest.of(roleReq.getPage(), roleReq.getPageSize());
        if (StringUtils.isNotBlank(roleReq.getClause())) {
            pageRequest = PageRequest.of(roleReq.getPage(), roleReq.getPageSize(), Sort.by(MallUtils.separateOrder(roleReq.getClause())));
        }
        Specification<Role> roleSpecification = (Specification<Role>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(roleReq.getRoleNameZh())) {
                predicateList.add(cb.like(root.get("roleNameZh"), "%" + roleReq.getRoleNameZh() + "%"));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            return predicateList.isEmpty() ? null : cb.and(predicateList.toArray(new Predicate[0]));
        };
        Page<Role> rolePage = roleService.findAll(roleSpecification, pageRequest);
        MallPage<RoleResp> roleRespMallPage = MallUtils.toMallPage(rolePage, RoleResp.class);
        if(Objects.nonNull(roleReq.getUserId())){
            List<UserRole> userRoles = userRoleService.findAllByUserId(roleReq.getUserId());
            List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            roleRespMallPage.getContent().forEach(roleResp -> {
                if (roleIds.contains(roleResp.getId())) {
                    roleResp.setIsHave(true);
                }
            });
        }
        log.debug("返回结果：{}", roleRespMallPage);
        return MallResult.buildQueryOk(roleRespMallPage);
    }
}