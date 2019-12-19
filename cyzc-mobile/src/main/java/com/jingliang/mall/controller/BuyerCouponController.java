package com.jingliang.mall.controller;

import com.jingliang.mall.amqp.producer.RabbitProducer;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.BuyerCoupon;
import com.jingliang.mall.entity.Coupon;
import com.jingliang.mall.entity.ProductType;
import com.jingliang.mall.req.BuyerCouponReq;
import com.jingliang.mall.resp.BuyerCouponResp;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.BuyerCouponService;
import com.jingliang.mall.service.BuyerService;
import com.jingliang.mall.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
@Api(tags = "用户优惠券")
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

    public BuyerCouponController(BuyerCouponService buyerCouponService, CouponService couponService, BuyerService buyerService, RedisService redisService, RabbitProducer rabbitProducer) {
        this.buyerCouponService = buyerCouponService;
        this.couponService = couponService;
        this.buyerService = buyerService;
        this.redisService = redisService;
        this.rabbitProducer = rabbitProducer;
    }

    /**
     * 领取优惠券
     */
    @ApiOperation(value = "领取优惠券")
    @PostMapping("/save")
    public MallResult<BuyerCouponResp> add(@RequestBody BuyerCouponReq buyerCouponReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerCouponReq);
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        if (buyerCouponService.countByCouponId(buyer.getId(), buyerCouponReq.getCouponId()) > 0) {
            return MallResult.build(MallConstant.COUPON_FAIL, MallConstant.TEXT_COUPON_RECEIVE_FAIL);
        }
        Coupon coupon = couponService.findById(buyerCouponReq.getCouponId());
        if (Objects.isNull(coupon)) {
            return MallResult.build(MallConstant.COUPON_FAIL, MallConstant.TEXT_COUPON_INVALID_FAIL);
        }
        //判断redis中的优惠券数量是否够
        Long decrement = redisService.couponDecrement(coupon.getId() + "", coupon.getReceiveNum());
        if (decrement < 0 && decrement + coupon.getReceiveNum() < 0) {
            redisService.couponIncrement(coupon.getId() + "", coupon.getReceiveNum());
            return MallResult.build(MallConstant.COUPON_FAIL, MallConstant.TEXT_COUPON_ROB_FAIL);
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
        BuyerCoupon buyerCoupon = MallBeanMapper.map(coupon, BuyerCoupon.class);
        assert buyerCoupon != null;
        buyerCoupon.setCouponId(coupon.getId());
        buyerCoupon.setId(null);
        buyerCoupon.setBuyerId(buyer.getId());
        buyerCoupon.setCreateTime(new Date());
        buyerCoupon.setCreateUserId(-1L);
        buyerCoupon.setCreateUser("系统");
        buyerCoupon = buyerCouponService.save(buyerCoupon);
        //通过消息异步减优惠券数量
        coupon.setResidueNumber(-1);
        rabbitProducer.sendCoupon(coupon);
        BuyerCouponResp buyerCouponResp = MallBeanMapper.map(buyerCoupon, BuyerCouponResp.class);
        log.debug("返回结果：{}", buyerCouponResp);
        return MallResult.buildSaveOk(buyerCouponResp);
    }

    /**
     * 分页查询所有领取优惠券
     */
    @ApiOperation(value = "分页查询所有领取优惠券")
    @GetMapping("/page/all")
    public MallResult<MallPage<BuyerCouponResp>> pageAll(BuyerCouponReq buyerCouponReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerCouponReq);
        PageRequest pageRequest = PageRequest.of(buyerCouponReq.getPage(), buyerCouponReq.getPageSize());
        if (StringUtils.isNotBlank(buyerCouponReq.getClause())) {
            pageRequest = PageRequest.of(buyerCouponReq.getPage(), buyerCouponReq.getPageSize(), Sort.by(MallUtils.separateOrder(buyerCouponReq.getClause())));
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
        MallPage<BuyerCouponResp> buyerCouponRespMallPage = MallUtils.toMallPage(buyerCouponPage, BuyerCouponResp.class);
        log.debug("返回结果：{}", buyerCouponRespMallPage);
        return MallResult.buildQueryOk(buyerCouponRespMallPage);
    }

    /**
     * 查询所有领取优惠券，按商品分类分组返回
     * @return
     */
    @ApiOperation(value = "查询所有领取优惠券，按商品分类分组返回")
    @GetMapping("/group/all")
    public MallResult<List<Map<String, Object>>> groupAll(BuyerCouponReq buyerCouponReq, @ApiIgnore HttpSession session) {
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
        List<BuyerCoupon> buyerCouponList = buyerCouponService.findAll(buyerCouponSpecification);
        Map<ProductType, List<BuyerCoupon>> collect = buyerCouponList.stream().collect(Collectors.groupingBy(BuyerCoupon::getProductType));
        List<Map<String,Object>> list = new ArrayList<>();
        for (Map.Entry<ProductType, List<BuyerCoupon>> entry : collect.entrySet()) {
            Map<String,Object> map = new HashMap<>(2);
            map.put("productTypeId",entry.getKey().getId());
            map.put("productTypeName",entry.getKey().getProductTypeName());
            map.put("data",entry.getValue());
            list.add(map);
        }
        return MallResult.buildQueryOk(list);
    }
}