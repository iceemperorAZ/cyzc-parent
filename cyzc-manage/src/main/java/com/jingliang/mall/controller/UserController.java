package com.jingliang.mall.controller;

import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.UserReq;
import com.jingliang.mall.resp.BuyerResp;
import com.jingliang.mall.resp.UserResp;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.BuyerService;
import com.jingliang.mall.service.UserService;
import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiOperation;
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
import com.citrsw.annatation.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
@Api(description = "员工")
@RestController("backUserController")
@Slf4j
@RequestMapping("/back/user")
public class UserController {
    @Value("${login.fail.count.prefix}")
    private String loginFailCountPrefix;
    @Value("${login.limit.prefix}")
    private String loginLimitPrefix;
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
    @ApiOperation(description = "登录")
    @PostMapping("login")
    public Result<UserResp> login() {
        return Result.buildOk();
    }

    /**
     * 修改用户密码
     */
    @ApiOperation(description = "修改用户密码")
    @PostMapping("/modify/password")
    public Result<UserResp> changePassword(@RequestBody UserReq userReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", userReq);
        String oldPassword = userReq.getOldPassword();
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(userReq.getPassword())) {
            log.debug("返回结果：{}", Msg.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findUserByLoginName(user.getLoginName());
        //判断用户密码是否匹配
        if (!passwordEncoder.matches(userReq.getOldPassword(), user.getPassword())) {
            return Result.build(Msg.FAIL, Msg.TEXT_OLD_PASSWORD_FAIL);
        }
        if (!MallUtils.checkPasswordSecurity(userReq.getPassword())) {
            return Result.build(Msg.FAIL, Msg.TEXT_MODIFY_PASSWORD_UNSAFE_FAIL);
        }
        //设置
        userReq.setId(user.getId());
        userReq.setPassword(passwordEncoder.encode(userReq.getPassword()));
        MallUtils.addDateAndUser(userReq, user);
        userReq.setIsInitPassword(false);
        UserResp userResp = BeanMapper.map(userService.save(BeanMapper.map(userReq, User.class)), UserResp.class);
        //清除redis中的token
        redisService.remove(tokenUserPrefix + user.getId());
        //清除security的登录信息
        SecurityContextHolder.getContext().setAuthentication(null);
        log.debug("返回结果：{}", userResp);
        return Result.buildUpdateOk(userResp);
    }

    /**
     * 重置其他用户密码
     */
    @ApiOperation(description = "重置其他用户密码")
    @PostMapping("/modify/other/password")
    public Result<UserResp> changeOtherPassword(@RequestBody UserReq userReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", userReq);
        if (Objects.isNull(userReq.getId())) {
            log.debug("返回结果：{}", Msg.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = (User) session.getAttribute(sessionUser);
        User otherUser = userService.findById(userReq.getId());
        //设置
        userReq.setId(otherUser.getId());
        //密码重置为登录名
        otherUser.setPassword(passwordEncoder.encode(otherUser.getLoginName()));
        otherUser.setUpdateTime(new Date());
        otherUser.setUpdateUserId(user.getId());
        otherUser.setUpdateUserName(user.getUserName());
        otherUser.setIsInitPassword(true);
        UserResp userResp = BeanMapper.map(userService.save(otherUser), UserResp.class);
        //清除redis中的token
        redisService.remove(tokenUserPrefix + otherUser.getId());
        //清除redis中的登录次数
        redisService.deleteByPre(loginFailCountPrefix + otherUser.getLoginName() + "-");
        redisService.deleteByPre(loginLimitPrefix + otherUser.getLoginName() + "-");
        log.debug("返回结果：{}", userResp);
        return Result.buildUpdateOk(userResp);
    }

    /**
     * 登出
     */
    @ApiOperation(description = "登出")
    @PostMapping("/logout")
    public Result<Boolean> logout() {
        return Result.buildOk(true);
    }

    /**
     * 新增用户
     */
    @PostMapping("/save")
    @ApiOperation(description = "新增用户")
    public Result<UserResp> save(@RequestBody UserReq userReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", userReq);
        if (Objects.nonNull(userReq.getBuyerId())) {
            Buyer buyer = buyerService.findById(userReq.getBuyerId());
            if (Objects.isNull(buyer)) {
                return Result.build(Msg.FAIL, Msg.TEXT_BUYER_FAIL);
            }
            //查询是否传过来的销售是否已经绑定员工
            User user = userService.findByBuyerId(userReq.getBuyerId());
            if (Objects.nonNull(user) && !Objects.equals(user.getId(), userReq.getId())) {
                return Result.build(Msg.FAIL, Msg.TEXT_BUYER_REPEAT_FAIL);
            }
            if (Objects.isNull(userReq.getLevel())) {
                userReq.setLevel(100);
            }
        }
        User user = (User) session.getAttribute(sessionUser);
        if (StringUtils.isNotBlank(userReq.getPhone()) && !MallUtils.phoneCheck(userReq.getPhone())) {
            //手机号格式不正确
            return Result.build(Msg.FAIL, Msg.TEXT_PHONE_FAIL);
        }
        MallUtils.addDateAndUser(userReq, user);
        //未设置提成则默认提成为0
        if (Objects.isNull(userReq.getRatio())) {
            userReq.setRatio(0);
        }
        if (userReq.getId() == null) {
            //加密密码
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userReq.setPassword(passwordEncoder.encode(userReq.getLoginName()));
            userReq.setIsInitPassword(true);
            userReq.setGroupNo("-1");
        }
        UserResp userResp = BeanMapper.map(userService.save(BeanMapper.map(userReq, User.class)), UserResp.class);
        log.debug("返回结果：{}", userResp);
        return Result.buildSaveOk(userResp);
    }

    /**
     * 根据商户Id查询绑定销售信息
     */
    @GetMapping("/findUserByBuyerId")
    @ApiOperation(description = "根据商户Id查询绑定销售信息")
    public Result<BuyerResp> findUserByBuyerId(Long buyerId) {
        Buyer buyer = buyerService.findById(buyerId);
        if (Objects.isNull(buyer) || Objects.isNull(buyer.getSaleUserId())) {
            return Result.build(Msg.FAIL, Msg.TEXT_BUYER_FAIL);
        }
        BuyerResp buyerResp = BeanMapper.map(buyer, BuyerResp.class);
        User user = userService.findById(buyer.getSaleUserId());
        buyerResp.setUser(BeanMapper.map(user, UserResp.class));
        log.debug("返回结果：{}", buyerResp);
        return Result.buildQueryOk(buyerResp);
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/page/all")
    @ApiOperation(description = "分页查询所有用户")
    public Result<MallPage<UserResp>> pageAllUser(UserReq userReq) {
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
        return Result.buildQueryOk(userRespPage);
    }

    /**
     * 根据分组编号查询用户
     */
    @GetMapping("/all/groupNo")
    @ApiOperation(description = "根据分组编号查询用户")
    public Result<List<UserResp>> allGroupNo(String groupNo) {
        return Result.buildQueryOk(BeanMapper.mapList(userService.likeAllByGroupNo(groupNo.replaceAll("0*$", "")), UserResp.class));
    }

    /**
     * 查询所有未分配的用户
     */
    @GetMapping("/all/ungrouped")
    @ApiOperation(description = "查询所有未分配的用户")
    public Result<List<UserResp>> allUngrouped() {
        return Result.buildQueryOk(BeanMapper.mapList(userService.allUngrouped(), UserResp.class));
    }

    /**
     * 移动/分配用户到组
     */
    @PostMapping("/distribution")
    @ApiOperation(description = "移动/分配用户到组")
    public Result<Boolean> distribution(@RequestBody Map<String, Object> map) {
        String groupNo = (String) map.get("groupNo");
        List<Long> userIds = ((List<?>) map.get("userIds")).stream().map(p -> Long.valueOf(p.toString())).collect(Collectors.toList());
        userService.distribution(groupNo, userIds);
        return Result.build(Msg.OK, "操作成功", true);
    }

    /**
     * 移除用户到未分配
     */
    @PostMapping("/remove/ungrouped")
    @ApiOperation(description = "移除用户到未分配")
    public Result<Boolean> removeToUngrouped(@RequestBody Map<String, Object> map) {
        List<Long> userIds = ((List<?>) map.get("userIds")).stream().map(p -> Long.valueOf(p.toString())).collect(Collectors.toList());
        userService.removeToUngrouped(userIds);
        return Result.build(Msg.OK, "已成功从该分组移除", true);
    }

    /**
     * 根据id逻辑删除用户
     */
    @PostMapping("/delete")
    @ApiOperation(description = "根据id逻辑删除用户")
    public Result<?> deleteUser(@RequestBody UserReq userReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", userReq);
        if (userReq.getId() <= 0 || userReq.getId() == null) {
            return Result.build(Msg.FAIL, "该用户可能不存在，请联系管理员核实");
        }
        User oldUser = BeanMapper.map(userReq, User.class);
        User user = (User) session.getAttribute(sessionUser);
        //根据id查询该用户名下的商户
        List<Buyer> buyers = buyerService.findAllBySaleUserId(userReq.getId());
        //如果名下没有商户，则逻辑删除
        if (Objects.isNull(buyers) || buyers.size() <= 0) {
            oldUser.setIsAvailable(false);
            oldUser.setUpdateTime(new Date());
            oldUser.setUpdateUserId(user.getId());
            oldUser.setUpdateUserName(user.getUserName());
            userService.save(oldUser);
            UserResp userResp = BeanMapper.map(oldUser, UserResp.class);
            //在删除用户的同时，同时拉黑用户绑定的小程序
            User u = userService.findById(user.getId());
            Buyer buyer = buyerService.findById(u.getBuyerId());
            buyer.setIsSealUp(true);
            buyerService.save(buyer);
            return Result.buildDeleteOk(userResp);
        }
        //名下有商户，返回失败结果和其名下所有商户信息
        return Result.build(Msg.FAIL, "该用户名下有商户，无法删除", buyers);
    }

    /**
     * 查询当前区域经理下的所有销售
     */
    @GetMapping("/all/region/groupNo")
    @ApiOperation(description = "根据分组编号查询用户")
    public Result<List<UserResp>> allRegionGroupNo(@ApiIgnore HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        if (user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "权限不足");
        }
        return Result.buildQueryOk(BeanMapper.mapList(userService.likeAllByGroupNo(user.getGroupNo().replaceAll("0*$", "")), UserResp.class));
    }

    /**
     * 根据用户id查询名下所有可用的商户
     *
     * @param userReq
     * @param session
     * @return
     */
    @ApiOperation(description = "根据用户id查询名下所有可用的商户")
    @GetMapping("/findAllBuyer")
    public Result<List<BuyerResp>> findAllByUserId(UserReq userReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", userReq);
        List<Buyer> buyerList = buyerService.findAllBySaleUserId(userReq.getId());
        List<BuyerResp> res = new ArrayList<>();
        for (Buyer buyer : buyerList) {
            res.add(BeanMapper.map(buyer, BuyerResp.class));
        }
        return Result.buildQueryOk(res);
    }

    /**
     * 批量操作商户重新绑定销售
     *
     * @param session
     * @return
     */
    @ApiOperation(description = "批量操作商户重新绑定销售")
    @PostMapping("/update/allSaleUser")
    public Result<Boolean> updateSaleUserToAll(@RequestBody Map<String, Object> map, @ApiIgnore HttpSession session) {
        //TODO 订单功能新增了销售帮客户下单，这时候，如果订单未完成，销售歇逼了，这个订单就歇了，这部分需要修改，代订，提出时间：time：2020/6/2
        log.debug("请求参数：{}", map);
        //获取操作者
        User user = (User) session.getAttribute(sessionUser);
        //判断操作者是否有修改权限
        if (user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "你没有修改权限");
        }
        //获取要更改的用户
        User newUser = userService.findById(Long.valueOf((String) map.get("userId")));
        //获取要操作的商户id集合
        List<Long> buyerIds = ((List<?>) map.get("buyerIds")).stream().map(p -> Long.valueOf(p.toString())).collect(Collectors.toList());
        for (Long buyerId : buyerIds) {
            //获取要操作的商户`
            Buyer buyer = buyerService.findById(buyerId);
            //修改商户绑定的销售
            buyer.setSaleUserId(newUser.getId());
            buyer.setUpdateTime(new Date());
            buyerService.save(buyer);
        }
        newUser.setUpdateUserName(user.getUserName());
        newUser.setUpdateUserId(user.getId());
        newUser.setUpdateTime(new Date());
        userService.save(newUser);
        return Result.buildUpdateOk(true);
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/list/all")
    @ApiOperation(description = "查询所有用户")
    public Result<List<UserResp>> listAllUser(UserReq userReq) {
        log.debug("请求参数：{}", userReq);
        Specification<User> userSpecification = (Specification<User>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(userReq.getSearchName())) {
                predicateList.add(cb.or(cb.like(root.get("userName"), "%" + userReq.getSearchName() + "%"),
                        cb.like(root.get("userNo"), "%" + userReq.getSearchName() + "%"),
                        cb.like(root.get("loginName"), "%" + userReq.getSearchName() + "%")));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.asc(root.get("createTime")));
            return query.getRestriction();
        };
        List<UserResp> userRespList = new ArrayList<>();
        for (User user : userService.findAll(userSpecification)) {
            userRespList.add(BeanMapper.map(user, UserResp.class));
        }
        log.debug("返回结果：{}", userRespList);
        return Result.buildQueryOk(userRespList);
    }
}
