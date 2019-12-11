package com.jingliang.mall.controller;

import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.UserReq;
import com.jingliang.mall.resp.UserResp;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.BuyerService;
import com.jingliang.mall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 员工表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
@Api(tags = "员工")
@RestController("backUserController")
@Slf4j
@RequestMapping("/back/user")
public class UserController {
    @Value("${token.user.redis.prefix}")
    private String tokenUserPrefix;
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final RedisService redisService;
    private final UserService userService;
    private final BuyerService buyerService;

    public UserController(RedisService redisService, UserService userService, BuyerService buyerService) {
        this.redisService = redisService;
        this.userService = userService;
        this.buyerService = buyerService;
    }

    /**
     * 登录
     */
    @ApiOperation(value = "登录")
    @PostMapping("login")
    public MallResult<UserResp> login() {
        return MallResult.buildOk();
    }

    /**
     * 修改用户密码
     */
    @ApiOperation(value = "修改用户密码")
    @PostMapping("/modify/password")
    public MallResult<UserResp> changePassword(@RequestBody UserReq userReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", userReq);
        String oldPassword = userReq.getOldPassword();
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(userReq.getPassword())) {
            log.debug("返回结果：{}", MallConstant.TEXT_PARAM_FAIL);
            return MallResult.buildParamFail();
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findUserByLoginName(user.getLoginName());
        //判断用户密码是否匹配
        if (!passwordEncoder.matches(userReq.getOldPassword(), user.getPassword())) {
            return MallResult.build(MallConstant.FAIL, MallConstant.TEXT_OLD_PASSWORD_FAIL);
        }
        if (!MallUtils.checkPasswordSecurity(userReq.getPassword())) {
            return MallResult.build(MallConstant.FAIL, MallConstant.TEXT_MODIFY_PASSWORD_UNSAFE_FAIL);
        }
        //设置
        userReq.setId(user.getId());
        userReq.setPassword(passwordEncoder.encode(userReq.getPassword()));
        MallUtils.addDateAndUser(userReq, user);
        userReq.setIsInitPassword(false);
        UserResp userResp = MallBeanMapper.map(userService.save(MallBeanMapper.map(userReq, User.class)), UserResp.class);
        //清除redis中的token
        redisService.remove(tokenUserPrefix + user.getId());
        //清除security的登录信息
        SecurityContextHolder.getContext().setAuthentication(null);
        log.debug("返回结果：{}", userResp);
        return MallResult.buildUpdateOk(userResp);
    }

    /**
     * 重置其他用户密码
     */
    @ApiOperation(value = "重置其他用户密码")
    @PostMapping("/modify/other/password")
    public MallResult<UserResp> changeOtherPassword(@RequestBody UserReq userReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", userReq);
        if (Objects.isNull(userReq.getId())) {
            log.debug("返回结果：{}", MallConstant.TEXT_PARAM_FAIL);
            return MallResult.buildParamFail();
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = (User) session.getAttribute(sessionUser);
        User otherUser = userService.findById(userReq.getId());
        //设置
        userReq.setId(otherUser.getId());
        //密码重置为登录名
        userReq.setPassword(passwordEncoder.encode(otherUser.getLoginName()));
        MallUtils.addDateAndUser(userReq, user);
        userReq.setIsInitPassword(true);
        UserResp userResp = MallBeanMapper.map(userService.save(MallBeanMapper.map(userReq, User.class)), UserResp.class);
        //清除redis中的token
        redisService.remove(tokenUserPrefix + otherUser.getId());
        log.debug("返回结果：{}", userResp);
        return MallResult.buildUpdateOk(userResp);
    }

    /**
     * 登出
     */
    @ApiOperation(value = "登出")
    @PostMapping("/logout")
    public MallResult<Boolean> logout() {
        return MallResult.buildOk(true);
    }

    /**
     * 新增用户
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增用户")
    public MallResult<UserResp> save(@RequestBody UserReq userReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", userReq);
        if (Objects.nonNull(userReq.getBuyerId())) {
            Buyer buyer = buyerService.findById(userReq.getBuyerId());
            if (Objects.isNull(buyer)) {
                return MallResult.build(MallConstant.FAIL, MallConstant.TEXT_BUYER_FAIL);
            }
            //查询是否传过来的销售是否已经绑定员工
            User user = userService.findByBuyerId(userReq.getBuyerId());
            if (Objects.isNull(userReq.getId())) {
                return MallResult.build(MallConstant.FAIL, MallConstant.TEXT_BUYER_REPEAT_FAIL);
            }
            if (Objects.nonNull(user) && !Objects.equals(user.getId(), userReq.getId())) {
                return MallResult.build(MallConstant.FAIL, MallConstant.TEXT_BUYER_REPEAT_FAIL);
            }
            if (Objects.isNull(userReq.getLevel())) {
                userReq.setLevel(100);
            }
        }
        User user = (User) session.getAttribute(sessionUser);
        if (StringUtils.isNotBlank(userReq.getPhone()) && !MallUtils.phoneCheck(userReq.getPhone())) {
            //手机号格式不正确
            return MallResult.build(MallConstant.FAIL, MallConstant.TEXT_PHONE_FAIL);
        }
        MallUtils.addDateAndUser(userReq, user);
        //未设置提成则默认提成为0
        if (Objects.isNull(userReq.getRatio())) {
            userReq.setRatio(0);
        }
        //加密密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userReq.setPassword(passwordEncoder.encode(userReq.getLoginName()));
        userReq.setIsInitPassword(true);
        UserResp userResp = MallBeanMapper.map(userService.save(MallBeanMapper.map(userReq, User.class)), UserResp.class);
        log.debug("返回结果：{}", userResp);
        return MallResult.buildSaveOk(userResp);
    }

    /**
     * 根据商户Id查询绑定销售信息
     */
    @GetMapping("/findUserByBuyerId")
    @ApiOperation(value = "根据商户Id查询绑定销售信息")
    public MallResult<UserResp> findUserByBuyerId(Long buyerId) {
        Buyer buyer = buyerService.findById(buyerId);
        if (Objects.isNull(buyer) || Objects.isNull(buyer.getSaleUserId())) {
            return MallResult.build(MallConstant.FAIL, MallConstant.TEXT_BUYER_FAIL);
        }
        UserResp userResp = MallBeanMapper.map(userService.findById(buyer.getSaleUserId()), UserResp.class);
        log.debug("返回结果：{}", userResp);
        return MallResult.buildQueryOk(userResp);
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/page/all")
    @ApiOperation(value = "分页查询所有用户")
    public MallResult<MallPage<UserResp>> pageAllUser(UserReq userReq) {
        log.debug("请求参数：{}", userReq);
        PageRequest pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        if (StringUtils.isNotBlank(userReq.getClause())) {
            pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize(), Sort.by(MallUtils.separateOrder(userReq.getClause())));
        }
        Specification<User> userSpecification = (Specification<User>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(userReq.getUserNo())) {
                predicateList.add(cb.like(root.get("userNo"), "%" + userReq.getUserNo() + "%"));
            }
            if (StringUtils.isNotBlank(userReq.getUserName())) {
                predicateList.add(cb.like(root.get("userName"), "%" + userReq.getUserName() + "%"));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.asc(root.get("createTime")));
            return query.getRestriction();
        };
        Page<User> userPage = userService.findAll(userSpecification, pageRequest);
        MallPage<UserResp> userRespPage = MallUtils.toMallPage(userPage, UserResp.class);
        log.debug("返回结果：{}", userRespPage);
        return MallResult.buildQueryOk(userRespPage);
    }
}