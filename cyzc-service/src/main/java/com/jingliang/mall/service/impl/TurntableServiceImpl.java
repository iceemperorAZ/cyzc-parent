package com.jingliang.mall.service.impl;

import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.*;
import com.jingliang.mall.exception.TurntableException;
import com.jingliang.mall.repository.*;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.BuyerAddressService;
import com.jingliang.mall.service.ConfigService;
import com.jingliang.mall.service.TurntableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 转盘ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@Service
@Slf4j
public class TurntableServiceImpl implements TurntableService {

    private final TurntableRepository turntableRepository;
    private final BuyerRepository buyerRepository;
    private final TurntableDetailRepository turntableDetailRepository;
    private final TurntableLogRepository turntableLogRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ConfigRepository configRepository;
    private final RedisService redisService;
    private final BuyerAddressService buyerAddressService;
    private final ConfigService configService;
    private final SkuRepository skuRepository;
    private final OrderDetailRepository orderDetailRepository;

    public TurntableServiceImpl(TurntableRepository turntableRepository, BuyerRepository buyerRepository, TurntableDetailRepository turntableDetailRepository, TurntableLogRepository turntableLogRepository, ProductRepository productRepository, OrderRepository orderRepository, ConfigRepository configRepository, RedisService redisService, BuyerAddressService buyerAddressService, ConfigService configService, SkuRepository skuRepository, OrderDetailRepository orderDetailRepository) {
        this.turntableRepository = turntableRepository;
        this.buyerRepository = buyerRepository;
        this.turntableDetailRepository = turntableDetailRepository;
        this.turntableLogRepository = turntableLogRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.configRepository = configRepository;
        this.redisService = redisService;
        this.buyerAddressService = buyerAddressService;
        this.configService = configService;
        this.skuRepository = skuRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public Turntable save(Turntable turntable) {
        return turntableRepository.save(turntable);
    }

    @Override
    public Turntable findById(Long id) {
        return turntableRepository.findAllByIdAndIsAvailable(id, true);
    }

    @Override
    public List<Turntable> findAll() {
        return turntableRepository.findAllByIsAvailableOrderByGoldAsc(true);
    }

    @Override
    public void delete(Long id, Long userId) {
        Turntable turntable = turntableRepository.getOne(id);
        turntable.setIsAvailable(false);
        turntable.setUpdateTime(new Date());
        turntable.setUpdateUserId(userId);
        turntableRepository.save(turntable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TurntableDetail extract(Long id, Long buyerId) {
        Turntable turntable = turntableRepository.findAllByIdAndIsAvailable(id, true);
        Buyer buyer = buyerRepository.findAllByIdAndIsAvailable(buyerId, true);
        Integer gold = buyer.getGold();
        //获取转盘所需要的金币数
        if (gold < turntable.getGold()) {
            //金币不够不能抽奖
            throw new TurntableException("金币数量不足！");
        }
        List<TurntableDetail> turntableDetails = turntableDetailRepository.findAllByTurntableIdAndIsAvailable(id, true);
        Map<Long, TurntableDetail> detailMap = turntableDetails.stream().parallel().collect(Collectors.toMap(TurntableDetail::getId, turntableDetail -> turntableDetail));
        TurntableDetail turntableDetail1 = MallUtils.weightRandom(detailMap);
        //获取转盘所需要的金币数
        if (turntableDetail1 == null) {
            //金币不够不能抽奖
            throw new TurntableException("奖品已被抽完！");
        }
        //把抽到的商品数量减1
        turntableDetail1.setPrizeNum(turntableDetail1.getPrizeNum() - 1);
        turntableDetailRepository.save(turntableDetail1);
        //减少用户金币
        buyer.setGold(buyer.getGold() - turntable.getGold());
        buyerRepository.saveAndFlush(buyer);
        //记录抽奖日志
        TurntableLog turntableLog = new TurntableLog();
        turntableLog.setBuyerId(buyerId);
        turntableLog.setCreateTime(new Date());
        turntableLog.setIsAvailable(true);
        turntableLog.setMsg("获得" + turntableDetail1.getPrizeName() + "x" + turntableDetail1.getBaseNum() + ".");
        turntableLogRepository.saveAndFlush(turntableLog);
        //处理抽到的奖品
        Integer type = turntableDetail1.getType();
        //判断奖品的类型
        switch (type) {
            case 200:
                //金币
                buyer.setGold(buyer.getGold() + turntableDetail1.getBaseNum());
                break;
            case 300:
                //返利次数
                buyer.setOrderSpecificNum(buyer.getOrderSpecificNum() + turntableDetail1.getBaseNum());
                break;
            case 400:
                //商品
                //生成订单
                Long prizeId = turntableDetail1.getPrizeId();
                Product product = productRepository.getOne(prizeId);
                //创建订单
                Order order = new Order();
                order.setOrderNo(redisService.getOrderNo());
                order.setBuyerId(buyerId);
                BuyerAddress address = buyerAddressService.findDefaultAddrByBuyerId(buyerId);
                if (address == null) {
                    //没有默认地址
                    throw new TurntableException("请先配置默认地址！");
                }
                order.setDetailAddress(address.getProvince().getName() + "/" + address.getCity().getName() + "/" + address.getArea().getName() + "/" + address.getDetailedAddress());
                order.setReceiverName(address.getConsigneeName());
                order.setReceiverPhone(address.getPhone());
                order.setTotalPrice(0L);
                order.setPayableFee(0L);
                order.setPreferentialFee(0L);
                order.setProductNum(turntableDetail1.getBaseNum());
                //计算运费
                Config config = configService.findByCode("200");
                order.setDeliverFee((long) (Double.parseDouble(config.getConfigValues()) * 100));
                order.setPayableFee((order.getPayableFee() + (long) (Double.parseDouble(config.getConfigValues()) * 100)));
                order.setPayWay(200);
                order.setOrderStatus(300);
                //订单预计送达时间
                //1.真实库存有值，则送达时间T+1
                //2.真实库存无值，则送达时间从配置表获取
                Calendar instance = Calendar.getInstance();
                instance.setTime(new Date());
                //捕获异常，防止填写错误，填写格式错误则默认延时3天
                //捕获异常，防止填写错误，填写格式错误则默认延时3天
                Sku sku = skuRepository.findFirstByProductIdAndIsAvailable(prizeId, true);
                if (sku.getSkuRealityNum() < turntableDetail1.getBaseNum()) {
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
                order.setCreateTime(new Date());
                order.setNote("通过抽奖形式生成的订单");
                order.setIsAvailable(true);
                orderRepository.saveAndFlush(order);
                //创建订单详情
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderNo(order.getOrderNo());
                orderDetail.setOrderId(order.getId());
                orderDetail.setProductId(prizeId);
                orderDetail.setSellingPrice(-0L);
                orderDetail.setProductNum(turntableDetail1.getBaseNum());
                orderDetail.setCreateTime(new Date());
                orderDetail.setIsAvailable(true);
                orderDetailRepository.saveAndFlush(orderDetail);
                break;
            default:
        }


        return turntableDetail1;
    }
}