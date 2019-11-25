package com.jingliang.mall.authority;

import com.jingliang.mall.entity.Role;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.entity.UserRole;
import com.jingliang.mall.service.UserRoleService;
import com.jingliang.mall.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * security 自定义登陆逻辑类
 * 用来做登陆认证，验证用户名与密码
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-01 15:13:01
 */
@Component("userDetailService")
public class MallUserDetailServiceImpl implements UserDetailsService {


    private final UserService userService;
    private final UserRoleService roleService;

    public MallUserDetailServiceImpl(UserService userService, UserRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        MallUserDetails userInfo = new MallUserDetails();
        //登录用户名
        userInfo.setUsername(loginName);
        User user = userService.findUserByLoginName(loginName);
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("用户[" + loginName + "不存在");
        }
        //从数据库获取密码
        userInfo.setPassword(user.getPassword());
        Set<SimpleGrantedAuthority> authoritiesSet = new HashSet<>();
        List<UserRole> userRoles = roleService.findAllByUserId(user.getId());
        List<String> roleNames = userRoles.stream().map(UserRole::getRole).collect(Collectors.toList()).stream().map(Role::getRoleName).collect(Collectors.toList());
        for (String roleName : roleNames) {
            //用户拥有的角色
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
            authoritiesSet.add(simpleGrantedAuthority);
        }
        userInfo.setAuthorities(authoritiesSet);
        return userInfo;
    }
}
