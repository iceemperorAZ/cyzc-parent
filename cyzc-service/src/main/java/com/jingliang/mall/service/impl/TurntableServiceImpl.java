package com.jingliang.mall.service.impl;

import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.Turntable;
import com.jingliang.mall.entity.TurntableDetail;
import com.jingliang.mall.entity.TurntableLog;
import com.jingliang.mall.exception.TurntableException;
import com.jingliang.mall.repository.*;
import com.jingliang.mall.service.TurntableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public TurntableServiceImpl(TurntableRepository turntableRepository, BuyerRepository buyerRepository, TurntableDetailRepository turntableDetailRepository, TurntableLogRepository turntableLogRepository, ProductRepository productRepository, OrderRepository orderRepository, ConfigRepository configRepository) {
        this.turntableRepository = turntableRepository;
        this.buyerRepository = buyerRepository;
        this.turntableDetailRepository = turntableDetailRepository;
        this.turntableLogRepository = turntableLogRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.configRepository = configRepository;
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
        buyerRepository.save(buyer);
        //记录抽奖日志
        TurntableLog turntableLog = new TurntableLog();
        turntableLog.setBuyerId(buyerId);
        turntableLog.setCreateTime(new Date());
        turntableLog.setIsAvailable(true);
        turntableLog.setMsg("获得" + turntableDetail1.getPrizeName() + "x" + turntableDetail1.getBaseNum() + ".");
        turntableLogRepository.save(turntableLog);
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
//                //生成订单
//                Long prizeId = turntableDetail1.getPrizeId();
//                Product product = productRepository.getOne(prizeId);
//                //创建订单
//                Order order = new Order();
//                order.setId();
//                order.setPayNo();
//                order.setOrderNo();
//                order.setBuyerId();
//                order.setDetailAddress();
//                order.setReceiverName();
//                order.setReceiverPhone();
//                order.setTotalPrice();
//                order.setPayableFee();
//                order.setPreferentialFee();
//                order.setCouponIds();
//                order.setProductNum();
//                order.setDeliverFee();
//                order.setPayWay();
//                order.setPayStartTime();
//                order.setPayEndTime();
//                order.setOrderStatus();
//                order.setDeliveryType();
//                order.setCreateTime();
//                order.setFinishTime();
//
//                //订单预计送达时间
//                //1.真实库存有值，则送达时间T+1
//                //2.真实库存无值，则送达时间从配置表获取
//                Calendar instance = Calendar.getInstance();
//                instance.setTime(date);
//                //捕获异常，防止填写错误，填写格式错误则默认延时3天
//                Config config;
//                if (hasSku) {
//                    //真实库存不足延迟配送
//                     config = configRepository.findFirstByCodeAndIsAvailable("500",true);
//                } else {
//                    //真实库存不足延迟配送
//                    config = configRepository.findFirstByCodeAndIsAvailable("400",true);
//                }
//                try {
//                    instance.add(Calendar.DAY_OF_MONTH, Integer.parseInt(config.getConfigValues()));
//                } catch (Exception e) {
//                    //捕获异常，防止填写错误，填写格式错误则默认延时3天
//                    instance.add(Calendar.DAY_OF_MONTH, 3);
//                }
//                order.setExpectedDeliveryTime();
//                order.setNote("通过抽奖形式生成的订单");
//                order.setIsAvailable(true);
//
//
//                //创建订单详情
//                OrderDetail orderDetail = new OrderDetail();
//                orderDetail.setOrderNo(order.getOrderNo());
//                orderDetail.setOrderId(order.getId());
//                orderDetail.setProductId(prizeId);
//                orderDetail.setSellingPrice(-0L);
//                orderDetail.setProductNum(turntableDetail1.getBaseNum());
//                orderDetail.setCreateTime(new Date());
//                orderDetail.setIsAvailable(true);


                // orderRepository;


                break;
            default:
        }


        return turntableDetail1;
    }
}