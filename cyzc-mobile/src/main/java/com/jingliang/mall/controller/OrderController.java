package com.jingliang.mall.controller;

import com.jingliang.mall.amqp.producer.RabbitProducer;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.*;
import com.jingliang.mall.req.OrderReq;
import com.jingliang.mall.resp.OrderDetailResp;
import com.jingliang.mall.resp.OrderResp;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.*;
import com.jingliang.mall.wx.service.WechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * 订单表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
@RequestMapping("/front/order")
@Api(tags = "订单")
@RestController
@Slf4j
public class OrderController {
    @Value("${session.buyer.key}")
    private String sessionBuyer;
    @Value("${product.sku.init.invented.num}")
    private Integer productSkuInitInventedNum;
    @Value("${coupon.use.limit}")
    private Integer couponUseLimit;

    private final OrderService orderService;
    private final ProductService productService;
    private final BuyerCouponService buyerCouponService;
    private final RedisService redisService;
    private final BuyerService buyerService;
    private final WechatService wechatService;
    private final RabbitProducer rabbitProducer;
    private final BuyerCouponLimitService buyerCouponLimitService;
    private final ConfigService configService;
    private final GoldLogService goldLogService;
    private final UserService userService;
    private final BuyerAddressService buyerAddressService;
    private final RegionService regionService;

    public OrderController(RedisTemplate<String, Object> redisTemplate, OrderService orderService,
                           ProductService productService, BuyerCouponService buyerCouponService, RedisService redisService,
                           BuyerService buyerService, WechatService wechatService, RabbitProducer rabbitProducer,
                           ConfigService configService, BuyerCouponLimitService buyerCouponLimitService, GoldLogService goldLogService, UserService userService,
                           BuyerAddressService buyerAddressService, RegionService regionService) {
        this.orderService = orderService;
        this.productService = productService;
        this.buyerCouponService = buyerCouponService;
        this.redisService = redisService;
        this.buyerService = buyerService;
        this.wechatService = wechatService;
        this.rabbitProducer = rabbitProducer;
        this.configService = configService;
        this.buyerCouponLimitService = buyerCouponLimitService;
        this.goldLogService = goldLogService;
        this.userService = userService;
        this.buyerAddressService = buyerAddressService;
        this.regionService = regionService;
    }


    /**
     * 创建订单
     */
    @ApiOperation(value = "创建订单")
    @PostMapping("/save")
    public Result<Map<String, String>> save(@RequestBody OrderReq orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq);
        if (Objects.isNull(orderReq.getPayWay())) {
            return Result.buildParamFail();
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        MUtils.addDateAndBuyer(orderReq, buyer);

        Order order = BeanMapper.map(orderReq, Order.class);
        assert order != null;
        order.setPayableFee(0L);
        order.setTotalPrice(0L);
        order.setDeliverFee(0L);
        int productNum = 0;
        List<OrderDetail> orderDetails = new ArrayList<>();
        Date date = new Date();
        //返币
        Long drinksPrice = 0L;
        List<OrderDetail> drinksDetails = new ArrayList<>();
        //是否有真实库存
        boolean hasSku = true;
        Map<Long, Long> productPriceMap = new HashMap<>(100);
        //计算商品总价
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            orderDetail.setId(null);
            //查询上架商品
            Product product = productService.findShowProductById(orderDetail.getProductId());
            if (Objects.isNull(product)) {
                for (OrderDetail detailReq : orderDetails) {
                    //如果本次有已经下架的商品就把减掉的库存加回去，并返回库存商品已下架
                    redisService.skuLineIncrement(String.valueOf(detailReq.getProductId()), detailReq.getProductNum());
                    redisService.decrement("PRODUCT-BUYER-LIMIT-" + buyer.getId() + detailReq.getProductId() + "", detailReq.getProductNum());
                }
                return Result.build(Msg.ORDER_FAIL, Msg.TEXT_ORDER_PRODUCT_FAIL);
            }
            //判断商品是否已经售空
            if (product.getIsSoonShow()) {
                for (OrderDetail detailReq : orderDetails) {
                    //如果本次有已经售空的商品就把减掉的库存加回去，并返回库存商品已售空
                    redisService.skuLineIncrement(String.valueOf(detailReq.getProductId()), detailReq.getProductNum());
                    redisService.decrement("PRODUCT-BUYER-LIMIT-" + buyer.getId() + detailReq.getProductId() + "", detailReq.getProductNum());
                }
                return Result.build(Msg.ORDER_FAIL, product.getProductName() + "已抢空");
            }
            //判断商品已经抢空
            if (product.getIsNew()) {
                for (OrderDetail detailReq : orderDetails) {
                    //如果本次有已经售空的商品就把减掉的库存加回去，并返回库存商品已售空
                    redisService.skuLineIncrement(String.valueOf(detailReq.getProductId()), detailReq.getProductNum());
                    redisService.decrement("PRODUCT-BUYER-LIMIT-" + buyer.getId() + detailReq.getProductId() + "", detailReq.getProductNum());
                }
                return Result.build(Msg.ORDER_FAIL, product.getProductName() + "已售罄");
            }

            //售价[商品价格*数量]
            long sellingPrice = product.getSellingPrice() * orderDetail.getProductNum();
            order.setTotalPrice(order.getTotalPrice() + sellingPrice);
            order.setPayableFee(order.getPayableFee() + sellingPrice);
            orderDetail.setSellingPrice(product.getSellingPrice());
            orderDetail.setCreateTime(date);
            orderDetail.setIsAvailable(true);
            orderDetails.add(orderDetail);
            //TODO 临时使用商品分类Id进行区分
            if (product.getProductTypeId().equals(2020030121L)) {
                //饮料类的价格不进行累加，单独出来计算
                drinksDetails.add(orderDetail);
                orderDetail.setDifference(product.getSellingPrice() - product.getMarketPrice());
                drinksPrice += sellingPrice;
            }
            //查询已购买数量
            Long increment = redisService.increment("PRODUCT-BUYER-LIMIT-" + buyer.getId() + product.getId() + "", orderDetail.getProductNum());
            //查询线上库存
            Long skuNum = redisService.skuLineDecrement(String.valueOf(product.getId()), orderDetail.getProductNum());
            if (increment.intValue() == orderDetail.getProductNum()) {
                //如果是第一次则进行倒计时（当天12点失效）
                Duration duration = Duration.between(LocalDateTime.now(), LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)));
                redisService.setExpire("PRODUCT-BUYER-LIMIT-" + buyer.getId() + product.getId() + "", duration.toMillis() / 1000);
            }
            //判断商品本次是否满足购买限制
            if (orderDetail.getProductNum() < product.getMinNum()) {
                for (OrderDetail detailReq : orderDetails) {
                    //如果本次有已经售空的商品就把减掉的库存加回去，并返回库存商品已售空
                    redisService.skuLineIncrement(String.valueOf(detailReq.getProductId()), detailReq.getProductNum());
                    redisService.decrement("PRODUCT-BUYER-LIMIT-" + buyer.getId() + detailReq.getProductId() + "", detailReq.getProductNum());
                }
                return Result.build(Msg.ORDER_FAIL, product.getProductName() + "商品最低" + product.getMinNum() + product.getUnit() + "起购");
            }
            //判断商品本次是否超过购买限制
            if (orderDetail.getProductNum() > product.getLimitNum()) {
                for (OrderDetail detailReq : orderDetails) {
                    //如果本次有已经售空的商品就把减掉的库存加回去，并返回库存商品已售空
                    redisService.skuLineIncrement(String.valueOf(detailReq.getProductId()), detailReq.getProductNum());
                    redisService.decrement("PRODUCT-BUYER-LIMIT-" + buyer.getId() + detailReq.getProductId() + "", detailReq.getProductNum());
                }
                return Result.build(Msg.ORDER_FAIL, product.getProductName() + "今日限购" + product.getLimitNum() + product.getUnit());
            }
            //判断今天总共是否超过购买限制
            if (increment > product.getLimitNum()) {
                for (OrderDetail detailReq : orderDetails) {
                    //如果本次有已经售空的商品就把减掉的库存加回去，并返回库存商品已售空
                    redisService.skuLineIncrement(String.valueOf(detailReq.getProductId()), detailReq.getProductNum());
                    redisService.decrement("PRODUCT-BUYER-LIMIT-" + buyer.getId() + detailReq.getProductId() + "", detailReq.getProductNum());
                }
                return Result.build(Msg.ORDER_FAIL, product.getProductName() + "超出今日购买上限");
            }
            if (skuNum < productSkuInitInventedNum) {
                hasSku = false;
            }
            if (skuNum < 0) {
                for (OrderDetail detail : orderDetails) {
                    //如果小于库存就把减掉的库存加回去，并返回库存不足的信息
                    redisService.skuLineIncrement(String.valueOf(detail.getProductId()), detail.getProductNum());
                    redisService.decrement("PRODUCT-BUYER-LIMIT-" + buyer.getId() + detail.getProductId() + "", detail.getProductNum());
                }
                return Result.build(Msg.ORDER_FAIL, Msg.TEXT_ORDER_SKU_FAIL);
            }

            if (productPriceMap.containsKey(product.getProductTypeId())) {
                productPriceMap.put(product.getProductTypeId(), productPriceMap.get(product.getProductTypeId()) + product.getSellingPrice() * orderDetail.getProductNum());
            } else {
                productPriceMap.put(product.getProductTypeId(), product.getSellingPrice() * orderDetail.getProductNum());
            }
            productNum += orderDetail.getProductNum();
        }
        order.setProductNum(productNum);
        //是否满足可以下单的订单额度
        Config config = configService.findByCode("300");
        if (order.getTotalPrice() < (long) (Double.parseDouble(config.getConfigValues()) * 100)) {
            for (OrderDetail detail : orderDetails) {
                //如果小于库存就把减掉的库存加回去，并返回库存不足的信息
                redisService.skuLineIncrement(String.valueOf(detail.getProductId()), detail.getProductNum());
                redisService.decrement("PRODUCT-BUYER-LIMIT-" + buyer.getId() + detail.getProductId() + "", detail.getProductNum());
            }
            return Result.build(Msg.ORDER_FAIL, config.getRemark().replace("#price#", (Integer.parseInt(config.getConfigValues())) + ""));
        }
        //是否满足可以下单的订单额度
        config = configService.findByCode("700");
        if (order.getTotalPrice() > (long) (Double.parseDouble(config.getConfigValues()) * 100)) {
            for (OrderDetail detail : orderDetails) {
                //如果小于库存就把减掉的库存加回去，并返回库存不足的信息
                redisService.skuLineIncrement(String.valueOf(detail.getProductId()), detail.getProductNum());
                redisService.decrement("PRODUCT-BUYER-LIMIT-" + buyer.getId() + detail.getProductId() + "", detail.getProductNum());
            }
            return Result.build(Msg.ORDER_FAIL, config.getRemark().replace("#price#", (Integer.parseInt(config.getConfigValues())) + ""));
        }
        //计算赠品
        config = configService.findByCode("600");
        String values = config.getConfigValues();
        if (StringUtils.isNotBlank(values) && !"-".equals(values)) {
            String[] sections = values.split(";");
            String productIds = "";
            Map<Double, String> map = new TreeMap<>();
            for (String section : sections) {
                String[] split = section.split(":");
                map.put(Double.valueOf(split[0]), split[1]);
            }
            map = ((TreeMap<Double, String>) map).descendingMap();
            for (Map.Entry<Double, String> entry : map.entrySet()) {
                if ((order.getTotalPrice() / 100.00) >= entry.getKey()) {
                    productIds = entry.getValue();
                    break;
                }
            }
            if (StringUtils.isNotBlank(productIds)) {
                for (String productId : productIds.split(",")) {
                    String[] split = productId.split("\\|");
                    Product product = productService.findAllById(Long.parseLong(split[1]));
                    //查询线上库存
                    Long skuNum = redisService.skuLineDecrement(String.valueOf(product.getId()), 1);
                    if (skuNum >= 0) {
                        OrderDetail orderDetail = new OrderDetail();
                        orderDetail.setProductNum(Integer.parseInt(split[2]));
                        orderDetail.setSellingPrice(0L);
                        orderDetail.setIsAvailable(true);
                        orderDetail.setCreateTime(new Date());
                        orderDetail.setProductId(product.getId());
                        order.getOrderDetails().add(orderDetail);
                        orderDetails.add(orderDetail);
                    }
                }
            }
        }
        order.setPreferentialFee(0L);
        //计算使用优惠券后的支付价
        //优惠总价
        double preferentialFee = 0D;
        if (Objects.nonNull(orderReq.getCouponIdList()) && orderReq.getCouponIdList().size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (Long couponId : orderReq.getCouponIdList()) {
                BuyerCoupon buyerCoupon = buyerCouponService.findByIdAndBuyerId(couponId, buyer.getId());
                if (Objects.isNull(buyerCoupon) || buyerCoupon.getReceiveNum() <= 0) {
                    for (OrderDetail detail : orderDetails) {
                        //如果小于库存就把减掉的库存加回去，并返回库存不足的信息
                        redisService.skuLineIncrement(String.valueOf(detail.getProductId()), detail.getProductNum());
                        redisService.decrement("PRODUCT-BUYER-LIMIT-" + buyer.getId() + detail.getProductId() + "", detail.getProductNum());
                    }
                    return Result.build(Msg.ORDER_FAIL, Msg.TEXT_ORDER_COUPON_FAIL);
                }
                if (!productPriceMap.containsKey(buyerCoupon.getProductTypeId())) {
                    for (OrderDetail detail : orderDetails) {
                        //如果小于库存就把减掉的库存加回去，并返回库存不足的信息
                        redisService.skuLineIncrement(String.valueOf(detail.getProductId()), detail.getProductNum());
                        redisService.decrement("PRODUCT-BUYER-LIMIT-" + buyer.getId() + detail.getProductId() + "", detail.getProductNum());
                    }
                    return Result.build(Msg.ORDER_FAIL, Msg.TEXT_ORDER_COUPON_FAIL);
                }
                //查询优惠券的限制使用张数
                BuyerCouponLimit buyerCouponLimit = buyerCouponLimitService.findByBuyerIdAndProductTypeId(buyer.getId(), buyerCoupon.getProductTypeId());
                Integer useLimit = couponUseLimit;
                if (Objects.nonNull(buyerCouponLimit)) {
                    useLimit = buyerCouponLimit.getUseLimit();
                }
                int min = Math.min(useLimit, buyerCoupon.getReceiveNum());
                preferentialFee += (productPriceMap.get(buyerCoupon.getProductTypeId()) * buyerCoupon.getPercentage() * 0.01 * min);
                buyerCoupon.setReceiveNum(buyerCoupon.getReceiveNum() - min);
                //优惠券数量减少
                buyerCouponService.save(buyerCoupon);
                builder.append(buyerCoupon.getId()).append("|").append(min).append(",");
            }
            order.setCouponIds(builder.substring(0, builder.length() - 1));
        }
        //总优惠金额进行四舍五入
        BigDecimal b = new BigDecimal(Double.toString(preferentialFee));
        order.setPayableFee(order.getPayableFee() - b.setScale(0, BigDecimal.ROUND_HALF_UP).longValue());
        order.setPreferentialFee(order.getPreferentialFee() + b.setScale(0, BigDecimal.ROUND_HALF_UP).longValue());
        //生成订单号sellingPrice
        order.setOrderNo(redisService.getOrderNo());
        order.setOrderStatus(100);
        order.setPayStartTime(date);
        order.setUpdateTime(date);
        Map<String, String> resultMap = new HashMap<>(156);
        if (order.getIsGold() != null && order.getIsGold()) {
            //选择使用金币后
            //扣除金币
            buyer = buyerService.findById(buyer.getId());
            Integer gold = buyer.getGold();
            //金币小于等于订单支付数
            if ((order.getPayableFee()) / 100 >= gold / 10) {
                order.setPayableFee(((order.getPayableFee()) / 10 - gold) * 10);
                order.setGold(gold);
            } else {
                //使用了对应钱数的金币
                order.setGold((order.getPayableFee().intValue() / 10));
                //金币大于订单支付数
                order.setPayableFee(0L);
            }
        }


        //计算运费
        config = configService.findByCode("100");
        if (order.getTotalPrice() >= (long) (Double.parseDouble(config.getConfigValues()) * 100)) {
            order.setDeliverFee(0L);
        } else {
            config = configService.findByCode("200");
            order.setDeliverFee((long) (Double.parseDouble(config.getConfigValues()) * 100));
            order.setPayableFee((order.getPayableFee() + (long) (Double.parseDouble(config.getConfigValues()) * 100)));
        }
        //微信支付
        if (Objects.equals(order.getPayWay(), 100)) {
            if (!order.getPayableFee().equals(0L)) {
                //调起微信预支付
                resultMap = wechatService.payUnifiedOrder(order, buyer.getUniqueId());
                if (Objects.isNull(resultMap)) {
                    //调起支付失败
                    for (OrderDetail detail : orderDetails) {
                        //如果小于库存就把减掉的库存加回去，并返回库存不足的信息
                        redisService.skuLineIncrement(String.valueOf(detail.getProductId()), detail.getProductNum());
                        redisService.decrement("PRODUCT-BUYER-LIMIT-" + buyer.getId() + detail.getProductId() + "", detail.getProductNum());
                    }
                    return Result.build(Msg.ORDER_FAIL, Msg.TEXT_ORDER_FAIL);
                }
            }
        }
        if (order.getPayableFee().equals(0L)) {
            order.setOrderStatus(300);
        }
        //订单预计送达时间
        //1.真实库存有值，则送达时间T+1
        //2.真实库存无值，则送达时间从配置表获取
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        //捕获异常，防止填写错误，填写格式错误则默认延时3天
        if (hasSku) {
            //真实库存不足延迟配送
            config = configService.findByCode("500");
        } else {
            //真实库存不足延迟配送
            config = configService.findByCode("400");
        }
        try {
            instance.add(Calendar.DAY_OF_MONTH, Integer.parseInt(config.getConfigValues()));
        } catch (Exception e) {
            //捕获异常，防止填写错误，填写格式错误则默认延时3天
            instance.add(Calendar.DAY_OF_MONTH, 3);
        }
        order.setExpectedDeliveryTime(instance.getTime());
        //查询出当前商户所属的销售和当前销售所在的分组
        User saleUser = userService.findById(buyer.getSaleUserId());
        //当前分组
        String groupNo = saleUser.getGroupNo();
        order.setGroupNo(groupNo);
        //销售Id
        order.setSaleUserId(buyer.getSaleUserId());
        //当前提成
        order.setRatio(saleUser.getRatio());
        //创建购买者地址类，寻找购买者的所有地址
        BuyerAddress buyerAddress = new BuyerAddress();
        //根据购买者id寻找地址
        buyerAddress = buyerAddressService.findDefaultAddrByBuyerId(buyer.getId());
        //根据购买者地址，往订单中存入相应的地址数据
        order.setDetailAddressProvince(buyerAddress.getProvinceCode());
        order.setDetailAddressCity(buyerAddress.getCityCode());
        order.setDetailAddressArea(buyerAddress.getAreaCode());
        order.setDetailAddressStreet(buyerAddress.getStreetCode());
//        String DetailAddress = regionService.findByCode(buyerAddress.getProvinceCode())
//                .concat("/")
//                .concat(buyerAddress.getCity().getName())
//                .concat("/")
//                .concat(buyerAddress.getArea().getName())
//                .concat("/")
//                .concat(StringUtils.isNotBlank(buyerAddress.getStreetCode())?buyerAddress.getStreetCode():"");
//        order.setDetailAddress(DetailAddress);

        order = orderService.save(order, drinksDetails, drinksPrice);
        rabbitProducer.sendOrderExpireMsg(order);
        if (order.getPayableFee().equals(0L)) {
            return Result.build(Msg.PAY_GOLD_OK, "");
        }
        resultMap.put("id", order.getId() + "");
        resultMap.put("orderNo", order.getOrderNo());
        //订单Id也返回
        OrderResp orderResp = BeanMapper.map(order, OrderResp.class);
        log.debug("微信返回结果二次签名后的返回结果：{}", resultMap);
        log.debug("返回结果：{}", orderResp);
        return Result.buildSaveOk(resultMap);
    }

    /**
     * 取消订单
     */
    @ApiOperation(value = "取消订单")
    @PostMapping("/cancel")
    public Result<OrderResp> cancel(@RequestBody OrderReq orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq);
        if (Objects.isNull(orderReq.getId())) {
            return Result.buildParamFail();
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Order order = orderService.findByIdAndBuyerId(orderReq.getId(), buyer.getId());
        if (Objects.isNull(order)) {
            return Result.build(Msg.ORDER_FAIL, Msg.TEXT_ORDER_NOT_EXIST_FAIL);
        }
        if (order.getOrderStatus() >= 200) {
            return Result.build(Msg.ORDER_FAIL, "订单已经支付成功或取消");
        }
        order.setFinishTime(new Date());
        order.setOrderStatus(200);
        OrderResp orderResp = BeanMapper.map(orderService.update(order), OrderResp.class);
        log.debug("返回结果：{}", orderResp);
        return Result.build(Msg.OK, Msg.TEXT_CANCEL_OK, orderResp);
    }

    /**
     * 订单完成确认
     */
    @ApiOperation(value = "订单完成确认")
    @PostMapping("/confirm")
    public Result<OrderResp> confirm(@RequestBody OrderReq orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq);
        if (Objects.isNull(orderReq.getId())) {
            return Result.buildParamFail();
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Order order = orderService.findByIdAndBuyerId(orderReq.getId(), buyer.getId());
        if (Objects.isNull(order)) {
            return Result.build(Msg.ORDER_FAIL, Msg.TEXT_ORDER_NOT_EXIST_FAIL);
        }
        if (order.getOrderStatus() == 600) {
            return Result.build(Msg.ORDER_FAIL, "订单已经完成确认");
        }
        order.setFinishTime(new Date());
        order.setOrderStatus(600);
        OrderResp orderResp = BeanMapper.map(orderService.update(order), OrderResp.class);
        log.debug("返回结果：{}", orderResp);
        return Result.build(Msg.OK, Msg.TEXT_CONFIRM_OK, orderResp);
    }

    /**
     * 分页查询全部用户订单信息
     */
    @ApiOperation(value = "分页查询全部用户订单信息")
    @GetMapping("/page/all")
    public Result<MallPage<OrderResp>> pageAll(OrderReq orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq);
        PageRequest pageRequest = PageRequest.of(orderReq.getPage(), orderReq.getPageSize());
        if (StringUtils.isNotBlank(orderReq.getClause())) {
            pageRequest = PageRequest.of(orderReq.getPage(), orderReq.getPageSize(), Sort.by(MUtils.separateOrder(orderReq.getClause())));
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (Objects.nonNull(orderReq.getOrderStatus())) {
                if (Objects.equals(orderReq.getOrderStatus(), 400)) {
                    //between前后都包含
                    predicateList.add(cb.between(root.get("orderStatus"), 300, 500));
                } else {
                    predicateList.add(cb.equal(root.get("orderStatus"), orderReq.getOrderStatus()));
                }
            }
            predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("createTime")));
            return query.getRestriction();
        };
        Page<Order> orderPage = orderService.findAll(orderSpecification, pageRequest);
        MallPage<OrderResp> orderRespMallPage = MUtils.toMallPage(orderPage, OrderResp.class);
        log.debug("返回结果：{}", orderRespMallPage);
        return Result.buildQueryOk(orderRespMallPage);
    }

    /**
     * 根据订单总价查询运费金额
     */
    @ApiOperation(value = "根据订单总价查询运费金额")
    @GetMapping("/find/deliverFee")
    public Result<Double> getDeliverFee(Double deliverFee) {
        Config config = configService.findByCode("100");
        if ((long) (deliverFee * 100) >= (long) (Double.parseDouble(config.getConfigValues()) * 100)) {
            return Result.buildQueryOk(0D);
        }
        config = configService.findByCode("200");
        return Result.buildQueryOk(Double.parseDouble(config.getConfigValues()));
    }

    /**
     * 根据订单总价查询赠品
     */
    @ApiOperation(value = "根据订单总价查询赠品")
    @GetMapping("/find/gift")
    public Result<List<OrderDetailResp>> findOrderGift(Double deliverFee) {
        Config config = configService.findByCode("600");
        String values = config.getConfigValues();
        if (StringUtils.isBlank(values) || "-".equals(values)) {
            return Result.buildOk();
        }
        String[] sections = values.split(";");
        String productIds = "";
        Map<Double, String> map = new TreeMap<>();
        for (String section : sections) {
            String[] split = section.split(":");
            map.put(Double.valueOf(split[0]), split[1]);
        }
        map = ((TreeMap<Double, String>) map).descendingMap();
        for (Map.Entry<Double, String> entry : map.entrySet()) {
            if (deliverFee >= entry.getKey()) {
                productIds = entry.getValue();
                break;
            }
        }
        List<OrderDetailResp> orderDetailResps = new ArrayList<>();
        if (StringUtils.isBlank(productIds)) {
            return Result.buildOk();
        }
        for (String productId : productIds.split(",")) {
            String[] split = productId.split("\\|");
            Product product = productService.findAllById(Long.parseLong(split[1]));
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProductNum(Integer.parseInt(split[2]));
            orderDetail.setSellingPrice(0L);
            orderDetail.setProduct(product);
            orderDetail.setProductId(product.getId());
            orderDetailResps.add(BeanMapper.map(orderDetail, OrderDetailResp.class));
        }
        return Result.buildQueryOk(orderDetailResps);
    }

    @GetMapping("/wait/gold")
    public Result<Integer> waitGold(HttpSession session) {
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(cb.between(root.get("orderStatus"), 300, 500));
            predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("createTime")));
            return query.getRestriction();
        };
        //订单完成之后返金币
        //计算返金币比例
        Config config = configService.findByCode("800");
        double percentage = Integer.parseInt(config.getConfigValues()) * 0.01;
        //返的金币数
        //统计所有支付后但未完成的订单
        List<Order> orders = orderService.findAll(orderSpecification);
        int wartGold = 0;
        if (orders.size() > 0) {
            for (Order order : orders) {
                if (order.getReturnGold() == null || order.getReturnGold() <= 0) {
                    continue;
                }
                int gold = order.getReturnGold();
                if (gold == 0) {
                    continue;
                }
                wartGold += gold;
            }
        }
        return Result.buildQueryOk(wartGold);
    }
}