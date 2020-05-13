package com.jingliang.mall.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jingliang.mall.bean.ConfluenceDetail;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.*;
import com.jingliang.mall.req.BuyerReq;
import com.jingliang.mall.req.OrderReq;
import com.jingliang.mall.req.UserReq;
import com.jingliang.mall.resp.BuyerResp;
import com.jingliang.mall.resp.ConfluenceDetailResp;
import com.jingliang.mall.resp.UserResp;
import com.jingliang.mall.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
@Api(tags = "微信管理")
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
    @ApiOperation(value = "根据父分组分组统计组下业绩")
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
    @ApiOperation(value = "分组统计分组及子分组下的销售（销售、区域经理、老板）业绩")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "开始时间", name = "createTimeStart", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "结束时间", name = "createTimeEnd", dataType = "date", paramType = "query", required = true),
    })
    public Result<List<Map<String, Object>>> bossGroupUserAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                      String groupNo, HttpSession session) {

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
    @ApiOperation(value = "统计指定销售下商户的所产生的总绩效")
    public Result<Map<String, Object>> bossUserAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                           Long saleUserId, HttpSession session) {
        //当前操作人
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        //要查询的销售员
        User saleUser = userService.findById(saleUserId);
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
            return Result.build(Msg.FAIL, "无查看此销售信息权限");
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
    @ApiOperation(value = "统计指定销售下商户的所产生的绩效")
    public Result<List<Map<String, Object>>> bossUserBuyerAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                      Long saleUserId, HttpSession session) {
        //当前操作人
        User user = (User) session.getAttribute(sessionUser);
        user = userService.findById(user.getId());
        //要查询的销售员
        User saleUser = userService.findById(saleUserId);
        //1.判断是否查询的是自己的商户
        if (user.getId().equals(saleUserId)) {
            //1.1.查询销售自己所有商户产生的绩效
            List<Map<String, Object>> achievements = wechatManageService.bossSelfBuyerAchievement(saleUserId, startTime, endTime);
            return Result.buildQueryOk(achievements);
        }
        //1.2.不是查询的是自己的商户，判断要查询的销售是否是自己分组下的人
        if (!saleUser.getGroupNo().startsWith(user.getGroupNo().replaceAll("0*$", ""))) {
            return Result.build(Msg.FAIL, "无查看此销售信息权限");
        }
        //1.3.查询指定销售在自己组下所有商户产生的绩效
        List<Map<String, Object>> achievements = wechatManageService.bossUserBuyerAchievement(user.getGroupNo().replaceAll("0*$", "") + "%", saleUserId, startTime, endTime);
        return Result.buildQueryOk(achievements);
    }


    /**
     * 统计指定商户下所有的订单产生的绩效
     */
    @GetMapping("/boss/buyer/order/achievement")
    @ApiOperation(value = "统计指定商户下所有的订单产生的绩效")
    public Result<List<Map<String, Object>>> bossBuyerOrderAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
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
            return Result.build(Msg.FAIL, "无查看此销售信息权限");
        }
        //1.3.查询指定销售在自己组下的商户所有订单产生的绩效
        List<Map<String, Object>> achievements = wechatManageService.bossUserBuyerOrderAchievement(user.getGroupNo().replaceAll("0*$", "") + "%", buyer.getId(), startTime, endTime);
        return Result.buildQueryOk(achievements);
    }


    /**
     * 统计指定商户下的订单的详情产生的绩效
     */
    @GetMapping("/boss/buyer/order/detail/achievement")
    @ApiOperation(value = "统计指定商户下所有的订单产生的绩效")
    public Result<List<Map<String, Object>>> bossBuyerOrderDetailAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                             Long orderId, HttpSession session) {
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
        return Result.build(Msg.FAIL, "无查看此销售信息权限");
    }

    /**
     * 查询分组统计各个组下的商品大类所产生的绩效
     */
    @GetMapping("/boss/group/product/type/achievement")
    @ApiOperation(value = "查询分组统计各个组下的商品大类所产生的绩效")
    public Result<List<Map<String, Object>>> bossGroupProductTypeAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                             String groupNo, HttpSession session) {
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
    @ApiOperation(value = "查询分组统计各个组下的商品所产生的绩效")
    public Result<List<Map<String, Object>>> bossGroupProductAchievement(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                                                         String groupNo, HttpSession session) {
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
     * 查询分组统计各个组下的商品所产生的绩效
     */
    @GetMapping("/boss/x")
    @ApiOperation(value = "查询分组统计各个组下的商品所产生的绩效")
    public Result<List<Map<String, Object>>> x(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                               String groupNo, HttpSession session) {
        wechatManageService.x();
        return null;
    }

    public static void main(String[] args) throws IOException {
        Random random = new Random();
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 5000; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < 12; j++) {
                int i1 = random.nextInt(10);
                if (j == 0) {
                    i1 = random.nextInt(9) + 1;
                }
                builder.append(i1);
            }
            set.add(builder.toString());
        }
        Writer writer = new OutputStreamWriter(new FileOutputStream("D:\\Users\\CleanCode\\Desktop\\x.txt", true));
        for (String s : set) {
            writer.write(s + "\r\n");
        }
        writer.close();

    }
//    /**
//     * 查询所有销售（领导身份）
//     */
//    @GetMapping("/boss/user/all")
//    @ApiOperation(value = "查询所有销售（老板身份）")
//    @ApiImplicitParams({
//            @ApiImplicitParam(value = "开始时间", name = "createTimeStart", dataType = "date", paramType = "query", required = true),
//            @ApiImplicitParam(value = "结束时间", name = "createTimeEnd", dataType = "date", paramType = "query", required = true),
//    })
//    public Result<List<ConfluenceDetailResp>> bossPageAllUser(@ApiIgnore UserReq userReq) {
//        log.debug("请求参数：{}", userReq);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(userReq.getCreateTimeEnd());
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//        List<User> userPage = userService.findAll();
//        //计算
//        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
//        userPage.forEach(user -> {
//            ConfluenceDetail confluenceDetail = wechatManageService.userPerformanceSummary(user, userReq.getCreateTimeStart(), userReq.getCreateTimeEnd());
//            confluenceDetails.add(confluenceDetail);
//        });
//        List<ConfluenceDetailResp> confluenceDetailResps = BeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class);
//        confluenceDetailResps = confluenceDetailResps.stream().sorted(Comparator.comparing(ConfluenceDetailResp::getTotalPrice).reversed()).collect(Collectors.toList());
//        log.debug("返回结果：{}", confluenceDetailResps);
//        return Result.buildQueryOk(confluenceDetailResps);
//    }

    /**
     * 查询指定销售员绩效（领导/区域经理）
     */
    @GetMapping({"/boss/user", "/manager/user"})
    @ApiOperation(value = "查询指定销售员绩效（老板/区域经理）")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "销售Id", name = "id", dataType = "long", paramType = "query", required = true),
            @ApiImplicitParam(value = "开始时间", name = "createTimeStart", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "结束时间", name = "createTimeEnd", dataType = "date", paramType = "query", required = true),
    })
    public Result<ConfluenceDetailResp> bossUser(@ApiIgnore UserReq userReq) {
        log.debug("请求参数：{}", userReq);
        if (Objects.isNull(userReq.getId())) {
            return Result.buildParamFail();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userReq.getCreateTimeEnd());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        User user = userService.findById(userReq.getId());
        ConfluenceDetail confluenceDetail = wechatManageService.userPerformanceSummary(user, userReq.getCreateTimeStart(), userReq.getUpdateTimeEnd());
        ConfluenceDetailResp confluenceDetailResp = BeanMapper.map(confluenceDetail, ConfluenceDetailResp.class);
        return Result.buildQueryOk(confluenceDetailResp);
    }

    /**
     * 查询销售员自己绩效总汇（销售）
     * {{host}}/wx/user?createTimeStart=2019-01-01&createTimeEnd=2019-12-12
     */
    @GetMapping("/user")
    @ApiOperation(value = "查询销售员自己绩效总汇(销售)")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "开始时间", name = "createTimeStart", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "结束时间", name = "createTimeEnd", dataType = "date", paramType = "query", required = true),
    })
    public Result<ConfluenceDetailResp> user(@ApiIgnore UserReq userReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", userReq);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userReq.getCreateTimeEnd());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        User user = (User) session.getAttribute(sessionUser);
        ConfluenceDetail confluenceDetail = wechatManageService.userPerformanceSummary(user, userReq.getCreateTimeStart(), userReq.getCreateTimeEnd());
        ConfluenceDetailResp confluenceDetailResp = BeanMapper.map(confluenceDetail, ConfluenceDetailResp.class);
        log.debug("返回结果：{}", confluenceDetailResp);
        return Result.buildQueryOk(confluenceDetailResp);
    }

    /**
     * 查询销售员自己下的商户（销售）
     * {{host}}/wx/user/buyers?createTimeStart=2019-01-01&createTimeEnd=2019-12-12&id=33
     */
    @GetMapping("/user/buyers")
    @ApiOperation(value = "查询销售员自己下的商户（销售）")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "开始时间", name = "createTimeStart", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "结束时间", name = "createTimeEnd", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "分页", name = "page", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(value = "每页条数", name = "pageSize", dataType = "int", paramType = "query", defaultValue = "10")
    })
    public Result<MallPage<ConfluenceDetailResp>> userBuyer(@ApiIgnore UserReq userReq, @ApiIgnore HttpSession
            session) {
        log.debug("请求参数：{}", userReq);
        User user = (User) session.getAttribute(sessionUser);
        PageRequest pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        if (StringUtils.isNotBlank(userReq.getClause())) {
            pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userReq.getCreateTimeEnd());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Page<Buyer> buyerPage = buyerService.findAllBySaleId(user.getId(), pageRequest);
        MallPage<ConfluenceDetailResp> confluenceMallPage = new MallPage<>();
        confluenceMallPage.setContent(new ArrayList<>());
        confluenceMallPage.setFirst(buyerPage.isFirst());
        confluenceMallPage.setLast(buyerPage.isLast());
        confluenceMallPage.setPage(buyerPage.getNumber());
        confluenceMallPage.setPageSize(buyerPage.getSize());
        confluenceMallPage.setTotalNumber(buyerPage.getTotalElements());
        confluenceMallPage.setTotalPages(buyerPage.getTotalPages());
        if (buyerPage.getContent().size() == 0) {
            return Result.buildQueryOk(confluenceMallPage);
        }
        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
        for (Buyer buyer : buyerPage) {
            ConfluenceDetail confluenceDetail = wechatManageService.buyerPerformanceSummary(buyer, user, userReq.getCreateTimeStart(), userReq.getCreateTimeEnd());
            //计算销售提成价格
            confluenceDetail.setRoyalty((long) (confluenceDetail.getTotalPrice() * user.getRatio() * 0.01));
            confluenceDetails.add(confluenceDetail);
        }
        confluenceMallPage.setContent(BeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class));
        log.debug("返回结果：{}", confluenceDetails);
        return Result.buildQueryOk(confluenceMallPage);
    }

    /**
     * 查询销售员下的商户（老板）
     * {{host}}/wx/boss/buyers?createTimeStart=2019-01-01&createTimeEnd=2019-12-12&id=33
     */
    @GetMapping("/boss/buyers")
    @ApiOperation(value = "查询销售员下的商户（老板）")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "销售Id", name = "id", dataType = "long", paramType = "query", required = true),
            @ApiImplicitParam(value = "开始时间", name = "createTimeStart", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "结束时间", name = "createTimeEnd", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "分页", name = "page", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(value = "每页条数", name = "pageSize", dataType = "int", paramType = "query", defaultValue = "10")
    })
    public Result<MallPage<ConfluenceDetailResp>> bossBuyer(@ApiIgnore UserReq userReq) {
        log.debug("请求参数：{}", userReq);
        User user = userService.findById(userReq.getId());
        if (Objects.isNull(user)) {
            return Result.build(Msg.DATA_FAIL, Msg.TEXT_USER_DATA_FAIL);
        }
        PageRequest pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        if (StringUtils.isNotBlank(userReq.getClause())) {
            pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userReq.getCreateTimeEnd());
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        Page<Buyer> buyerPage = buyerService.findAllBySaleId(userReq.getId(), pageRequest);
        MallPage<ConfluenceDetailResp> confluenceMallPage = new MallPage<>();
        confluenceMallPage.setContent(new ArrayList<>());
        confluenceMallPage.setFirst(buyerPage.isFirst());
        confluenceMallPage.setLast(buyerPage.isLast());
        confluenceMallPage.setPage(buyerPage.getNumber());
        confluenceMallPage.setPageSize(buyerPage.getSize());
        confluenceMallPage.setTotalNumber(buyerPage.getTotalElements());
        confluenceMallPage.setTotalPages(buyerPage.getTotalPages());
        if (buyerPage.getContent().size() == 0) {
            return Result.buildQueryOk(confluenceMallPage);
        }
        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
        for (Buyer buyerSale : buyerPage) {
            ConfluenceDetail confluenceDetail = wechatManageService.buyerPerformanceSummary(buyerSale, user, userReq.getCreateTimeStart(), userReq.getCreateTimeEnd());
            //计算销售提成价格
            confluenceDetail.setRoyalty((long) (confluenceDetail.getTotalPrice() * user.getRatio() * 0.01));
            confluenceDetails.add(confluenceDetail);
        }
        confluenceMallPage.setContent(BeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class));
        log.debug("返回结果：{}", confluenceDetails);
        return Result.buildQueryOk(confluenceMallPage);
    }


    /**
     * 查询商品销量(老板)[暂时不支持分页]
     */
    @GetMapping({"/boss/products"})
    @ApiOperation(value = "查询商品销量（老板）[暂时不支持分页]")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "开始时间", name = "createTimeStart", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "结束时间", name = "createTimeEnd", dataType = "date", paramType = "query", required = true),
    })
    public Result<List<ConfluenceDetailResp>> bossProducts(@ApiIgnore UserReq userReq) {
        log.debug("请求参数：{}", userReq);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userReq.getCreateTimeEnd());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        //1.查询指定时间的订单
        //1.1.查询每笔订单下的订单详情，取出商品分类保存
        //1.2.分类统计出商品的销量
        //查询指定时间的所有已经完成支付的订单
        Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            //订单状态300-600表示已经支付并未退货
            predicateList.add(cb.between(root.get("orderStatus"), 300, 600));
            predicateList.add(cb.between(root.get("payEndTime"), userReq.getCreateTimeStart(), calendar.getTime()));
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            return query.getRestriction();
        };
        List<Order> orderPage = orderService.findAll(orderSpecification);
        Map<String, Long> confluenceDetailMap = new HashMap<>(156);
        Map<String, Integer> productNum = new HashMap<>(156);
        orderPage.forEach(order -> {
            //循环订单详情（商品）
            order.getOrderDetails().forEach(orderDetail -> {
                //商品归类计算
                if (!confluenceDetailMap.containsKey(orderDetail.getProduct().getProductName())) {
                    confluenceDetailMap.put(orderDetail.getProduct().getProductName(), 0L);
                    productNum.put(orderDetail.getProduct().getProductName(), 0);
                }
                confluenceDetailMap.put(orderDetail.getProduct().getProductName(), confluenceDetailMap.get(orderDetail.getProduct().getProductName()) + orderDetail.getSellingPrice() * orderDetail.getProductNum());
                productNum.put(orderDetail.getProduct().getProductName(), productNum.get(orderDetail.getProduct().getProductName()) + orderDetail.getProductNum());
            });
        });
        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
        confluenceDetailMap.forEach((productName, totalPrice) -> {
            ConfluenceDetail confluenceDetail = new ConfluenceDetail();
            confluenceDetail.setName(productName);
            confluenceDetail.setTotalPrice(totalPrice);
            confluenceDetail.setNum(productNum.get(productName));
            confluenceDetails.add(confluenceDetail);

        });
        List<ConfluenceDetailResp> confluenceDetailResps = BeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class);
        confluenceDetailResps = confluenceDetailResps.stream().sorted(Comparator.comparing(ConfluenceDetailResp::getNum).reversed()).collect(Collectors.toList());
        log.debug("返回结果：{}", confluenceDetailResps);
        return Result.buildQueryOk(confluenceDetailResps);
    }

    /**
     * 查询销售自己下面的各个商品销量(销售)[暂时不支持分页]
     */
    @GetMapping("/user/products")
    @ApiOperation(value = "查询销售自己下面的各个商品销量(销售)[暂时不支持分页]")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "开始时间", name = "createTimeStart", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "结束时间", name = "createTimeEnd", dataType = "date", paramType = "query", required = true),
    })
    public Result<List<ConfluenceDetailResp>> userProducts(@ApiIgnore UserReq userReq, @ApiIgnore HttpSession
            session) {
        log.debug("请求参数：{}", userReq);
        User user = (User) session.getAttribute(sessionUser);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userReq.getCreateTimeEnd());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        //查询绑定会员列表
        List<Buyer> buyers = buyerService.findAllBySaleUserId(user.getId());
        Map<String, Long> confluenceDetailMap = new HashMap<>(156);
        Map<String, Integer> productNum = new HashMap<>(156);
        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
        for (Buyer buyer : buyers) {
            //1.查询指定时间的订单
            //1.1.查询每笔订单下的订单详情，取出商品分类保存
            //1.2.分类统计出商品的销量
            //查询指定时间的所有已经完成支付的订单
            Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态300-600表示已经支付并未退货
                predicateList.add(cb.between(root.get("orderStatus"), 300, 600));
                predicateList.add(cb.between(root.get("payEndTime"), userReq.getCreateTimeStart(), calendar.getTime()));
                predicateList.add(cb.equal(root.get("isAvailable"), true));
                predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
                query.where(cb.and(predicateList.toArray(new Predicate[0])));
                return query.getRestriction();
            };
            List<Order> orderPage = orderService.findAll(orderSpecification);
            orderPage.forEach(order -> {
                //循环订单详情（商品）
                order.getOrderDetails().forEach(orderDetail -> {
                    //商品归类计算
                    if (!confluenceDetailMap.containsKey(orderDetail.getProduct().getProductName())) {
                        confluenceDetailMap.put(orderDetail.getProduct().getProductName(), 0L);
                        productNum.put(orderDetail.getProduct().getProductName(), 0);
                    }
                    confluenceDetailMap.put(orderDetail.getProduct().getProductName(), confluenceDetailMap.get(orderDetail.getProduct().getProductName()) + orderDetail.getSellingPrice() * orderDetail.getProductNum());
                    productNum.put(orderDetail.getProduct().getProductName(), productNum.get(orderDetail.getProduct().getProductName()) + orderDetail.getProductNum());
                });
            });
        }
        confluenceDetailMap.forEach((productName, totalPrice) -> {
            ConfluenceDetail confluenceDetail = new ConfluenceDetail();
            confluenceDetail.setName(productName);
            confluenceDetail.setTotalPrice(totalPrice);
            confluenceDetail.setNum(productNum.get(productName));
            confluenceDetails.add(confluenceDetail);

        });

        List<ConfluenceDetailResp> confluenceDetailResps = BeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class);
        confluenceDetailResps = confluenceDetailResps.stream().sorted(Comparator.comparing(ConfluenceDetailResp::getNum).reversed()).collect(Collectors.toList());
        log.debug("返回结果：{}", confluenceDetailResps);
        return Result.buildQueryOk(confluenceDetailResps);

    }

    /**
     * 查询商户下的订单（销售）
     */
    @GetMapping("/user/buyer/orders")
    @ApiOperation(value = "查询商户下的订单（销售）")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商户Id", name = "id", dataType = "long", paramType = "query", required = true),
            @ApiImplicitParam(value = "开始时间", name = "createTimeStart", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "结束时间", name = "createTimeEnd", dataType = "date", paramType = "query", required = true),
    })
    public Result<MallPage<ConfluenceDetailResp>> userBuyerOrdersr(@ApiIgnore BuyerReq
                                                                           buyerReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerReq);
        User user = (User) session.getAttribute(sessionUser);
        PageRequest pageRequest = PageRequest.of(buyerReq.getPage(), buyerReq.getPageSize());
        if (StringUtils.isNotBlank(buyerReq.getClause())) {
            pageRequest = PageRequest.of(buyerReq.getPage(), buyerReq.getPageSize());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(buyerReq.getCreateTimeEnd());
        calendar.add(Calendar.DAY_OF_MONTH, 1);


        //1.查询指定时间的订单
        //1.1.查询每笔订单下的订单详情，取出商品分类保存
        //1.2.分类统计出商品的销量
        //查询指定时间的所有已经完成支付的订单
        Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            //订单状态300-600表示已经支付并未退货
            predicateList.add(cb.between(root.get("orderStatus"), 300, 600));
            predicateList.add(cb.between(root.get("payEndTime"), buyerReq.getCreateTimeStart(), calendar.getTime()));
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            predicateList.add(cb.equal(root.get("buyerId"), buyerReq.getId()));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("updateTime")));
            return query.getRestriction();
        };
        Page<Order> orderPage = orderService.findAll(orderSpecification, pageRequest);
        MallPage<ConfluenceDetailResp> confluenceMallPage = new MallPage<>();
        confluenceMallPage.setContent(new ArrayList<>());
        confluenceMallPage.setFirst(orderPage.isFirst());
        confluenceMallPage.setLast(orderPage.isLast());
        confluenceMallPage.setPage(orderPage.getNumber());
        confluenceMallPage.setPageSize(orderPage.getSize());
        confluenceMallPage.setTotalNumber(orderPage.getTotalElements());
        confluenceMallPage.setTotalPages(orderPage.getTotalPages());
        if (orderPage.getContent().size() == 0) {
            return Result.buildQueryOk(confluenceMallPage);
        }
        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
        orderPage.forEach(order -> {
            ConfluenceDetail confluenceDetail = new ConfluenceDetail();
            confluenceDetail.setId(order.getId());
            confluenceDetail.setName(order.getOrderNo());
            confluenceDetail.setTotalPrice(order.getTotalPrice());
            //每笔订单的提成
            confluenceDetail.setRoyalty((long) (order.getTotalPrice() * user.getRatio() * 0.01));
            confluenceDetails.add(confluenceDetail);
        });
        List<ConfluenceDetailResp> confluenceDetailResps = BeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class);
        confluenceMallPage.setContent(confluenceDetailResps);
        log.debug("返回结果：{}", confluenceMallPage);
        return Result.buildQueryOk(confluenceMallPage);
    }

    /**
     * 查询商户下的订单详情（销售）
     */
    @GetMapping("/user/buyer/order/orderDetail")
    @ApiOperation(value = "查询商户下的订单详情（销售）")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商户Id", name = "id", dataType = "long", paramType = "query", required = true),
    })
    public Result<List<ConfluenceDetailResp>> userBuyerOrderDetail(@ApiIgnore OrderReq
                                                                           orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq);
        User user = (User) session.getAttribute(sessionUser);
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderReq.getId());
        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
        orderDetails.forEach(orderDetail -> {
            ConfluenceDetail confluenceDetail = new ConfluenceDetail();
            confluenceDetail.setId(orderDetail.getId());
            confluenceDetail.setName(orderDetail.getProduct().getProductName());
            confluenceDetail.setTotalPrice(orderDetail.getSellingPrice() * orderDetail.getProductNum());
            //每笔订单的提成
            confluenceDetail.setRoyalty((long) (orderDetail.getSellingPrice() * orderDetail.getProductNum() * user.getRatio() * 0.01));
            confluenceDetails.add(confluenceDetail);
        });
        List<ConfluenceDetailResp> confluenceDetailResps = BeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class);
        log.debug("返回结果：{}", confluenceDetailResps);
        return Result.buildQueryOk(confluenceDetailResps);
    }

    /**
     * 查询商户下各个商品的销量(销售)[暂时不支持分页]
     */
    @GetMapping("/user/buyer/products")
    @ApiOperation(value = "查询商户下各个商品的销量(销售)[暂时不支持分页]")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商户Id", name = "id", dataType = "long", paramType = "query", required = true),
            @ApiImplicitParam(value = "开始时间", name = "createTimeStart", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "结束时间", name = "createTimeEnd", dataType = "date", paramType = "query", required = true),
    })
    public Result<List<ConfluenceDetailResp>> userBuyerProducts(@ApiIgnore BuyerReq buyerReq) {
        log.debug("请求参数：{}", buyerReq);
        PageRequest pageRequest = PageRequest.of(buyerReq.getPage(), buyerReq.getPageSize());
        if (StringUtils.isNotBlank(buyerReq.getClause())) {
            pageRequest = PageRequest.of(buyerReq.getPage(), buyerReq.getPageSize());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(buyerReq.getCreateTimeEnd());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        //1.查询指定时间的订单
        //1.1.查询每笔订单下的订单详情，取出商品分类保存
        //1.2.分类统计出商品的销量
        //查询指定时间的所有已经完成支付的订单
        Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            //订单状态300-600表示已经支付并未退货
            predicateList.add(cb.between(root.get("orderStatus"), 300, 600));
            predicateList.add(cb.between(root.get("payEndTime"), buyerReq.getCreateTimeStart(), calendar.getTime()));
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            predicateList.add(cb.equal(root.get("buyerId"), buyerReq.getId()));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            return query.getRestriction();
        };
        Page<Order> orderPage = orderService.findAll(orderSpecification, pageRequest);
        Map<String, Long> confluenceDetailMap = new HashMap<>(156);
        Map<String, Integer> productNum = new HashMap<>(156);
        orderPage.forEach(order -> {
            //循环订单详情（商品）
            order.getOrderDetails().forEach(orderDetail -> {
                //商品归类计算
                if (!confluenceDetailMap.containsKey(orderDetail.getProduct().getProductTypeName())) {
                    confluenceDetailMap.put(orderDetail.getProduct().getProductTypeName(), 0L);
                    productNum.put(orderDetail.getProduct().getProductTypeName(), 0);
                }
                confluenceDetailMap.put(orderDetail.getProduct().getProductTypeName(), confluenceDetailMap.get(orderDetail.getProduct().getProductTypeName()) + orderDetail.getSellingPrice() * orderDetail.getProductNum());
                productNum.put(orderDetail.getProduct().getProductTypeName(), productNum.get(orderDetail.getProduct().getProductTypeName()) + orderDetail.getProductNum());
            });
        });
        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
        confluenceDetailMap.forEach((productName, totalPrice) -> {
            ConfluenceDetail confluenceDetail = new ConfluenceDetail();
            confluenceDetail.setName(productName);
            confluenceDetail.setTotalPrice(totalPrice);
            confluenceDetail.setNum(productNum.get(productName));
            confluenceDetails.add(confluenceDetail);

        });
        List<ConfluenceDetailResp> confluenceDetailResps = BeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class);
        confluenceDetailResps = confluenceDetailResps.stream().sorted(Comparator.comparing(ConfluenceDetailResp::getNum).reversed()).collect(Collectors.toList());
        log.debug("返回结果：{}", confluenceDetailResps);
        return Result.buildQueryOk(confluenceDetailResps);
    }

    /**
     * 查询所有销售信息
     */
    @GetMapping("/manager/page/users")
    @ApiOperation(value = "查询所有销售信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "分页", name = "page", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(value = "每页条数", name = "pageSize", dataType = "int", paramType = "query", defaultValue = "10")
    })
    public Result<MallPage<UserResp>> managerPageUsers(@ApiIgnore UserReq userReq) {
        PageRequest pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        if (StringUtils.isNotBlank(userReq.getClause())) {
            pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        }
        Page<User> allUserByPage = userService.findAllUserByPage(pageRequest);
        MallPage<UserResp> userRespMallPage = MUtils.toMallPage(allUserByPage, UserResp.class);
        return Result.buildQueryOk(userRespMallPage);
    }


    /**
     * 老板查看所有销售经理
     */
    @GetMapping("/boss/managers")
    @ApiOperation(value = "老板查看所有销售经理")
    public Result<List<UserResp>> bossManagers() {
        List<User> userList = userService.findByLevel(110);
        List<UserResp> userResps = BeanMapper.mapList(userList, UserResp.class);
        return Result.buildQueryOk(userResps);
    }

    /**
     * 查看区域经理下的所有销售
     */
    @GetMapping("/manager/sales")
    @ApiOperation(value = "查看区域经理下的所有销售")
    public Result<List<UserResp>> managersSales(@DateTimeFormat(pattern = "yyyy-MM-dd")
                                                @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") Date
                                                        creationTime, @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") Date endTime, Long id, HttpSession session) {
        Specification<ManagerSale> managerSaleSpecification = (Specification<ManagerSale>) (root, query, cb) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(cb.equal(root.get("managerId"), id));
            andPredicateList.add(cb.equal(root.get("isAvailable"), true));
            Predicate andPredicate = cb.and(andPredicateList.toArray(new Predicate[0]));
            query.where(andPredicate);
            query.orderBy(cb.desc(root.get("createTime")));
            return query.getRestriction();
        };
        List<ManagerSale> managerSales = managerSaleService.findAll(managerSaleSpecification);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        List<User> collect = managerSales.stream().filter(managerSale -> managerSale.getUntyingTime().compareTo(creationTime) >= 0 && managerSale.getCreateTime().compareTo(calendar.getTime()) <= 0).map(ManagerSale::getUser).collect(Collectors.toList());
        User user = userService.findById(id);
        Long buyerId = user.getBuyerId();
        if (buyerId != null) {
            Buyer buyer = buyerService.findById(buyerId);
            if (buyer.getSaleUserId().equals(user.getId())) {
                user.setUserName(user.getUserName() + "(自己)");
                collect.add(user);

            }
        }
        List<UserResp> userResps = BeanMapper.mapList(collect, UserResp.class);
        return Result.buildQueryOk(userResps);
    }
    //销售查看自己下的所有客户

    /**
     * 查询指定销售下的商户
     */
    @GetMapping("/user/page/buyers")
    @ApiOperation(value = "查询指定销售下的商户")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "销售Id", name = "id", dataType = "long", paramType = "query", required = true),
            @ApiImplicitParam(value = "分页", name = "page", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(value = "每页条数", name = "pageSize", dataType = "int", paramType = "query", defaultValue = "10")
    })
    public Result<List<BuyerResp>> salePageBuyer(@DateTimeFormat(pattern = "yyyy-MM-dd")
                                                 @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") Date
                                                         creationTime, @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                 @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") Date endTime, Long id) {
        Specification<BuyerSale> buyerSaleSpecification = (Specification<BuyerSale>) (root, query, cb) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(cb.equal(root.get("saleId"), id));
            andPredicateList.add(cb.equal(root.get("isAvailable"), true));
            Predicate andPredicate = cb.and(andPredicateList.toArray(new Predicate[0]));
//            List<Predicate> orPredicateList = new ArrayList<>();
//            orPredicateList.add(cb.lessThanOrEqualTo(root.get("createTime"), endTime));
//            orPredicateList.add(cb.greaterThanOrEqualTo(root.get("untyingTime"), startTime));
//            Predicate orPredicate = cb.or(orPredicateList.toArray(new Predicate[0]));
//            query.where(andPredicate, orPredicate);
            query.where(andPredicate);
            query.orderBy(cb.desc(root.get("createTime")));
            return query.getRestriction();
        };

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        List<BuyerSale> buyerSales = buyerSaleService.finAll(buyerSaleSpecification);
        List<Buyer> collect = buyerSales.stream().filter(buyerSale -> {
            if (buyerSale.getUntyingTime().compareTo(creationTime) < 0 || buyerSale.getCreateTime().compareTo(calendar.getTime()) > 0) {
                return false;
            }
            return true;
        }).map(BuyerSale::getBuyer).collect(Collectors.toList());
        List<BuyerResp> buyerResps = BeanMapper.mapList(collect, BuyerResp.class);
        for (BuyerResp buyerResp : buyerResps) {
            BuyerAddress buyerAddress = buyerAddressService.findDefaultAddrByBuyerId(buyerResp.getId());
            if (buyerAddress != null) {
                String addr = buyerAddress.getProvince().getName() + "/" + buyerAddress.getCity().getName() + "/" + buyerAddress.getArea().getName() + buyerAddress.getDetailedAddress();
                buyerResp.setDefaultAddr(addr);
            }
        }
        return Result.buildQueryOk(buyerResps);
    }

    /**
     * 区域经理查看自己的销量
     */
    @GetMapping("/manager/volume")
    @ApiOperation(value = "区域经理查看自己的销量")
    public Result<List<ConfluenceDetailResp>> managersVolume(@DateTimeFormat(pattern = "yyyy-MM-dd")
                                                             @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") Date
                                                                     creationTime, @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                             @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") Date endTime, HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        Specification<ManagerSale> managerSaleSpecification = (Specification<ManagerSale>) (root, query, cb) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(cb.equal(root.get("managerId"), user.getId()));
            andPredicateList.add(cb.equal(root.get("isAvailable"), true));
            Predicate andPredicate = cb.and(andPredicateList.toArray(new Predicate[0]));
//            List<Predicate> orPredicateList = new ArrayList<>();
//            orPredicateList.add(cb.lessThanOrEqualTo(root.get("createTime"), startTime));
//            orPredicateList.add(cb.greaterThanOrEqualTo(root.get("untyingTime"), endTime));
//            Predicate orPredicate = cb.or(orPredicateList.toArray(new Predicate[0]));
            query.where(andPredicate/*, orPredicate*/);
            query.orderBy(cb.desc(root.get("createTime")));
            return query.getRestriction();
        };
        List<ManagerSale> managerSales = managerSaleService.findAll(managerSaleSpecification);
        //计算
        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
        managerSales.stream().filter(managerSale -> {
            if (managerSale.getUntyingTime().compareTo(creationTime) < 0 || managerSale.getCreateTime().compareTo(endTime) > 0) {
                return false;
            }
            return true;
        }).collect(Collectors.toList()).forEach(managerSale -> {
            ConfluenceDetail confluenceDetail = wechatManageService.userPerformanceSummary(managerSale.getUser()
                    , creationTime.before(managerSale.getCreateTime()) ? managerSale.getCreateTime() : creationTime
                    , endTime.before(managerSale.getUntyingTime()) ? endTime : managerSale.getUntyingTime());
            confluenceDetails.add(confluenceDetail);
        });
        List<ConfluenceDetailResp> confluenceDetailResps = BeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class);
        confluenceDetailResps = confluenceDetailResps.stream().sorted(Comparator.comparing(ConfluenceDetailResp::getTotalPrice).reversed()).collect(Collectors.toList());
        log.debug("返回结果：{}", confluenceDetailResps);
        return Result.buildQueryOk(confluenceDetailResps);
    }

    /**
     * 查询销售员下的商户（区域经理）
     * {{host}}/wx/manager/buyers?createTimeStart=2019-01-01&createTimeEnd=2019-12-12&id=33
     */
    @GetMapping("/manager/buyers")
    @ApiOperation(value = "查询销售员下的商户（区域经理）")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "销售Id", name = "id", dataType = "long", paramType = "query", required = true),
            @ApiImplicitParam(value = "开始时间", name = "createTimeStart", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "结束时间", name = "createTimeEnd", dataType = "date", paramType = "query", required = true),
    })
    public Result<List<ConfluenceDetailResp>> managerBuyer(@ApiIgnore UserReq userReq) {
        log.debug("请求参数：{}", userReq);
        User user = userService.findById(userReq.getId());
        if (Objects.isNull(user)) {
            return Result.build(Msg.DATA_FAIL, Msg.TEXT_USER_DATA_FAIL);
        }
        List<ManagerSale> managerSales = managerSaleService.findByManagerIdAndSaleId(user.getId(), userReq.getId());
        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
        managerSales.forEach(managerSale -> {
            List<Buyer> buyerPage = buyerService.findAllBySaleUserId(userReq.getId());
            for (Buyer buyerSale : buyerPage) {
                ConfluenceDetail confluenceDetail = wechatManageService.buyerPerformanceSummary(buyerSale
                        , user, userReq.getCreateTimeStart().before(managerSale.getCreateTime()) ? managerSale.getCreateTime() : userReq.getCreateTimeStart()
                        , userReq.getCreateTimeEnd().before(managerSale.getUntyingTime()) ? userReq.getCreateTimeEnd() : managerSale.getUntyingTime());
                //计算销售提成价格
                confluenceDetail.setRoyalty((long) (confluenceDetail.getTotalPrice() * user.getRatio() * 0.01));
                confluenceDetails.add(confluenceDetail);
            }
        });
        log.debug("返回结果：{}", confluenceDetails);
        List<ConfluenceDetailResp> confluenceDetailResps = BeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class);
        return Result.buildQueryOk(confluenceDetailResps);
    }
}
