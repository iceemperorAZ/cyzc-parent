package com.jingliang.mall.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jingliang.mall.bean.Confluence;
import com.jingliang.mall.bean.ConfluenceDetail;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.entity.OrderDetail;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.BuyerReq;
import com.jingliang.mall.req.OrderReq;
import com.jingliang.mall.req.UserReq;
import com.jingliang.mall.resp.ConfluenceDetailResp;
import com.jingliang.mall.resp.ConfluenceResp;
import com.jingliang.mall.service.BuyerService;
import com.jingliang.mall.service.OrderDetailService;
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
    private final OrderDetailService orderDetailService;

    public WechatManageController(UserService userService, BuyerService buyerService, OrderService orderService, OrderDetailService orderDetailService) {
        this.userService = userService;
        this.buyerService = buyerService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
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
        Confluence confluence = new Confluence();
        confluence.setTotalPrice(0L);
        confluence.setRoyalty(0L);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        users.forEach(user -> {
            //查询绑定会员列表
            List<Buyer> buyers = buyerService.findAllBySaleUserId(user.getId());
            //总价（总价-退款）
            AtomicReference<Long> totalPrice = new AtomicReference<>(0L);
            buyers.forEach(buyer -> {
                //查询会员在指定时间的所有已经完成支付的订单
                Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
                    List<Predicate> predicateList = new ArrayList<>();
                    //订单状态300-700表示已经支付
                    predicateList.add(cb.between(root.get("orderStatus"), 300, 600));
                    predicateList.add(cb.between(root.get("payEndTime"), startTime, calendar.getTime()));
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
                    predicateList.add(cb.greaterThanOrEqualTo(root.get("orderStatus"), 700));
                    predicateList.add(cb.between(root.get("updateTime"), startTime, calendar.getTime()));
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
            confluence.setTotalPrice(totalPrice.get() + confluence.getTotalPrice());
            //计算销售提成价格
            confluence.setRoyalty(confluence.getRoyalty() + (long) (user.getRatio() * 0.01 * totalPrice.get()));
        });
        ConfluenceResp confluenceResp = MallBeanMapper.map(confluence, ConfluenceResp.class);
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userReq.getCreateTimeEnd());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Page<User> userPage = userService.findAllUserByPage(pageRequest);
        MallPage<ConfluenceDetailResp> confluenceMallPage = new MallPage<>();
        confluenceMallPage.setContent(new ArrayList<>());
        confluenceMallPage.setFirst(userPage.isFirst());
        confluenceMallPage.setLast(userPage.isLast());
        confluenceMallPage.setPage(userPage.getNumber());
        confluenceMallPage.setPageSize(userPage.getSize());
        confluenceMallPage.setTotalNumber(userPage.getTotalElements());
        confluenceMallPage.setTotalPages(userPage.getTotalPages());
        if (userPage.getContent().size() == 0) {
            return MallResult.buildQueryOk(confluenceMallPage);
        }
        //计算
        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
        userPage.getContent().forEach(user -> {
            ConfluenceDetail confluenceDetail = new ConfluenceDetail();
            confluenceDetail.setId(user.getId());
            confluenceDetail.setName(user.getUserName());
            //查询绑定会员列表
            List<Buyer> buyers = buyerService.findAllBySaleUserId(user.getId());
            Long totalPrice = 0L;
            for (Buyer buyer : buyers) {
                //查询会员在指定时间的所有已经完成支付的订单
                Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
                    List<Predicate> predicateList = new ArrayList<>();
                    //订单状态300-700表示已经支付
                    predicateList.add(cb.between(root.get("orderStatus"), 300, 600));
                    predicateList.add(cb.between(root.get("payEndTime"), userReq.getCreateTimeStart(), calendar.getTime()));
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
                    predicateList.add(cb.between(root.get("updateTime"), userReq.getCreateTimeStart(), calendar.getTime()));
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
            confluenceDetail.setTotalPrice(totalPrice);
            confluenceDetail.setRoyalty((long) (totalPrice * user.getRatio() * 0.01));
            confluenceDetails.add(confluenceDetail);
        });
        confluenceMallPage.setContent(MallBeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class));
        log.debug("返回结果：{}", confluenceDetails);
        return MallResult.buildQueryOk(confluenceMallPage);
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userReq.getCreateTimeEnd());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        User user = userService.findById(userReq.getId());
        //计算绩效
        ConfluenceDetail confluenceDetail = new ConfluenceDetail();
        confluenceDetail.setName(user.getUserName());
        confluenceDetail.setTotalPrice(0L);
        confluenceDetail.setRoyalty(0L);
        //查询绑定会员列表
        List<Buyer> buyers = buyerService.findAllBySaleUserId(user.getId());
        Long totalPrice = 0L;
        for (Buyer buyer : buyers) {
            //查询会员在指定时间的所有已经完成支付的订单
            Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态300-700表示已经支付
                predicateList.add(cb.between(root.get("orderStatus"), 300, 600));
                predicateList.add(cb.between(root.get("payEndTime"), userReq.getCreateTimeStart(), calendar.getTime()));
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
                predicateList.add(cb.between(root.get("updateTime"), userReq.getCreateTimeStart(), calendar.getTime()));
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
        confluenceDetail.setTotalPrice(totalPrice);
        confluenceDetail.setRoyalty((long) (totalPrice * user.getRatio() * 0.01));
        ConfluenceDetailResp confluenceDetailResp = MallBeanMapper.map(confluenceDetail, ConfluenceDetailResp.class);
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userReq.getCreateTimeEnd());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        User user = (User) session.getAttribute(sessionUser);
        //计算绩效
        ConfluenceDetail confluenceDetail = new ConfluenceDetail();
        confluenceDetail.setTotalPrice(0L);
        confluenceDetail.setRoyalty(0L);
        //查询绑定会员列表
        List<Buyer> buyers = buyerService.findAllBySaleUserId(user.getId());
        Long totalPrice = 0L;
        for (Buyer buyer : buyers) {
            //查询会员在指定时间的所有已经完成支付的订单
            Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态300-700表示已经支付
                predicateList.add(cb.between(root.get("orderStatus"), 300, 600));
                predicateList.add(cb.between(root.get("payEndTime"), userReq.getCreateTimeStart(), calendar.getTime()));
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
                predicateList.add(cb.between(root.get("updateTime"), userReq.getCreateTimeStart(), calendar.getTime()));
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
        confluenceDetail.setTotalPrice(totalPrice);
        confluenceDetail.setRoyalty((long) (totalPrice * user.getRatio() * 0.01));
        ConfluenceDetailResp confluenceDetailResp = MallBeanMapper.map(confluenceDetail, ConfluenceDetailResp.class);
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userReq.getCreateTimeEnd());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Page<Buyer> buyerPage = buyerService.findAllBySaleUserId(user.getId(), pageRequest);
        MallPage<ConfluenceDetailResp> confluenceMallPage = new MallPage<>();
        confluenceMallPage.setContent(new ArrayList<>());
        confluenceMallPage.setFirst(buyerPage.isFirst());
        confluenceMallPage.setLast(buyerPage.isLast());
        confluenceMallPage.setPage(buyerPage.getNumber());
        confluenceMallPage.setPageSize(buyerPage.getSize());
        confluenceMallPage.setTotalNumber(buyerPage.getTotalElements());
        confluenceMallPage.setTotalPages(buyerPage.getTotalPages());
        if (buyerPage.getContent().size() == 0) {
            return MallResult.buildQueryOk(confluenceMallPage);
        }
        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
        for (Buyer buyer : buyerPage) {
            ConfluenceDetail confluenceDetail = new ConfluenceDetail();
            confluenceDetail.setId(buyer.getId());
            if (StringUtils.isNotBlank(buyer.getShopName())) {
                confluenceDetail.setName(buyer.getShopName());
            } else {
                confluenceDetail.setName(buyer.getUserName());
            }
            Long totalPrice = 0L;
            //查询会员在指定时间的所有已经完成支付的订单
            Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态300-700表示已经支付
                predicateList.add(cb.between(root.get("orderStatus"), 300, 600));
                predicateList.add(cb.between(root.get("payEndTime"), userReq.getCreateTimeStart(), calendar.getTime()));
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
                predicateList.add(cb.between(root.get("updateTime"), userReq.getCreateTimeStart(), calendar.getTime()));
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
            confluenceDetail.setTotalPrice(totalPrice);
            confluenceDetail.setRoyalty((long) (totalPrice * user.getRatio() * 0.01));
            confluenceDetails.add(confluenceDetail);
        }
        confluenceMallPage.setContent(MallBeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class));
        log.debug("返回结果：{}", confluenceDetails);
        return MallResult.buildQueryOk(confluenceMallPage);
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userReq.getCreateTimeEnd());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Page<Buyer> buyerPage = buyerService.findAllBySaleUserId(userReq.getId(), pageRequest);
        MallPage<ConfluenceDetailResp> confluenceMallPage = new MallPage<>();
        confluenceMallPage.setContent(new ArrayList<>());
        confluenceMallPage.setFirst(buyerPage.isFirst());
        confluenceMallPage.setLast(buyerPage.isLast());
        confluenceMallPage.setPage(buyerPage.getNumber());
        confluenceMallPage.setPageSize(buyerPage.getSize());
        confluenceMallPage.setTotalNumber(buyerPage.getTotalElements());
        confluenceMallPage.setTotalPages(buyerPage.getTotalPages());
        if (buyerPage.getContent().size() == 0) {
            return MallResult.buildQueryOk(confluenceMallPage);
        }
        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
        for (Buyer buyer : buyerPage) {
            ConfluenceDetail confluenceDetail = new ConfluenceDetail();
            confluenceDetail.setId(buyer.getId());
            if (StringUtils.isNotBlank(buyer.getShopName())) {
                confluenceDetail.setName(buyer.getShopName());
            } else {
                confluenceDetail.setName(buyer.getUserName());
            }
            Long totalPrice = 0L;
            //查询会员在指定时间的所有已经完成支付的订单
            Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态300-700表示已经支付
                predicateList.add(cb.between(root.get("orderStatus"), 300, 600));
                predicateList.add(cb.between(root.get("payEndTime"), userReq.getCreateTimeStart(), calendar.getTime()));
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
                predicateList.add(cb.between(root.get("updateTime"), userReq.getCreateTimeStart(), calendar.getTime()));
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
            confluenceDetail.setTotalPrice(totalPrice);
            confluenceDetail.setRoyalty((long) (totalPrice * user.getRatio() * 0.01));
            confluenceDetails.add(confluenceDetail);
        }
        confluenceMallPage.setContent(MallBeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class));
        log.debug("返回结果：{}", confluenceDetails);
        return MallResult.buildQueryOk(confluenceMallPage);
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
    public MallResult<List<ConfluenceDetailResp>> managerProducts(@ApiIgnore UserReq userReq) {
        log.debug("请求参数：{}", userReq);
        PageRequest pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        if (StringUtils.isNotBlank(userReq.getClause())) {
            pageRequest = PageRequest.of(userReq.getPage(), userReq.getPageSize());
        }
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
        Page<Order> orderPage = orderService.findAll(orderSpecification, pageRequest);
        Map<String, Long> confluenceDetailMap = new HashMap<>(156);
        orderPage.forEach(order -> {
            //循环订单详情（商品）
            order.getOrderDetails().forEach(orderDetail -> {
                //商品归类计算
                if (!confluenceDetailMap.containsKey(orderDetail.getProduct().getProductTypeName())) {
                    confluenceDetailMap.put(orderDetail.getProduct().getProductTypeName(), 0L);
                }
                confluenceDetailMap.put(orderDetail.getProduct().getProductTypeName(), confluenceDetailMap.get(orderDetail.getProduct().getProductTypeName()) + orderDetail.getSellingPrice() * orderDetail.getProductNum());
            });
        });
        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
        confluenceDetailMap.forEach((productName, totalPrice) -> {
            ConfluenceDetail confluenceDetail = new ConfluenceDetail();
            confluenceDetail.setName(productName);
            confluenceDetail.setTotalPrice(totalPrice);
            confluenceDetails.add(confluenceDetail);

        });
        List<ConfluenceDetailResp> confluenceDetailResps = MallBeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class);
        log.debug("返回结果：{}", confluenceDetailResps);
        return MallResult.buildQueryOk(confluenceDetailResps);
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
    public MallResult<List<ConfluenceDetailResp>> userBuyerOrdersr(@ApiIgnore BuyerReq buyerReq, @ApiIgnore HttpSession session) {
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
            return query.getRestriction();
        };
        Page<Order> orderPage = orderService.findAll(orderSpecification, pageRequest);
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
        List<ConfluenceDetailResp> confluenceDetailResps = MallBeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class);
        log.debug("返回结果：{}", confluenceDetailResps);
        return MallResult.buildQueryOk(confluenceDetailResps);
    }

    //查询商户下的订单详情

    /**
     * 查询商户下的订单详情（销售）
     */
    @GetMapping("/user/buyer/order/orderDetail")
    @ApiOperation(value = "查询商户下的订单详情（销售）")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商户Id", name = "id", dataType = "long", paramType = "query", required = true),
    })
    public MallResult<List<ConfluenceDetailResp>> userBuyerOrderDetail(@ApiIgnore OrderReq orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq);
        User user = (User) session.getAttribute(sessionUser);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(orderReq.getCreateTimeEnd());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        //1.查询指定时间的订单
        //1.1.查询每笔订单下的订单详情，取出商品分类保存
        //1.2.分类统计出商品的销量
        //查询指定时间的所有已经完成支付的订单
        Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            //订单状态300-600表示已经支付并未退货
            predicateList.add(cb.between(root.get("orderStatus"), 300, 600));
            predicateList.add(cb.between(root.get("payEndTime"), orderReq.getCreateTimeStart(), calendar.getTime()));
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            predicateList.add(cb.equal(root.get("buyerId"), orderReq.getId()));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            return query.getRestriction();
        };
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderReq.getId());
        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
        orderDetails.forEach(orderDetail -> {
            ConfluenceDetail confluenceDetail = new ConfluenceDetail();
            confluenceDetail.setId(orderDetail.getId());
            confluenceDetail.setName(orderDetail.getOrderNo());
            confluenceDetail.setTotalPrice(orderDetail.getSellingPrice() * orderDetail.getProductNum());
            //每笔订单的提成
            confluenceDetail.setRoyalty((long) (orderDetail.getSellingPrice() * orderDetail.getProductNum() * user.getRatio() * 0.01));
            confluenceDetails.add(confluenceDetail);
        });
        List<ConfluenceDetailResp> confluenceDetailResps = MallBeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class);
        log.debug("返回结果：{}", confluenceDetailResps);
        return MallResult.buildQueryOk(confluenceDetailResps);
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
    public MallResult<List<ConfluenceDetailResp>> userBuyerProducts(@ApiIgnore BuyerReq buyerReq) {
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
        orderPage.forEach(order -> {
            //循环订单详情（商品）
            order.getOrderDetails().forEach(orderDetail -> {
                //商品归类计算
                if (!confluenceDetailMap.containsKey(orderDetail.getProduct().getProductTypeName())) {
                    confluenceDetailMap.put(orderDetail.getProduct().getProductTypeName(), 0L);
                }
                confluenceDetailMap.put(orderDetail.getProduct().getProductTypeName(), confluenceDetailMap.get(orderDetail.getProduct().getProductTypeName()) + orderDetail.getSellingPrice() * orderDetail.getProductNum());
            });
        });
        List<ConfluenceDetail> confluenceDetails = new ArrayList<>();
        confluenceDetailMap.forEach((productName, totalPrice) -> {
            ConfluenceDetail confluenceDetail = new ConfluenceDetail();
            confluenceDetail.setName(productName);
            confluenceDetail.setTotalPrice(totalPrice);
            confluenceDetails.add(confluenceDetail);

        });
        List<ConfluenceDetailResp> confluenceDetailResps = MallBeanMapper.mapList(confluenceDetails, ConfluenceDetailResp.class);
        log.debug("返回结果：{}", confluenceDetailResps);
        return MallResult.buildQueryOk(confluenceDetailResps);
    }
}
