package com.jingliang.mall.controller;

import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.service.GroupService;
import com.jingliang.mall.service.ManagerService;
import com.jingliang.mall.service.UserService;
import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 后台管理系统数据分析
 *
 * @author Xiaobing Li
 * @version 1.0
 * @date 2019-11-01 10:40
 */
@Api(description = "数据分析")
@RestController
@RequestMapping("/dataAnalysis")
@Slf4j
public class ManagerController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final UserService userService;
    private final ManagerService managerService;
    private final GroupService groupService;

    public ManagerController(UserService userService, ManagerService managerService, GroupService groupService) {
        this.userService = userService;
        this.managerService = managerService;
        this.groupService = groupService;
    }

    /*————————————————————————查询绩效————————————————————————————*/

    /**
     * 通过parentid查询大区绩效绩效信息
     */
    @GetMapping("/searchAchievementsByGroup")
    @ApiOperation(description = "查询各个分区时间段内的所有绩效")
    public Result<List<Map<String, Object>>> searchAchievementsByGroup(Long parentGroupId,
                                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                       HttpSession session) {
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //2.判断传参是查总绩效还是分区绩效
        if (!Objects.isNull(parentGroupId)) {
            //未传父组id，查询总绩效
            List<Map<String, Object>> achievements = managerService.findGroupAchievement(parentGroupId, startTime, endTime);
            log.debug("返回参数:{}", achievements);
            return Result.buildQueryOk(achievements);
        }
        return Result.buildParamFail();
    }

    /**
     * 通过parentid查询大区绩效绩效信息
     */
    @GetMapping("/searchAchievementsByGroupNo")
    @ApiOperation(description = "查询各个分区时间段内的所有绩效")
    public Result<List<Map<String, Object>>> searchAchievementsByGroupNo(String groupNo,
                                                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                         HttpSession session) {
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //2.判断传参是查总绩效还是分区绩效
        if (!Objects.isNull(groupNo)) {
            //未传父组id，查询总绩效
            List<Map<String, Object>> achievements = managerService.findGroupAchievementByGroupNo(groupNo, startTime, endTime);
            log.debug("返回参数:{}", achievements);
            return Result.buildQueryOk(achievements);
        }
        return Result.buildParamFail();
    }

    /**
     * 通过分区查询销售绩效信息
     */
    @GetMapping("/searchAchievementsWithUser")
    @ApiOperation(description = "查询各个销售时间段内的所有绩效")
    public Result<List<Map<String, Object>>> searchAchievementsWithUser(String groupNo,
                                                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                        HttpSession session) {
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //2.查询各个销售的绩效
        List<Map<String, Object>> achievements = managerService.findUserAchievement(groupNo, startTime, endTime);
        log.debug("返回参数:{}", achievements);
        return Result.buildQueryOk(achievements);
    }

    /**
     * 查询总绩效-年
     */
    @GetMapping("/searchAchievementsByYear")
    @ApiOperation(description = "根据年份查询绩效")
    public Result<?> searchAchievementsByYear(Long parentGroupId,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                              HttpSession session) {
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        if (!Objects.isNull(parentGroupId)) {
            List<Map<String, Object>> achievements = managerService.findGroupAchievementWithTimeByYear(parentGroupId, startTime, endTime);
            TreeSet<Object> date = achievements.stream().map(stringObjectMap -> stringObjectMap.get("date")).collect(Collectors.toCollection(TreeSet::new));
            Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
            achievements.forEach(stringObjectMap -> {
                if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                    map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
                }
                map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
            });
            List<List<Map<String, Object>>> list = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            Map<String, Object> resultMap = new HashMap<>(156);
            resultMap.put("x", date);
            resultMap.put("data", list);
            log.debug("返回参数:{}", resultMap);
            return Result.buildQueryOk(resultMap);
        }
        return Result.buildParamFail();
    }

    /**
     * 查询总绩效-月
     */
    @GetMapping("/searchAchievementsByMonth")
    @ApiOperation(description = "根据月份查询绩效")
    public Result<?> searchAchievementsByMonth(Long parentGroupId,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                               HttpSession session) {
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //查询分组处理数据
        if (!Objects.isNull(parentGroupId)) {
            List<Map<String, Object>> achievements = managerService.findGroupAchievementWithTimeByMonth(parentGroupId, startTime, endTime);
            log.debug("返回参数:{}", achievements);
            Set<Object> date = achievements.stream().map(stringObjectMap -> stringObjectMap.get("date")).collect(Collectors.toSet());
            Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
            achievements.forEach(stringObjectMap -> {
                if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                    map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
                }
                map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
            });
            List<List<Map<String, Object>>> list = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            Map<String, Object> resultMap = new HashMap<>(156);
            resultMap.put("x", date);
            resultMap.put("data", list);
            log.debug("返回参数:{}", resultMap);
            return Result.buildQueryOk(resultMap);
        }
        return Result.buildParamFail();
    }

    /**
     * 查询总绩效-日
     */
    @GetMapping("/searchAchievementsByDay")
    public Result<?> searchAchievementsByDay(Long parentGroupId,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                             HttpSession session) {
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        if (!Objects.isNull(parentGroupId)) {
            List<Map<String, Object>> achievements = managerService.findGroupAchievementWithTimeByDay(parentGroupId, startTime, endTime);
            log.debug("返回参数:{}", achievements);
            Set<Object> date = achievements.stream().map(stringObjectMap -> stringObjectMap.get("date")).collect(Collectors.toSet());
            Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
            achievements.forEach(stringObjectMap -> {
                if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                    map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
                }
                map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
            });
            List<List<Map<String, Object>>> list = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            Map<String, Object> resultMap = new HashMap<>(156);
            resultMap.put("x", date);
            resultMap.put("data", list);
            return Result.buildQueryOk(resultMap);
        }
        return Result.buildParamFail();
    }

    /**
     * 查询总绩效-年
     */
    @GetMapping("/searchAchievementsByYearAndGroupNo")
    @ApiOperation(description = "根据年份查询绩效")
    public Result<?> searchAchievementsByYearAndGroupNo(String groupNo,
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                        HttpSession session) {
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        if (!Objects.isNull(groupNo)) {
            List<Map<String, Object>> achievements = managerService.findGroupAchievementWithTimeByYearAndGroupNo(groupNo, startTime, endTime);
            TreeSet<Object> date = achievements.stream().map(stringObjectMap -> stringObjectMap.get("date")).collect(Collectors.toCollection(TreeSet::new));
            Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
            achievements.forEach(stringObjectMap -> {
                if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                    map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
                }
                map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
            });
            List<List<Map<String, Object>>> list = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            Map<String, Object> resultMap = new HashMap<>(156);
            resultMap.put("x", date);
            resultMap.put("data", list);
            log.debug("返回参数:{}", resultMap);
            return Result.buildQueryOk(resultMap);
        }
        return Result.buildParamFail();
    }

    /**
     * 查询总绩效-月
     */
    @GetMapping("/searchAchievementsByMonthAndGroupNo")
    @ApiOperation(description = "根据月份查询绩效")
    public Result<?> searchAchievementsByMonthAndGroupNo(String groupNo,
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                         HttpSession session) {
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //查询分组处理数据
        if (!Objects.isNull(groupNo)) {
            List<Map<String, Object>> achievements = managerService.findGroupAchievementWithTimeByMonthAndGroupNo(groupNo, startTime, endTime);
            log.debug("返回参数:{}", achievements);
            Set<Object> date = achievements.stream().map(stringObjectMap -> stringObjectMap.get("date")).collect(Collectors.toSet());
            Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
            achievements.forEach(stringObjectMap -> {
                if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                    map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
                }
                map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
            });
            List<List<Map<String, Object>>> list = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            Map<String, Object> resultMap = new HashMap<>(156);
            resultMap.put("x", date);
            resultMap.put("data", list);
            log.debug("返回参数:{}", resultMap);
            return Result.buildQueryOk(resultMap);
        }
        return Result.buildParamFail();
    }

    /**
     * 查询总绩效-日
     */
    @GetMapping("/searchAchievementsByDayAndGroupNo")
    public Result<?> searchAchievementsByDayAndGroupNo(String groupNo,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                       HttpSession session) {
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        if (!Objects.isNull(groupNo)) {
            List<Map<String, Object>> achievements = managerService.findGroupAchievementWithTimeByDayAndGroupNo(groupNo, startTime, endTime);
            log.debug("返回参数:{}", achievements);
            Set<Object> date = achievements.stream().map(stringObjectMap -> stringObjectMap.get("date")).collect(Collectors.toSet());
            Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
            achievements.forEach(stringObjectMap -> {
                if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                    map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
                }
                map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
            });
            List<List<Map<String, Object>>> list = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            Map<String, Object> resultMap = new HashMap<>(156);
            resultMap.put("x", date);
            resultMap.put("data", list);
            return Result.buildQueryOk(resultMap);
        }
        return Result.buildParamFail();
    }

    /*————————————————————————查询订单量————————————————————————————*/

    /*
     * 查询总订单量
     * */
    @GetMapping("/findOrdersTotalByGroup")
    @ApiOperation(description = "根据分组查询订单量")
    public Result<List<Map<String, Object>>> findOrdersTotalByGroup(Long parentGroupId,
                                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                    HttpSession session) {
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //2.判断传参是查总绩效还是分区绩效
        if (!Objects.isNull(parentGroupId)) {
            List<Map<String, Object>> achievements = managerService.findOrdersTotalByGroup(parentGroupId, startTime, endTime);
            log.debug("返回参数:{}", achievements);
            return Result.buildQueryOk(achievements);
        }
        return Result.buildParamFail();
    }

    /*
     * 根据年份查询订单量-折线
     * */
    @GetMapping("/findOrdersTotalByGroupAndYear")
    @ApiOperation(description = "根据年份查询订单量-折线")
    public Result<?> findOrdersTotalByGroupAndYear(Long parentGroupId,
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                   HttpSession session) {
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //2.判断传参是否为空
        if (!Objects.isNull(parentGroupId)) {
            List<Map<String, Object>> achievements = managerService.findOrdersTotalByGroupAndYear(parentGroupId, startTime, endTime);
            Set<Object> date = achievements.stream().map(stringObjectMap -> stringObjectMap.get("date")).collect(Collectors.toSet());
            Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
            achievements.forEach(stringObjectMap -> {
                if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                    map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
                }
                map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
            });
            List<List<Map<String, Object>>> list = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            Map<String, Object> resultMap = new HashMap<>(156);
            resultMap.put("x", date);
            resultMap.put("data", list);
            log.debug("返回参数:{}", resultMap);
            return Result.buildQueryOk(resultMap);
        }
        return Result.buildParamFail();
    }

    /*
     * 根据月份查询订单量-折线
     * */
    @GetMapping("/findOrdersTotalByGroupAndMonth")
    @ApiOperation(description = "根据年份查询订单量-折线")
    public Result<?> findOrdersTotalByGroupAndMonth(Long parentGroupId,
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                    HttpSession session) {
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //2.判断传参是查总绩效还是分区绩效
        if (!Objects.isNull(parentGroupId)) {
            //未传父组id，查询总绩效
            List<Map<String, Object>> achievements = managerService.findOrdersTotalByGroupAndMonth(parentGroupId, startTime, endTime);
            Set<Object> date = achievements.stream().map(stringObjectMap -> stringObjectMap.get("date")).collect(Collectors.toSet());
            Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
            achievements.forEach(stringObjectMap -> {
                if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                    map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
                }
                map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
            });
            List<List<Map<String, Object>>> list = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            Map<String, Object> resultMap = new HashMap<>(156);
            resultMap.put("x", date);
            resultMap.put("data", list);
            log.debug("返回参数:{}", resultMap);
            return Result.buildQueryOk(resultMap);
        }
        return Result.buildParamFail();
    }

    /*
     * 根据天数查询订单量-折线
     * */
    @GetMapping("/findOrdersTotalByGroupAndDay")
    @ApiOperation(description = "根据年份查询订单量-折线")
    public Result<?> findOrdersTotalByGroupAndDay(Long parentGroupId,
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                  HttpSession session) {
        //1.判断是否是管理员
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //2.判断传参是查总绩效还是分区绩效
        if (!Objects.isNull(parentGroupId)) {
            List<Map<String, Object>> achievements = managerService.findOrdersTotalByGroupAndDay(parentGroupId, startTime, endTime);
            Set<Object> date = achievements.stream().map(stringObjectMap -> stringObjectMap.get("date")).collect(Collectors.toSet());
            Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
            achievements.forEach(stringObjectMap -> {
                if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                    map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
                }
                map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
            });
            List<List<Map<String, Object>>> list = new ArrayList<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            Map<String, Object> resultMap = new HashMap<>(156);
            resultMap.put("x", date);
            resultMap.put("data", list);
            log.debug("返回参数:{}", resultMap);
            return Result.buildQueryOk(resultMap);
        }
        return Result.buildParamFail();
    }

    @GetMapping("/getBuyerTop30")
    @ApiOperation(description = "查询最新增加的商户数量的前三十")
    public Result<List<Map<String, Object>>> getBuyerTop30() {
        List<Map<String, Object>> getBuyer = managerService.getBuyerTop30();
        if (Objects.isNull(getBuyer)) {
            log.debug("返回结果:{}", getBuyer);
            return Result.buildParamFail();
        }
        log.debug("返回结果:{}", getBuyer);
        return Result.buildQueryOk(getBuyer);
    }

    @GetMapping("/findSaleByGroup")
    @ApiOperation(description = "查询组下的所有销售和其名下商户数")
    public Result<?> findSaleByGroup(String groupNo) {
        if (!Objects.isNull(groupNo)) {
//            //未传父组id，查询总绩效
//            List<User> salies = managerService.findSaleByGroup(groupNo);
            List<Map<String, Object>> mapList = managerService.findAllBySaleIdAndGroupNo(groupNo);
            log.debug("返回参数:{}", mapList);
            return Result.buildQueryOk(mapList);
        }
        return Result.buildParamFail();
    }

    @GetMapping("/findGoldDontReturn")
    @ApiOperation(description = "查询未返金币的用户并返回金币")
    public Result<?> findGoldDontReturn(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date createTime) {
        Map<String, Integer> goldDontReturn = managerService.findGoldDontReturn(createTime);
        return Result.buildSaveOk(goldDontReturn);
    }

    /**
     * 根据父组id查询前10天的绩效
     *
     * @return
     */
    @GetMapping("/findParentGroupIdTo10DayLate")
    @ApiOperation(description = "根据父组id查询前10天的绩效")
    public Result<?> findGoldDontReturn() {
        List<Map<String, Object>> achievements = managerService.findGroupAchievementWithTimeBy10DayLate();
        log.debug("返回参数:{}", achievements);
        Set<Object> date = achievements.stream().map(stringObjectMap -> stringObjectMap.get("Days")).collect(Collectors.toSet());
        Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
        achievements.forEach(stringObjectMap -> {
            if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
            }
            map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
        });
        List<List<Map<String, Object>>> list = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        Map<String, Object> resultMap = new HashMap<>(156);
        List<String> days = new ArrayList<>();
        for (Object o : date) {
            days.add(o.toString());
        }
        //Collections.sort()方法默认正序，String或Integer这些已经实现Comparable接口的类来说，可以直接使用Collections.sort方法传入list参数来实现默认方式（正序）排序，
        //如果不想使用默认方式（正序）排序，可以通过Collections.sort传入第二个参数类型为Comparator来自定义排序规则
        Collections.sort(days);
        resultMap.put("x", days);
        resultMap.put("data", list);
        return Result.buildQueryOk(resultMap);
    }

    /**
     * 按组查询前10天的绩效
     *
     * @param groupNo
     * @return
     */
    @GetMapping("/findGroupNoTo10DayLate")
    @ApiOperation(description = "按组查询前10天的绩效")
    public Result<?> findGroupNoTo10DayLate(String groupNo) {
        List<Map<String, Object>> achievements = managerService.findGroupAchievementWithTimeByGroupNo10DayLate(groupNo.replaceAll("0*$", "") + "%");
        log.debug("返回参数:{}", achievements);
        List<Object> date = achievements.stream().map(stringObjectMap -> stringObjectMap.get("Days")).collect(Collectors.toList());
        Map<String, List<Map<String, Object>>> map = new HashMap<>(156);
        achievements.forEach(stringObjectMap -> {
            if (map.get(((String) stringObjectMap.get("groupName"))) == null) {
                map.put(((String) stringObjectMap.get("groupName")), new ArrayList<>());
            }
            map.get(((String) stringObjectMap.get("groupName"))).add(stringObjectMap);
        });
        List<List<Map<String, Object>>> list = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        Map<String, Object> resultMap = new HashMap<>(156);
        resultMap.put("x", date);
        resultMap.put("data", list);
        return Result.buildQueryOk(resultMap);
    }

    @GetMapping("/findProductTypeSalePrice")
    @ApiOperation(description = "查询各个商品种类的销售情况")
    public Result<?> findProductTypeSalePrice(String groupNo) {
        List<Map<String, Object>> typePrice = new ArrayList<>();
        if (Objects.isNull(groupNo)) {
            typePrice = managerService.findProductTypeSalePrice();
        } else {
            typePrice = managerService.findProductTypeSalePrice(groupNo);
        }
        return Result.buildQueryOk(typePrice);
    }
}
