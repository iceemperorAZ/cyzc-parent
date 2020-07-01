package com.jingliang.mall.controller;

import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiIgnore;
import com.citrsw.annatation.ApiOperation;
import com.citrsw.annatation.ApiParam;
import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.*;
import com.jingliang.mall.resp.BuyerResp;
import com.jingliang.mall.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 微信管理
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-11-01 10:40
 */
@Api(description = "微信管理")
@RestController
@RequestMapping("/wx")
@Slf4j
public class WechatManageController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final UserService userService;
    private final BuyerService buyerService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final WechatManageService wechatManageService;
    private final BuyerAddressService buyerAddressService;
    private final ManagerSaleService managerSaleService;
    private final BuyerSaleService buyerSaleService;
    private final GroupService groupService;


    public WechatManageController(UserService userService, BuyerService buyerService, OrderService orderService,
                                  OrderDetailService orderDetailService, WechatManageService wechatManageService, BuyerAddressService buyerAddressService, ManagerSaleService managerSaleService, BuyerSaleService buyerSaleService, GroupService groupService) {
        this.userService = userService;
        this.buyerService = buyerService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.wechatManageService = wechatManageService;
        this.buyerAddressService = buyerAddressService;
        this.managerSaleService = managerSaleService;
        this.buyerSaleService = buyerSaleService;
        this.groupService = groupService;
    }

    /**
     * 根据父分组分组统计组下业绩
     */
    @GetMapping("/boss/group/achievement")
    @ApiOperation(description = "根据父分组分组统计组下业绩")
    public Result<List<Map<String, Object>>> bossGroupAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                  String parentGroupNo,
                                                                  HttpSession session) {
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //2.如果未传父分组编号，则查询自己组的业绩
        if (StringUtils.isBlank(parentGroupNo)) {
            List<Map<String, Object>> achievements = wechatManageService.bossONeGroupAchievement(user.getGroupNo(), startTime, endTime);
            return Result.buildQueryOk(achievements);
        }
        //return Result.buildQueryOk(achievements);
        //3.如果传父分组编号，判断查看的是否是自己的子分组
        Group group = groupService.findByGroupNo(parentGroupNo);
        if (!parentGroupNo.startsWith(user.getGroupNo().replaceAll("0*$", ""))) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //3.分组统计子分组的绩效
        List<Map<String, Object>> achievements = wechatManageService.bossGroupAchievement(group.getId(), startTime, endTime);
        return Result.buildQueryOk(achievements);
    }

    /**
     * 分组统计分组及子分组下的销售（销售、区域经理、老板）业绩
     */
    @GetMapping("/boss/group/user/achievement")
    @ApiOperation(description = "分组统计分组及子分组下的销售（销售、区域经理、老板）业绩")
    public Result<List<Map<String, Object>>> bossGroupUserAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam(description = "开始时间") Date startTime,
                                                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam(description = "结束时间") Date endTime,
                                                                      @ApiParam(description = "分组名称") String groupNo, HttpSession session) {

        if (StringUtils.isBlank(groupNo)) {
            return Result.buildQueryOk(new ArrayList<>());
        }
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //2.判断查看的是否是自己的分组或自己的子分组
        Group group = groupService.findByGroupNo(groupNo);
        if (!groupNo.startsWith(user.getGroupNo().replaceAll("0*$", ""))) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //3.查询组下及子组下的销售
        List<Map<String, Object>> achievements = wechatManageService.bossGroupUserAchievement(group.getGroupNo().replaceAll("0*$", "") + "%", startTime, endTime);
        //4.处理不是本组直接管理的销售
        achievements = achievements.stream().map((Function<Map<String, Object>, Map<String, Object>>) HashMap::new).collect(Collectors.toList());

        achievements.forEach(map -> {
            if (groupNo.equals(map.get("groupNo"))) {
                map.put("direct", true);
            } else {
                map.put("direct", false);
            }
        });
        return Result.buildQueryOk(achievements);
    }

    /**
     * 统计指定销售下商户的所产生的总绩效
     */
    @GetMapping("/boss/user/achievement")
    @ApiOperation(description = "统计指定销售下商户的所产生的总绩效")
    public Result<Map<String, Object>> bossUserAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam(description = "开始时间") Date startTime,
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam(description = "结束时间") Date endTime,
                                                           @ApiParam(description = "销售Id") Long saleUserId, HttpSession session) {
        //当前操作人
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        //要查询的销售员，没传就查自己的
        User saleUser = user;
        if (saleUserId != null) {
            saleUser = userService.findById(saleUserId);
        } else {
            saleUserId = user.getId();
        }
        //1.判断是否查询的是自己的商户
        if (user.getId().equals(saleUserId)) {
            //1.1.查询销售自己所有商户产生的总绩效
            Map<String, Object> achievement = wechatManageService.bossSelfAchievement(saleUserId, startTime, endTime);
            achievement = new HashMap<>(achievement);
            achievement.put("userName", saleUser.getUserName());
            return Result.buildQueryOk(achievement);
        }
        //1.2.不是查询的是自己的商户，判断要查询的销售是否是自己分组下的人
        if (!saleUser.getGroupNo().startsWith(user.getGroupNo().replaceAll("0*$", ""))) {
            return Result.build(Msg.FAIL, "无查看此商户信息权限");
        }
        //1.3.查询指定销售在自己组下所有商户产生的总绩效
        Map<String, Object> achievement = wechatManageService.bossUserAchievement(user.getGroupNo().replaceAll("0*$", "") + "%", saleUserId, startTime, endTime);
        achievement = new HashMap<>(achievement);
        achievement.put("userName", saleUser.getUserName());
        return Result.buildQueryOk(achievement);
    }


    /**
     * 统计指定销售下商户的所产生的绩效
     */
    @GetMapping("/boss/user/buyer/achievement")
    @ApiOperation(description = "统计指定销售下商户的所产生的绩效")
    public Result<List<Map<String, Object>>> bossUserBuyerAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam(description = "开始时间") Date startTime,
                                                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam(description = "结束时间") Date endTime,
                                                                      @ApiParam(description = "销售Id") Long saleUserId, HttpSession session) {
        //当前操作人
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        //要查询的销售员，没传就查自己的
        User saleUser = user;
        if (saleUserId != null) {
            saleUser = userService.findById(saleUserId);
        } else {
            saleUserId = user.getId();
        }
        //1.判断是否查询的是自己的商户
        if (user.getId().equals(saleUserId)) {
            //1.1.查询销售自己所有商户产生的绩效
            List<Map<String, Object>> achievements = wechatManageService.bossSelfBuyerAchievement(saleUserId, startTime, endTime);
            return Result.buildQueryOk(achievements);
        }
        //1.2.不是查询的是自己的商户，判断要查询的销售是否是自己分组下的人
        if (!saleUser.getGroupNo().startsWith(user.getGroupNo().replaceAll("0*$", ""))) {
            return Result.build(Msg.FAIL, "无查看此商信息权限");
        }
        //1.3.查询指定销售在自己组下所有商户产生的绩效
        List<Map<String, Object>> achievements = wechatManageService.bossUserBuyerAchievement(user.getGroupNo().replaceAll("0*$", "") + "%", saleUserId, startTime, endTime);
        return Result.buildQueryOk(achievements);
    }


    /**
     * 统计指定商户下所有的订单产生的绩效
     */
    @GetMapping("/boss/buyer/order/achievement")
    @ApiOperation(description = "统计指定商户下所有的订单产生的绩效")
    public Result<List<Map<String, Object>>> bossBuyerOrderAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam(description = "开始时间") Date startTime,
                                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam(description = "结束时间") Date endTime,
                                                                       Long buyerId, HttpSession session) {
        //当前操作人
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        //要查询的销售员
        Buyer buyer = buyerService.findById(buyerId);
        Long saleUserId = buyer.getSaleUserId();
        if (saleUserId == null) {
            return Result.build(Msg.FAIL, "无查看此商户信息权限");
        }
        User saleUser = userService.findById(saleUserId);
        if (saleUser == null) {
            return Result.build(Msg.FAIL, "无查看此商户信息权限");
        }
        //1.判断是否查询的是自己的商户
        if (user.getId().equals(saleUserId)) {
            //1.1.查询销售自己的商户所有订单产生的绩效
            List<Map<String, Object>> achievements = wechatManageService.bossSelfBuyerOrderAchievement(saleUserId, buyer.getId(), startTime, endTime);
            return Result.buildQueryOk(achievements);
        }
        //1.2.不是查询自己的商户，判断要查询的销售是否是自己分组下的人
        if (!saleUser.getGroupNo().startsWith(user.getGroupNo().replaceAll("0*$", ""))) {
            return Result.build(Msg.FAIL, "无查看此商户信息权限");
        }
        //1.3.查询指定销售在自己组下的商户所有订单产生的绩效
        List<Map<String, Object>> achievements = wechatManageService.bossUserBuyerOrderAchievement(user.getGroupNo().replaceAll("0*$", "") + "%", buyer.getId(), startTime, endTime);
        return Result.buildQueryOk(achievements);
    }


    /**
     * 统计指定商户下的订单的详情产生的绩效
     */
    @GetMapping("/boss/buyer/order/detail/achievement")
    @ApiOperation(description = "统计指定商户下所有的订单产生的绩效")
    public Result<List<Map<String, Object>>> bossBuyerOrderDetailAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam(description = "开始时间") Date startTime,
                                                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam(description = "结束时间") Date endTime,
                                                                             @ApiParam(description = "订单Id") Long orderId, HttpSession session) {
        //当前操作人
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        //要查询的销售员
        Order order = orderService.findById(orderId);
        Long saleUserId = order.getSaleUserId();
        if (saleUserId == null) {
            return Result.build(Msg.FAIL, "无查看此商户订单详情的权限");
        }
        //1.判断是否查询的是自己的或自己组下的商户订单详情
        if ((user.getId().equals(saleUserId)) || order.getGroupNo().startsWith(user.getGroupNo().replaceAll("0*$", ""))) {
            //1.1.查询订单详情产生的绩效
            List<Map<String, Object>> achievements = wechatManageService.bossBuyerOrderDetailAchievement(orderId, startTime, endTime);
            return Result.buildQueryOk(achievements);
        }
        return Result.build(Msg.FAIL, "无查看此商户订单详情的权限");
    }

    /**
     * 查询分组统计各个组下的商品大类所产生的绩效
     */
    @GetMapping("/boss/group/product/type/achievement")
    @ApiOperation(description = "查询分组统计各个组下的商品大类所产生的绩效")
    public Result<List<Map<String, Object>>> bossGroupProductTypeAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam(description = "开始时间") Date startTime,
                                                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam(description = "结束时间") Date endTime,
                                                                             @ApiParam(description = "分组编号") String groupNo, HttpSession session) {
        if (StringUtils.isBlank(groupNo)) {
            return Result.buildQueryOk(new ArrayList<>());
        }
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //2.判断查看的是否是自己的分组或自己的子分组
        Group group = groupService.findByGroupNo(groupNo);
        if (!groupNo.startsWith(user.getGroupNo().replaceAll("0*$", ""))) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //3.查询组下及子组下的销售
        List<Map<String, Object>> achievements = wechatManageService.bossGroupProductTypeAchievement(group.getGroupNo().replaceAll("0*$", "") + "%", startTime, endTime);
        return Result.buildQueryOk(achievements);
    }

    /**
     * 查询分组统计各个组下的商品所产生的绩效
     */
    @GetMapping("/boss/group/product/achievement")
    @ApiOperation(description = "查询分组统计各个组下的商品所产生的绩效")
    public Result<List<Map<String, Object>>> bossGroupProductAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam(description = "开始时间") Date startTime,
                                                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam(description = "结束时间") Date endTime,
                                                                         @ApiParam(description = "分组编号") String groupNo, HttpSession session) {
        if (StringUtils.isBlank(groupNo)) {
            return Result.buildQueryOk(new ArrayList<>());
        }
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //2.判断查看的是否是自己的分组或自己的子分组
        Group group = groupService.findByGroupNo(groupNo);
        if (!groupNo.startsWith(user.getGroupNo().replaceAll("0*$", ""))) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //3.查询组下及子组下的销售
        List<Map<String, Object>> achievements = wechatManageService.bossGroupProductAchievement(group.getGroupNo().replaceAll("0*$", "") + "%", startTime, endTime);
        return Result.buildQueryOk(achievements);
    }

    /**
     * 统计商户新增
     *
     * @param session
     * @return
     */
    @GetMapping("/boss/group/buyer/count")
    public Result<Map<String, Object>> groupBuyerCount(HttpSession session) {
        //当前操作人
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        Map<String, Object> resultMap = new TreeMap<>();
        //判断是否是管理员
        Map<String, List<Map<String, Object>>> hashMap = new LinkedHashMap<>();
        if (user.getLevel() == 200) {
            List<Group> groups = groupService.findGroupAll();
            for (Group group : groups) {
                List<Map<String, Object>> list = new ArrayList<>();
                Map<String, Object> map = new HashMap<>();
                map.put("name", group.getGroupName());
                map.put("value", 0L);
                list.add(map);
                map = new HashMap<>();
                map.put("name", group.getGroupName());
                map.put("value", 0L);
                list.add(map);
                map = new HashMap<>();
                map.put("name", group.getGroupName());
                map.put("value", 0L);
                list.add(map);
                hashMap.put(group.getGroupName(), list);
            }
            //查询总商户
            Integer countBuyerAll = wechatManageService.countBuyerAll();
            resultMap.put("countBuyerAll", countBuyerAll);
            //查询总活跃商户
            Integer countActiveBuyerAll = wechatManageService.countActiveBuyerAll();
            resultMap.put("countActiveBuyerAll", countActiveBuyerAll);
            //查询总待激活商户
            Integer countInactiveBuyerAll = wechatManageService.countInactiveBuyerAll();
            resultMap.put("countInactiveBuyerAll", countInactiveBuyerAll);
            //查询总的月新增商户
            Integer totalMonthBuyerAll = wechatManageService.totalMonthBuyerAll(new Date());
            resultMap.put("totalMonthBuyerAll", totalMonthBuyerAll);
            //查询总的日新增商户
            Integer totalDayBuyerAll = wechatManageService.totalDayBuyerAll(new Date());
            resultMap.put("totalDayBuyerAll", totalDayBuyerAll);

            //各个区总新增
            List<Map<String, Object>> allIncrease = wechatManageService.allIncrease(user.getGroupNo().replaceAll("0*$", "") + "%");
            for (Map<String, Object> objectMap : allIncrease) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", objectMap.get("groupName"));
                map.put("value", ((BigInteger) objectMap.get("count")).longValue());
                if (hashMap.containsKey(objectMap.get("groupName"))) {
                    hashMap.get(objectMap.get("groupName")).set(0, map);
                }
            }
            //各个区总月新增
            List<Map<String, Object>> monthIncrease = wechatManageService.monthIncrease(user.getGroupNo().replaceAll("0*$", "") + "%", new Date());
            for (Map<String, Object> objectMap : monthIncrease) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", objectMap.get("groupName"));
                map.put("value", ((BigInteger) objectMap.get("count")).longValue());
                if (hashMap.containsKey(objectMap.get("groupName"))) {
                    hashMap.get(objectMap.get("groupName")).set(1, map);
                }
            }
            //各个区总日新增
            List<Map<String, Object>> dayIncrease = wechatManageService.dayIncrease(user.getGroupNo().replaceAll("0*$", "") + "%", new Date());
            for (Map<String, Object> objectMap : dayIncrease) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", objectMap.get("groupName"));
                map.put("value", ((BigInteger) objectMap.get("count")).longValue());
                if (hashMap.containsKey(objectMap.get("groupName"))) {
                    hashMap.get(objectMap.get("groupName")).set(2, map);
                }
            }
            resultMap.put("regionList", new ArrayList<>(hashMap.values()));
            return Result.build(Msg.OK, "", resultMap);
        } else if (user.getLevel() == 110) {
            Group group = groupService.findByGroupNo(user.getGroupNo());
            hashMap = new LinkedHashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("name", group.getGroupName());
            map.put("value", 0L);
            list.add(map);
            map = new HashMap<>();
            map.put("name", group.getGroupName());
            map.put("value", 0L);
            list.add(map);
            map = new HashMap<>();
            map.put("name", group.getGroupName());
            map.put("value", 0L);
            list.add(map);
            hashMap.put(group.getGroupName(), list);
            //总新增
            List<Map<String, Object>> allIncrease = wechatManageService.allIncrease(user.getGroupNo().replaceAll("0*$", "") + "%");
            if (allIncrease.size() > 1) {
                return Result.build(Msg.AUTHORITY_FAIL, "权限不足");
            }
            if (!allIncrease.isEmpty()) {
                map = new HashMap<>();
                map.put("name", allIncrease.get(0).get("groupName"));
                map.put("value", ((BigInteger) allIncrease.get(0).get("count")).longValue());
                if (hashMap.containsKey(allIncrease.get(0).get("groupName"))) {
                    hashMap.get(allIncrease.get(0).get("groupName")).set(0, map);
                }
            }
            //总月新增
            List<Map<String, Object>> monthIncrease = wechatManageService.monthIncrease(user.getGroupNo().replaceAll("0*$", "") + "%", new Date());
            if (!monthIncrease.isEmpty()) {
                map = new HashMap<>();
                map.put("name", monthIncrease.get(0).get("groupName"));
                map.put("value", ((BigInteger) monthIncrease.get(0).get("count")).longValue());
                if (hashMap.containsKey(monthIncrease.get(0).get("groupName"))) {
                    hashMap.get(monthIncrease.get(0).get("groupName")).set(1, map);
                }
            }
            //总日新增
            List<Map<String, Object>> dayIncrease = wechatManageService.dayIncrease(user.getGroupNo().replaceAll("0*$", "") + "%", new Date());
            if (!dayIncrease.isEmpty()) {
                map = new HashMap<>();
                map.put("name", (String) dayIncrease.get(0).get("groupName"));
                map.put("value", ((BigInteger) dayIncrease.get(0).get("count")).longValue());
                if (hashMap.containsKey(dayIncrease.get(0).get("groupName"))) {
                    hashMap.get(dayIncrease.get(0).get("groupName")).set(2, map);
                }
            }
            resultMap.put("regionList", new ArrayList<>(hashMap.values()));
            return Result.build(Msg.OK, "", resultMap);
        }
        return Result.build(Msg.AUTHORITY_FAIL, "权限不足");
    }

    /**
     * 查询销售信息
     */
    @GetMapping("/boss/group/users")
    public Result<List<Map<String, Object>>> groupUsers(HttpSession session) {
        //当前操作人
        User user = (User) session.getAttribute(sessionUser);
        List<Map<String, Object>> lists = new ArrayList<>();
        if (user.getLevel() == 200) {
            List<Group> groups = groupService.findGroupAll();
            for (Group group : groups) {
                List<User> userList = userService.likeAllByGroupNo(group.getGroupNo());
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("name", group.getGroupName());
                map.put("value", userList);
                lists.add(map);
            }
        } else if (user.getLevel() == 110) {
            Group group = groupService.findByGroupNo(user.getGroupNo());
            List<User> userList = userService.likeAllByGroupNo(user.getGroupNo());
            Map<String, Object> map = new LinkedHashMap<>();

            map.put("name", group.getGroupName());
            map.put("value", userList);
            lists.add(map);
        }
        return Result.build(Msg.OK, "", lists);
    }

    /**
     * 查询商户信息
     */
    @GetMapping("/boss/group/buyers")
    public Result<List<BuyerResp>> groupBuyers(Long userId, HttpSession session) {
        //当前操作人
        User user = (User) session.getAttribute(sessionUser);
        List<Buyer> buyers;
        if (user.getLevel() == 200 || user.getLevel() == 110) {
            //如果是管理员或区域经理则可以直接用传过来的用户Id查询商户
            buyers = buyerService.findAllBySaleUserId(userId);
        } else {
            //否则只能查询自己下面的商户
            buyers = buyerService.findAllBySaleUserId(user.getId());
        }
        return Result.build(Msg.OK, "", BeanMapper.mapList(buyers, BuyerResp.class));
    }

    /**
     * 根据商户信息查询收货地址信息
     */
    @GetMapping("/boss/buyer/addresses")
    public Result<List<BuyerAddress>> buyerAddresses(Long buyerId, HttpSession session) {
        //当前操作人
        User user = (User) session.getAttribute(sessionUser);
        if (user.getLevel() == 200 || user.getLevel() == 110 || user.getLevel() == 100) {
            //如果是管理员或区域经理则可以直接用传过来的用户Id查询商户
            List<BuyerAddress> buyerAddresses = buyerAddressService.findByBuyerId(buyerId);
            return Result.build(Msg.OK, "", buyerAddresses);
        } else {
            return Result.build(Msg.AUTHORITY_FAIL, "权限不足");
        }

    }

    /**
     * 根据手机号查询用户
     *
     * @param phone
     * @return
     */
    @GetMapping("/boss/phone/users")
    public Result<List<Map<String, Object>>> phoneUsers(String phone, @ApiIgnore HttpSession session) {
        log.debug("请求参数:{}", phone);
        List<Map<String, Object>> lists = new ArrayList<>();
        List<Group> groups = groupService.findGroupAll();
        for (Group group : groups) {
            List<Map<String, Object>> mapList = userService.findPhoneLike(phone);
            Map<String, Object> map1 = new LinkedHashMap<>();
            List<User> users = new ArrayList<>();
            for (Map<String, Object> map : mapList) {
                if(!(Objects.equals(map.get("groupNo"),"-1")) && Objects.equals(group.getGroupNo(),map.get("groupNo"))){
                    User user = userService.findById(Long.valueOf(map.get("id").toString()));
                    users.add(user);
                }else {
                    continue;
                }
            }
            map1.put("name", group.getGroupName());
            map1.put("value", users);
            lists.add(map1);
        }
        return Result.build(Msg.OK, "", lists);
    }
}
