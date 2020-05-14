package com.jingliang.mall.controller;

import com.jingliang.mall.common.Msg;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.service.BuyerManageService;
import com.jingliang.mall.service.GroupService;
import com.jingliang.mall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lmd
 * @date 2020/5/11
 * @company 晶粮
 */
@Api(tags = "商户管理")
@RestController
@RequestMapping("/buyerManage")
@Slf4j
public class BuyerManageController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final BuyerManageService buyerManageService;
    private final UserService userService;
    private final GroupService groupService;

    public BuyerManageController(BuyerManageService buyerManageService, UserService userService, GroupService groupService) {
        this.buyerManageService = buyerManageService;
        this.userService = userService;
        this.groupService = groupService;
    }

    /**
     * 根据父分组分组统计商户增加量
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/date/parentGroupId/achievement")
    @ApiOperation(value = "根据父分组分组统计商户增加量")
    public Result<List<Map<String, Object>>> dateAndGroupNoAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                       Long parentGroupId,
                                                                       HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //如果未传父分组编号，则赋值-1L
        if (parentGroupId == null) {
            parentGroupId = -1L;
            List<Map<String, Object>> counts = buyerManageService.dateAndParentGroupIdAchievement(startTime, endTime, parentGroupId);
            return Result.buildQueryOk(counts);
        }
        //通过父组id查询
        List<Map<String, Object>> counts = buyerManageService.dateAndParentGroupIdAchievement(startTime, endTime, parentGroupId);
        return Result.buildQueryOk(counts);
    }

    /**
     * 查询组内销售名下的商户数
     *
     * @param startTime
     * @param endTime
     * @param groupNo
     * @return
     */
    @GetMapping("/user/groupNo/buyerCounts")
    @ApiOperation(value = "查询组内销售名下的商户数")
    public Result<List<Map<String, Object>>> userByGroupNoAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                      String groupNo,
                                                                      HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        List<Map<String, Object>> counts = buyerManageService.userByGroupNoAchievement(startTime, endTime, groupNo);
        return Result.buildQueryOk(counts);
    }

    /**
     * 根据父id查询子区在每年的用户量
     *
     * @param startTime
     * @param endTime
     * @param parentGroupId
     * @return
     */
    @GetMapping("/year/parentGroupId/buyerCounts")
    @ApiOperation(value = "根据父id查询子区在每年的用户量")
    public Result<List<Map<String, Object>>> yearByDateAndParentGroupIdAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                                   Long parentGroupId,
                                                                                   HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //查询自身组名下的新增商户数
        List<Map<String, Object>> counts = buyerManageService.yearByDateAndParentGroupIdAchievement(startTime, endTime, parentGroupId);
        return Result.buildQueryOk(counts);
    }

    /**
     * 根据父id查询子区在每月的用户量
     *
     * @param startTime
     * @param endTime
     * @param parentGroupId
     * @return
     */
    @GetMapping("/month/parentGroupId/buyerCounts")
    @ApiOperation(value = "根据父id查询子区在每月的用户量")
    public Result<List<Map<String, Object>>> monthByDateAndParentGroupIdAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                                    Long parentGroupId,
                                                                                    HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //查询自身组名下的新增商户数
        List<Map<String, Object>> counts = buyerManageService.monthByDateAndParentGroupIdAchievement(startTime, endTime, parentGroupId);
        return Result.buildQueryOk(counts);
    }

    /**
     * 根据父id查询子区在每天的用户量
     *
     * @param startTime
     * @param endTime
     * @param parentGroupId
     * @return
     */
    @GetMapping("/day/parentGroupId/buyerCounts")
    @ApiOperation(value = "根据父id查询子区在每天的用户量")
    public Result<List<Map<String, Object>>> daysByDateAndParentGroupIdAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                                   Long parentGroupId,
                                                                                   HttpSession session) {
        //获取用户
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        if (user.getLevel() == null || user.getLevel() < 110) {
            return Result.build(Msg.FAIL, "无查看此分组的权限");
        }
        //查询自身组名下的新增商户数
        List<Map<String, Object>> counts = buyerManageService.daysByDateAndParentGroupIdAchievement(startTime, endTime, parentGroupId);
        return Result.buildQueryOk(counts);
    }

}
