package com.jingliang.mall.controller;

import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiIgnore;
import com.citrsw.annatation.ApiOperation;
import com.jingliang.mall.amqp.producer.RabbitProducer;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.*;
import com.jingliang.mall.req.BuyerCouponReq;
import com.jingliang.mall.resp.BuyerCouponResp;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户优惠券Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 15:21:18
 */
@RestController
@Slf4j
@RequestMapping("/front/buyerCoupon")
@Api(description = "用户优惠券")
public class BuyerCouponController {
    /**
     * session用户Key
     */
    @Value("${session.buyer.key}")
    private String sessionBuyer;
    private final BuyerCouponService buyerCouponService;
    private final CouponService couponService;
    private final BuyerService buyerService;
    private final RedisService redisService;
    private final RabbitProducer rabbitProducer;
    private final ProductService productService;
    private final CartService cartService;

    public BuyerCouponController(BuyerCouponService buyerCouponService, CouponService couponService, BuyerService buyerService, RedisService redisService, RabbitProducer rabbitProducer, ProductService productService, CartService cartService) {
        this.buyerCouponService = buyerCouponService;
        this.couponService = couponService;
        this.buyerService = buyerService;
        this.redisService = redisService;
        this.rabbitProducer = rabbitProducer;
        this.productService = productService;
        this.cartService = cartService;
    }

    /**
     * 领取优惠券
     */
    @ApiOperation(description = "领取优惠券")
    @PostMapping("/save")
    public Result<BuyerCouponResp> add(@RequestBody BuyerCouponReq buyerCouponReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerCouponReq);
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        if (buyerCouponService.countByCouponId(buyer.getId(), buyerCouponReq.getCouponId()) > 0) {
            return Result.build(Msg.COUPON_FAIL, Msg.TEXT_COUPON_RECEIVE_FAIL);
        }
        Coupon coupon = couponService.findById(buyerCouponReq.getCouponId());
        if (Objects.isNull(coupon)) {
            return Result.build(Msg.COUPON_FAIL, Msg.TEXT_COUPON_INVALID_FAIL);
        }
        Integer residueNumber = coupon.getResidueNumber();
        Integer receiveNum = coupon.getReceiveNum();
        coupon.setReceiveNum(Math.min(residueNumber, receiveNum));
        //判断redis中的优惠券数量是否够
        Long decrement = redisService.couponDecrement(coupon.getId() + "", coupon.getReceiveNum());
        if (decrement < 0 && decrement + coupon.getReceiveNum() < 0) {
            redisService.couponIncrement(coupon.getId() + "", coupon.getReceiveNum());
            return Result.build(Msg.COUPON_FAIL, Msg.TEXT_COUPON_ROB_FAIL);
        }

        //1.判断是否是新用户券
        //2.是则比较是否已经把新用户券领完
        //3.如果领完则将新用户状态改为false
        if (Objects.equals(coupon.getCouponType(), 300)) {
            //查询新用户券
            List<Coupon> coupons = couponService.findAllByCouponType(300);
            List<Long> ids = coupons.stream().map(Coupon::getId).collect(Collectors.toList());
            //去掉当前这条后统计，因为这一条还没领
            ids.remove(coupon.getId());
            //查询已经领取了的新用户券
            Integer count = buyerCouponService.countAllByBuyerIdAndCouponIds(buyer.getId(), ids);
            if (Objects.equals(ids.size(), count)) {
                buyer.setIsNew(false);
                buyerService.save(buyer);
            }
        }
        BuyerCoupon buyerCoupon = BeanMapper.map(coupon, BuyerCoupon.class);
        assert buyerCoupon != null;
        buyerCoupon.setCouponId(coupon.getId());
        buyerCoupon.setId(null);
        buyerCoupon.setProductType(null);
        buyerCoupon.setBuyerId(buyer.getId());
        buyerCoupon.setCreateTime(new Date());
        buyerCoupon.setCreateUserId(-1L);
        buyerCoupon.setCreateUser("系统");
        buyerCoupon = buyerCouponService.save(buyerCoupon);
        //通过消息异步减优惠券数量
        coupon.setResidueNumber(-buyerCoupon.getReceiveNum());
        rabbitProducer.sendCoupon(coupon);
        BuyerCouponResp buyerCouponResp = BeanMapper.map(buyerCoupon, BuyerCouponResp.class);
        log.debug("返回结果：{}", buyerCouponResp);
        return Result.buildSaveOk(buyerCouponResp);
    }

    /**
     * 分页查询所有领取优惠券
     */
    @ApiOperation(description = "分页查询所有领取优惠券")
    @GetMapping("/page/all")
    public Result<MallPage<BuyerCouponResp>> pageAll(BuyerCouponReq buyerCouponReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerCouponReq);
        PageRequest pageRequest = PageRequest.of(buyerCouponReq.getPage(), buyerCouponReq.getPageSize());
        if (StringUtils.isNotBlank(buyerCouponReq.getClause())) {
            pageRequest = PageRequest.of(buyerCouponReq.getPage(), buyerCouponReq.getPageSize(), Sort.by(MUtils.separateOrder(buyerCouponReq.getClause())));
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Specification<BuyerCoupon> buyerCouponSpecification = (Specification<BuyerCoupon>) (root, query, cb) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
            andPredicateList.add(cb.equal(root.get("isAvailable"), true));
            List<Predicate> orPredicateList = new ArrayList<>();
            if (Objects.nonNull(buyerCouponReq.getProductTypeIds()) && !buyerCouponReq.getProductTypeIds().isEmpty()) {
                for (Long productTypeId : buyerCouponReq.getProductTypeIds()) {
                    orPredicateList.add(cb.equal(root.get("productTypeId"), productTypeId));
                }
            }
            if (Objects.nonNull(buyerCouponReq.getStatus())) {
                Date date = new Date();
                switch (buyerCouponReq.getStatus()) {
                    case 100:
                        andPredicateList.add(cb.greaterThan(root.get("receiveNum"), 0));
                        andPredicateList.add(cb.greaterThan(root.get("expirationTime"), date));
                        break;
                    case 200:
                        andPredicateList.add(cb.lessThanOrEqualTo(root.get("receiveNum"), 0));
                        break;
                    default:
                        andPredicateList.add(cb.lessThanOrEqualTo(root.get("expirationTime"), date));
                }
            }
            Predicate andPredicate = cb.and(andPredicateList.toArray(new Predicate[0]));
            if (orPredicateList.size() > 0) {
                Predicate orPredicate = cb.or(orPredicateList.toArray(new Predicate[0]));
                query.where(andPredicate, orPredicate);
            } else {
                query.where(andPredicate);
            }
            query.orderBy(cb.asc(root.get("expirationTime")));
            return query.getRestriction();
        };
        Page<BuyerCoupon> buyerCouponPage = buyerCouponService.findAll(buyerCouponSpecification, pageRequest);
        MallPage<BuyerCouponResp> buyerCouponRespMallPage = MUtils.toMallPage(buyerCouponPage, BuyerCouponResp.class);
        log.debug("返回结果：{}", buyerCouponRespMallPage);
        return Result.buildQueryOk(buyerCouponRespMallPage);
    }

    /**
     * 查询所有领取优惠券，按商品分类分组返回
     *
     * @return
     */
    @ApiOperation(description = "查询所有领取优惠券，按商品分类分组返回")
    @GetMapping("/group/all")
    public Result<List<Map<String, Object>>> groupAll(BuyerCouponReq buyerCouponReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerCouponReq);
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Specification<BuyerCoupon> buyerCouponSpecification = (Specification<BuyerCoupon>) (root, query, cb) -> {
            List<Predicate> andPredicateList = new ArrayList<>();
            andPredicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
            andPredicateList.add(cb.equal(root.get("isAvailable"), true));
            List<Predicate> orPredicateList = new ArrayList<>();
            if (Objects.nonNull(buyerCouponReq.getProductTypeIds()) && !buyerCouponReq.getProductTypeIds().isEmpty()) {
                for (Long productTypeId : buyerCouponReq.getProductTypeIds()) {
                    orPredicateList.add(cb.equal(root.get("productTypeId"), productTypeId));
                }
            }
            Date date = new Date();
            andPredicateList.add(cb.greaterThan(root.get("receiveNum"), 0));
            andPredicateList.add(cb.lessThanOrEqualTo(root.get("startTime"), date));
            andPredicateList.add(cb.greaterThan(root.get("expirationTime"), date));
            Predicate andPredicate = cb.and(andPredicateList.toArray(new Predicate[0]));
            if (orPredicateList.size() > 0) {
                Predicate orPredicate = cb.or(orPredicateList.toArray(new Predicate[0]));
                query.where(andPredicate, orPredicate);
            } else {
                query.where(andPredicate);
            }
            query.orderBy(cb.asc(root.get("expirationTime")));
            return query.getRestriction();
        };
        List<BuyerCoupon> buyerCouponList = buyerCouponService.findAll(buyerCouponSpecification);
        Map<ProductType, List<BuyerCoupon>> collect = buyerCouponList.stream().collect(Collectors.groupingBy(BuyerCoupon::getProductType));
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<ProductType, List<BuyerCoupon>> entry : collect.entrySet()) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("productTypeId", entry.getKey().getId());
            map.put("productTypeName", entry.getKey().getProductTypeName());
            map.put("data", BeanMapper.mapList(entry.getValue(), BuyerCouponResp.class));
            list.add(map);
        }
        return Result.buildQueryOk(list);
    }

    /**
     * 查询可以使用的优惠券
     *
     * @param ids
     * @param session
     * @return
     */
    @ApiOperation(description = "查询可以使用的优惠券")
    @GetMapping("/toUseCoupon")
    public Result<List<BuyerCouponResp>> toUseCoupon(@RequestParam(value = "ids") List<Long> ids, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", ids);
        Set<BuyerCouponResp> buyerCouponRespSet = new HashSet<>();
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        for (Long id : ids) {
            //通过商户id和主键id查询购物车
            Cart cart = cartService.findById(id);
            //获得满减金额
            long price = cart.getProductNum() * productService.findAllById(cart.getProductId()).getSellingPrice();
            //查询商户拥有的所有未过期的优惠券
            List<BuyerCoupon> buyerCoupons = buyerCouponService.findAllbyBuyerIdAndTime(buyer.getId());
            for (BuyerCoupon buyerCoupon : buyerCoupons) {
                BuyerCouponResp buyerCouponResp = BeanMapper.map(buyerCoupon, BuyerCouponResp.class);
                assert buyerCouponResp != null;
                //判断是否满足满减金额
                if (price >= buyerCoupon.getFullDecrement()) {
                    //如果是全程通用券，存入set
                    if (buyerCoupon.getProductTypeId() == -1) {
                        buyerCouponResp.setWhetherToUse(true);
                        buyerCouponRespSet.add(buyerCouponResp);
                    } else if (Objects.equals(buyerCoupon.getProductTypeId(), cart.getProductTypeId())) {
                        //如果是分类券，存入set
                        buyerCouponResp.setWhetherToUse(true);
                        buyerCouponRespSet.add(buyerCouponResp);
                    }
                }
            }
        }
        List<BuyerCouponResp> buyerCouponResps = new ArrayList<>(buyerCouponRespSet);
        //对list集合根据优惠金额进行倒序排序
        buyerCouponResps = buyerCouponResps.stream().sorted((o1, o2) -> -o1.getPreferentialPrice().compareTo(o2.getPreferentialPrice())).collect(Collectors.toList());
        log.debug("返回结果：{}", buyerCouponResps);
        return Result.buildQueryOk(buyerCouponResps);
    }
}