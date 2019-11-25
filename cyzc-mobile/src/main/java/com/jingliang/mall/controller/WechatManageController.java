package com.jingliang.mall.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.UserReq;
import com.jingliang.mall.resp.ConfluenceDetailResp;
import com.jingliang.mall.resp.ConfluenceResp;
import com.jingliang.mall.service.BuyerService;
import com.jingliang.mall.service.OrderService;
import com.jingliang.mall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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

    public WechatManageController(UserService userService, BuyerService buyerService, OrderService orderService) {
        this.userService = userService;
        this.buyerService = buyerService;
        this.orderService = orderService;
    }

    /**
     * 查询汇总信息
     */
    @GetMapping("/manager/confluence")
    @ApiOperation(value = "查询汇总信息（领导身份）")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "开始时间", name = "startTime", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "结束时间", name = "endTime", dataType = "date", paramType = "query", required = true),
    })
    public MallResult<ConfluenceResp> managerConfluence(@ApiIgnore @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") Date startTime,
                                                        @ApiIgnore @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") Date endTime) {
        List<User> users = userService.findAll();
        ConfluenceResp confluenceResp = new ConfluenceResp();
        confluenceResp.setTotalPrice(0D);
        confluenceResp.setRoyalty(0D);
        users.forEach(user -> {
            //查询绑定会员列表
            List<Buyer> buyers = buyerService.findAllBySaleUserId(user.getId());
            AtomicReference<Double> totalPrice = new AtomicReference<>(0D);
            buyers.forEach(buyer -> {
                //查询会员在指定时间的所有已经完成支付的订单
                Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
                    List<Predicate> predicateList = new ArrayList<>();
                    //订单状态300-700表示已经支付
                    predicateList.add(cb.between(root.get("orderStatus"), 300, 700));
                    predicateList.add(cb.between(root.get("payEndTime"), startTime, endTime));
                    predicateList.add(cb.equal(root.get("isAvailable"), true));
                    predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
                    query.where(cb.and(predicateList.toArray(new Predicate[0])));
                    return query.getRestriction();
                };
                List<Order> orders = orderService.findAll(orderSpecification);
                if (orders.size() > 0) {
                    for (Order order : orders) {
                        //统计所有订单价格
                        totalPrice.updateAndGet(v -> v + order.getTotalPrice());
                    }
                }
                //计算退货的单子
                orderSpecification = (Specification<Order>) (root, query, cb) -> {
                    List<Predicate> predicateList = new ArrayList<>();
                    //订单状态大于700表示要扣除绩效
                    predicateList.add(cb.greaterThan(root.get("orderStatus"), 700));
                    predicateList.add(cb.between(root.get("finishTime"), startTime, endTime));
                    predicateList.add(cb.equal(root.get("isAvailable"), true));
                    predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
                    query.where(cb.and(predicateList.toArray(new Predicate[0])));
                    return query.getRestriction();
                };
                orders = orderService.findAll(orderSpecification);
                if (orders.size() > 0) {
                    for (Order order : orders) {
                        //减去要扣除绩效的单子
                        totalPrice.updateAndGet(v -> v - order.getTotalPrice());
                    }
                }
            });
            //总价
            confluenceResp.setTotalPrice(totalPrice.get() + confluenceResp.getTotalPrice());
            //计算销售提成价格--- 把百分比转换为小数在进行运算
            confluenceResp.setRoyalty(MatchUtils.mul(totalPrice.get(), user.getRatio() * 0.01 + confluenceResp.getRoyalty()));
        });
        return MallResult.buildQueryOk(confluenceResp);
    }

    /**
     * 分页查询所有用户（领导身份）
     */
    @GetMapping("/manager/user/page/all")
    @ApiOperation(value = "分页查询所有销售（领导身份）")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "开始时间", name = "createTimeStart", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "结束时间", name = "createTimeEnd", dataType = "date", paramType = "query", required = true),
    })
    public MallResult<MallPage<ConfluenceDetailResp>> pageAllUser(@ApiIgnore UserReq userReq) {
        log.debug("请求参数：{}", userReq);
        PageRequest pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        if (StringUtils.isNotBlank(userReq.getClause())) {
            pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize(), Sort.by(MallUtils.separateOrder(userReq.getClause())));
        }
        Page<User> userPage = userService.findAllUserByPage(pageRequest);
        MallPage<ConfluenceDetailResp> confluenceRespMallPage = new MallPage<>();
        confluenceRespMallPage.setContent(new ArrayList<>());
        confluenceRespMallPage.setFirst(userPage.isFirst());
        confluenceRespMallPage.setLast(userPage.isLast());
        confluenceRespMallPage.setPage(userPage.getNumber());
        confluenceRespMallPage.setPageSize(userPage.getSize());
        confluenceRespMallPage.setTotalNumber(userPage.getTotalElements());
        confluenceRespMallPage.setTotalPages(userPage.getTotalPages());
        if (userPage.getContent().size() == 0) {
            return MallResult.buildQueryOk(confluenceRespMallPage);
        }
        //计算
        List<ConfluenceDetailResp> confluenceDetailResps = new ArrayList<>();
        userPage.getContent().forEach(user -> {
            ConfluenceDetailResp confluenceDetailResp = new ConfluenceDetailResp();
            confluenceDetailResp.setName(user.getUserName());
            //查询绑定会员列表
            List<Buyer> buyers = buyerService.findAllBySaleUserId(user.getId());
            Double totalPrice = 0D;
            for (Buyer buyer : buyers) {
                //查询会员在指定时间的所有已经完成支付的订单
                Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
                    List<Predicate> predicateList = new ArrayList<>();
                    //订单状态300-700表示已经支付
                    predicateList.add(cb.between(root.get("orderStatus"), 300, 700));
                    predicateList.add(cb.between(root.get("payEndTime"), userReq.getCreateTimeStart(), userReq.getCreateTimeEnd()));
                    predicateList.add(cb.equal(root.get("isAvailable"), true));
                    predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
                    query.where(cb.and(predicateList.toArray(new Predicate[0])));
                    return query.getRestriction();
                };
                List<Order> orders = orderService.findAll(orderSpecification);
                if (orders.size() > 0) {
                    for (Order order : orders) {
                        //统计所有订单价格
                        totalPrice += order.getTotalPrice();
                    }
                }
                //计算退货的单子
                orderSpecification = (Specification<Order>) (root, query, cb) -> {
                    List<Predicate> predicateList = new ArrayList<>();
                    //订单状态大于700表示要扣除绩效
                    predicateList.add(cb.greaterThan(root.get("orderStatus"), 700));
                    predicateList.add(cb.between(root.get("finishTime"), userReq.getCreateTimeStart(), userReq.getCreateTimeEnd()));
                    predicateList.add(cb.equal(root.get("isAvailable"), true));
                    predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
                    query.where(cb.and(predicateList.toArray(new Predicate[0])));
                    return query.getRestriction();
                };
                orders = orderService.findAll(orderSpecification);
                if (orders.size() > 0) {
                    for (Order order : orders) {
                        //减去要扣除绩效的单子
                        totalPrice -= order.getTotalPrice();
                    }
                }
            }
            //销售提成
            confluenceDetailResp.setTotalPrice(totalPrice);
            confluenceDetailResp.setRoyalty(MatchUtils.mul(totalPrice, user.getRatio() * 0.01));
            confluenceDetailResps.add(confluenceDetailResp);
        });
        confluenceRespMallPage.setContent(confluenceDetailResps);
        log.debug("返回结果：{}", confluenceDetailResps);
        return MallResult.buildQueryOk(confluenceRespMallPage);
    }

    /**
     * 查询指定销售员绩效（领导）
     */
    @GetMapping("/manager/user")
    @ApiOperation(value = "查询指定销售员绩效（领导）")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "销售Id", name = "id", dataType = "long", paramType = "query", required = true),
            @ApiImplicitParam(value = "开始时间", name = "createTimeStart", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "结束时间", name = "createTimeEnd", dataType = "date", paramType = "query", required = true),
    })
    public MallResult<ConfluenceDetailResp> managerUser(@ApiIgnore UserReq userReq) {
        log.debug("请求参数：{}", userReq);
        if (Objects.isNull(userReq.getId())) {
            return MallResult.buildParamFail();
        }
        User user = userService.findById(userReq.getId());
        //计算绩效
        ConfluenceDetailResp confluenceDetailResp = new ConfluenceDetailResp();
        confluenceDetailResp.setName(user.getUserName());
        confluenceDetailResp.setTotalPrice(0D);
        confluenceDetailResp.setRoyalty(0D);
        //查询绑定会员列表
        List<Buyer> buyers = buyerService.findAllBySaleUserId(user.getId());
        Double totalPrice = 0D;
        for (Buyer buyer : buyers) {
            //查询会员在指定时间的所有已经完成支付的订单
            Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态300-700表示已经支付
                predicateList.add(cb.between(root.get("orderStatus"), 300, 700));
                predicateList.add(cb.between(root.get("payEndTime"), userReq.getCreateTimeStart(), userReq.getCreateTimeEnd()));
                predicateList.add(cb.equal(root.get("isAvailable"), true));
                predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
                query.where(cb.and(predicateList.toArray(new Predicate[0])));
                return query.getRestriction();
            };
            List<Order> orders = orderService.findAll(orderSpecification);
            if (orders.size() > 0) {
                for (Order order : orders) {
                    //统计所有订单价格
                    totalPrice += order.getTotalPrice();
                }
            }
            //计算退货的单子
            orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态大于700表示要扣除绩效
                predicateList.add(cb.greaterThan(root.get("orderStatus"), 700));
                predicateList.add(cb.between(root.get("finishTime"), userReq.getCreateTimeStart(), userReq.getCreateTimeEnd()));
                predicateList.add(cb.equal(root.get("isAvailable"), true));
                predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
                query.where(cb.and(predicateList.toArray(new Predicate[0])));
                return query.getRestriction();
            };
            orders = orderService.findAll(orderSpecification);
            if (orders.size() > 0) {
                for (Order order : orders) {
                    //减去要扣除绩效的单子
                    totalPrice -= order.getTotalPrice();
                }
            }
        }
        //销售提成
        confluenceDetailResp.setTotalPrice(totalPrice);
        confluenceDetailResp.setRoyalty(MatchUtils.mul(totalPrice, user.getRatio() * 0.01));
        return MallResult.buildQueryOk(confluenceDetailResp);
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
    public MallResult<ConfluenceDetailResp> user(@ApiIgnore UserReq userReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", userReq);
        User user = (User) session.getAttribute(sessionUser);
        //计算绩效
        ConfluenceDetailResp confluenceDetailResp = new ConfluenceDetailResp();
        confluenceDetailResp.setTotalPrice(0D);
        confluenceDetailResp.setRoyalty(0D);
        //查询绑定会员列表
        List<Buyer> buyers = buyerService.findAllBySaleUserId(user.getId());
        Double totalPrice = 0D;
        for (Buyer buyer : buyers) {
            //查询会员在指定时间的所有已经完成支付的订单
            Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态300-700表示已经支付
                predicateList.add(cb.between(root.get("orderStatus"), 300, 700));
                predicateList.add(cb.between(root.get("payEndTime"), userReq.getCreateTimeStart(), userReq.getCreateTimeEnd()));
                predicateList.add(cb.equal(root.get("isAvailable"), true));
                predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
                query.where(cb.and(predicateList.toArray(new Predicate[0])));
                return query.getRestriction();
            };
            List<Order> orders = orderService.findAll(orderSpecification);
            if (orders.size() > 0) {
                for (Order order : orders) {
                    //统计所有订单价格
                    totalPrice += order.getTotalPrice();
                }
            }
            //计算退货的单子
            orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态大于700表示要扣除绩效
                predicateList.add(cb.greaterThan(root.get("orderStatus"), 700));
                predicateList.add(cb.between(root.get("finishTime"), userReq.getCreateTimeStart(), userReq.getCreateTimeEnd()));
                predicateList.add(cb.equal(root.get("isAvailable"), true));
                predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
                query.where(cb.and(predicateList.toArray(new Predicate[0])));
                return query.getRestriction();
            };
            orders = orderService.findAll(orderSpecification);
            if (orders.size() > 0) {
                for (Order order : orders) {
                    //减去要扣除绩效的单子
                    totalPrice -= order.getTotalPrice();
                }
            }
        }
        //计算销售提成价格
        confluenceDetailResp.setTotalPrice(totalPrice);
        confluenceDetailResp.setRoyalty(MatchUtils.mul(totalPrice, user.getRatio() * 0.01));
        return MallResult.buildQueryOk(confluenceDetailResp);
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
    public MallResult<MallPage<ConfluenceDetailResp>> userBuyer(@ApiIgnore UserReq userReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", userReq);
        User user = (User) session.getAttribute(sessionUser);
        PageRequest pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        if (StringUtils.isNotBlank(userReq.getClause())) {
            pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        }
        Page<Buyer> buyerPage = buyerService.findAllByPage(userReq.getId(), pageRequest);
        MallPage<ConfluenceDetailResp> confluenceRespMallPage = new MallPage<>();
        confluenceRespMallPage.setContent(new ArrayList<>());
        confluenceRespMallPage.setFirst(buyerPage.isFirst());
        confluenceRespMallPage.setLast(buyerPage.isLast());
        confluenceRespMallPage.setPage(buyerPage.getNumber());
        confluenceRespMallPage.setPageSize(buyerPage.getSize());
        confluenceRespMallPage.setTotalNumber(buyerPage.getTotalElements());
        confluenceRespMallPage.setTotalPages(buyerPage.getTotalPages());
        if (buyerPage.getContent().size() == 0) {
            return MallResult.buildQueryOk(confluenceRespMallPage);
        }
        List<ConfluenceDetailResp> confluenceDetailResps = new ArrayList<>();
        for (Buyer buyer : buyerPage) {
            ConfluenceDetailResp confluenceDetailResp = new ConfluenceDetailResp();
            confluenceDetailResp.setName(buyer.getUserName());
            Double totalPrice = 0D;
            //查询会员在指定时间的所有已经完成支付的订单
            Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态300-700表示已经支付
                predicateList.add(cb.between(root.get("orderStatus"), 300, 700));
                predicateList.add(cb.between(root.get("payEndTime"), userReq.getCreateTimeStart(), userReq.getCreateTimeEnd()));
                predicateList.add(cb.equal(root.get("isAvailable"), true));
                predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
                query.where(cb.and(predicateList.toArray(new Predicate[0])));
                return query.getRestriction();
            };
            List<Order> orders = orderService.findAll(orderSpecification);
            if (orders.size() > 0) {
                for (Order order : orders) {
                    //统计所有订单价格
                    totalPrice += order.getTotalPrice();
                }
            }
            //计算退货的单子
            orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态大于700表示要扣除绩效
                predicateList.add(cb.greaterThan(root.get("orderStatus"), 700));
                predicateList.add(cb.between(root.get("finishTime"), userReq.getCreateTimeStart(), userReq.getCreateTimeEnd()));
                predicateList.add(cb.equal(root.get("isAvailable"), true));
                predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
                query.where(cb.and(predicateList.toArray(new Predicate[0])));
                return query.getRestriction();
            };
            orders = orderService.findAll(orderSpecification);
            if (orders.size() > 0) {
                for (Order order : orders) {
                    //减去要扣除绩效的单子
                    totalPrice -= order.getTotalPrice();
                }
            }
            //计算销售提成价格
            confluenceDetailResp.setTotalPrice(totalPrice);
            confluenceDetailResp.setRoyalty(MatchUtils.mul(totalPrice, user.getRatio() * 0.01));
            confluenceDetailResps.add(confluenceDetailResp);
        }
        confluenceRespMallPage.setContent(confluenceDetailResps);
        log.debug("返回结果：{}", confluenceDetailResps);
        return MallResult.buildQueryOk(confluenceRespMallPage);
    }

    /**
     * 查询销售员下的商户（领导）
     * {{host}}/wx/manager/buyers?createTimeStart=2019-01-01&createTimeEnd=2019-12-12&id=33
     */
    @GetMapping("/manager/buyers")
    @ApiOperation(value = "查询销售员下的商户（领导）")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "销售Id", name = "id", dataType = "long", paramType = "query", required = true),
            @ApiImplicitParam(value = "开始时间", name = "createTimeStart", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "结束时间", name = "createTimeEnd", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "分页", name = "page", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(value = "每页条数", name = "pageSize", dataType = "int", paramType = "query", defaultValue = "10")
    })
    public MallResult<MallPage<ConfluenceDetailResp>> manageBuyer(@ApiIgnore UserReq userReq) {
        log.debug("请求参数：{}", userReq);
        User user = userService.findById(userReq.getId());
        if (Objects.isNull(user)) {
            return MallResult.build(MallConstant.DATA_FAIL, MallConstant.TEXT_USER_DATA_FAIL);
        }
        PageRequest pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        if (StringUtils.isNotBlank(userReq.getClause())) {
            pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        }
        Page<Buyer> buyerPage = buyerService.findAllByPage(userReq.getId(), pageRequest);
        MallPage<ConfluenceDetailResp> confluenceRespMallPage = new MallPage<>();
        confluenceRespMallPage.setContent(new ArrayList<>());
        confluenceRespMallPage.setFirst(buyerPage.isFirst());
        confluenceRespMallPage.setLast(buyerPage.isLast());
        confluenceRespMallPage.setPage(buyerPage.getNumber());
        confluenceRespMallPage.setPageSize(buyerPage.getSize());
        confluenceRespMallPage.setTotalNumber(buyerPage.getTotalElements());
        confluenceRespMallPage.setTotalPages(buyerPage.getTotalPages());
        if (buyerPage.getContent().size() == 0) {
            return MallResult.buildQueryOk(confluenceRespMallPage);
        }
        List<ConfluenceDetailResp> confluenceDetailResps = new ArrayList<>();
        for (Buyer buyer : buyerPage) {
            ConfluenceDetailResp confluenceDetailResp = new ConfluenceDetailResp();
            confluenceDetailResp.setName(buyer.getUserName());
            Double totalPrice = 0D;
            //查询会员在指定时间的所有已经完成支付的订单
            Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态300-700表示已经支付
                predicateList.add(cb.between(root.get("orderStatus"), 300, 700));
                predicateList.add(cb.between(root.get("payEndTime"), userReq.getCreateTimeStart(), userReq.getCreateTimeEnd()));
                predicateList.add(cb.equal(root.get("isAvailable"), true));
                predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
                query.where(cb.and(predicateList.toArray(new Predicate[0])));
                return query.getRestriction();
            };
            List<Order> orders = orderService.findAll(orderSpecification);
            if (orders.size() > 0) {
                for (Order order : orders) {
                    //统计所有订单价格
                    totalPrice += order.getTotalPrice();
                }
            }
            //计算退货的单子
            orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态大于700表示要扣除绩效
                predicateList.add(cb.greaterThan(root.get("orderStatus"), 700));
                predicateList.add(cb.between(root.get("finishTime"), userReq.getCreateTimeStart(), userReq.getCreateTimeEnd()));
                predicateList.add(cb.equal(root.get("isAvailable"), true));
                predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
                query.where(cb.and(predicateList.toArray(new Predicate[0])));
                return query.getRestriction();
            };
            orders = orderService.findAll(orderSpecification);
            if (orders.size() > 0) {
                for (Order order : orders) {
                    //减去要扣除绩效的单子
                    totalPrice -= order.getTotalPrice();
                }
            }
            //计算销售提成价格
            confluenceDetailResp.setTotalPrice(totalPrice);
            confluenceDetailResp.setRoyalty(MatchUtils.mul(totalPrice, user.getRatio() * 0.01));
            confluenceDetailResps.add(confluenceDetailResp);
        }
        confluenceRespMallPage.setContent(confluenceDetailResps);
        log.debug("返回结果：{}", confluenceRespMallPage);
        return MallResult.buildQueryOk(confluenceRespMallPage);
    }


    /**
     * 查询商品销量(领导)[暂时不支持分页]
     */
    @GetMapping("/manager/products")
    @ApiOperation(value = "查询商品销量（领导）[暂时不支持分页]")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "开始时间", name = "createTimeStart", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(value = "结束时间", name = "createTimeEnd", dataType = "date", paramType = "query", required = true),
    })
    public MallResult<List<ConfluenceDetailResp>> buyer(@ApiIgnore UserReq userReq) {
        log.debug("请求参数：{}", userReq);
        PageRequest pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        if (StringUtils.isNotBlank(userReq.getClause())) {
            pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        }
        //1.查询指定时间的订单
        //1.1.查询每笔订单下的订单详情，取出商品分类保存
        //1.2.分类统计出商品的销量
        //查询会员在指定时间的所有已经完成支付的订单
        Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            //订单状态300-600表示已经支付并未退货
            predicateList.add(cb.between(root.get("orderStatus"), 300, 600));
            predicateList.add(cb.between(root.get("payEndTime"), userReq.getCreateTimeStart(), userReq.getCreateTimeEnd()));
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            return query.getRestriction();
        };
        Page<Order> orderPage = orderService.findAll(orderSpecification, pageRequest);
        Map<String, Double> confluenceDetailMap = new HashMap<>(156);
        orderPage.forEach(order -> {
            //循环订单详情（商品）
            order.getOrderDetails().forEach(orderDetail -> {
                //商品归类计算
                if (!confluenceDetailMap.containsKey(orderDetail.getProduct().getProductTypeName())) {
                    confluenceDetailMap.put(orderDetail.getProduct().getProductTypeName(), 0D);
                }
                confluenceDetailMap.put(orderDetail.getProduct().getProductTypeName(), confluenceDetailMap.get(orderDetail.getProduct().getProductTypeName()) + orderDetail.getSellingPrice() * orderDetail.getProductNum());
            });
        });
        List<ConfluenceDetailResp> confluenceDetailResps = new ArrayList<>();
        confluenceDetailMap.forEach((productName, totalPrice) -> {
            ConfluenceDetailResp confluenceDetailResp = new ConfluenceDetailResp();
            confluenceDetailResp.setName(productName);
            confluenceDetailResp.setTotalPrice(totalPrice);
            confluenceDetailResps.add(confluenceDetailResp);

        });
        log.debug("返回结果：{}", confluenceDetailResps);
        return MallResult.buildQueryOk(confluenceDetailResps);
    }
}
