package com.jingliang.mall.service.impl;

import com.jingliang.mall.bean.ConfluenceDetail;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.repository.BuyerRepository;
import com.jingliang.mall.repository.OrderRepository;
import com.jingliang.mall.service.WechatManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 微信管理服务
 *
 * @author Zhenfeng Li
 * @date 2019-12-12 14:21:42
 */
@Service
@Slf4j
public class WechatManageServiceImpl implements WechatManageService {

    private final BuyerRepository buyerRepository;
    private final OrderRepository orderRepository;

    public WechatManageServiceImpl(BuyerRepository buyerRepository, OrderRepository orderRepository) {
        this.buyerRepository = buyerRepository;
        this.orderRepository = orderRepository;
    }


    @Override
    public ConfluenceDetail userPerformanceSummary(User user, Date startTime, Date endTime) {
        ConfluenceDetail confluence = new ConfluenceDetail();
        //用户Id
        confluence.setId(user.getId());
        //用户名称
        confluence.setName(user.getUserName());
        confluence.setTotalPrice(0L);
        confluence.setRoyalty(0L);
        confluence.setProfit(0L);
        //1.判断开始时间和结束时间是否是同年同月
        //1.1 是则直接查询
        //1.2 否以开始时间的月底为分割时间，分开查询
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startTime);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        int startYear = startCalendar.get(Calendar.YEAR);
        int startMonth = startCalendar.get(Calendar.MONTH);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endTime);
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        int endYear = endCalendar.get(Calendar.YEAR);
        int endMonth = endCalendar.get(Calendar.MONTH);
        if (startYear == endYear && startMonth == endMonth) {
            ConfluenceDetail confluenceDetail = doUserPerformanceSummary(user, startCalendar.getTime(), endCalendar.getTime());
            //总价
            confluence.setTotalPrice(confluenceDetail.getTotalPrice() + confluence.getTotalPrice());
            //计算销售提成价格
            confluence.setRoyalty(confluenceDetail.getRoyalty() + confluence.getRoyalty());
            //净利润=总价-优惠券-提成
            confluence.setProfit(confluenceDetail.getProfit() + confluence.getProfit());
        } else {
            //第一段业绩
            startCalendar.set(Calendar.HOUR_OF_DAY, 0);
            startCalendar.set(Calendar.MINUTE, 0);
            startCalendar.set(Calendar.SECOND, 0);
            Date firstStartTime = startCalendar.getTime();

            startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            startCalendar.set(Calendar.HOUR_OF_DAY, 23);
            startCalendar.set(Calendar.MINUTE, 59);
            startCalendar.set(Calendar.SECOND, 59);
            Date firstEndTime = startCalendar.getTime();
            ConfluenceDetail firstConfluenceDetail = doUserPerformanceSummary(user, firstStartTime, firstEndTime);
            //总价
            confluence.setTotalPrice(firstConfluenceDetail.getTotalPrice() + confluence.getTotalPrice());
            //计算销售提成价格
            confluence.setRoyalty(firstConfluenceDetail.getRoyalty() + confluence.getRoyalty());
            //净利润=总价-优惠券-提成
            confluence.setProfit(firstConfluenceDetail.getProfit() + confluence.getProfit());
            //第二段业绩
            endCalendar.set(Calendar.HOUR_OF_DAY, 23);
            endCalendar.set(Calendar.MINUTE, 59);
            endCalendar.set(Calendar.SECOND, 59);
            Date secondEndTime = endCalendar.getTime();

            endCalendar.set(Calendar.DAY_OF_MONTH, 1);
            endCalendar.set(Calendar.HOUR_OF_DAY, 0);
            endCalendar.set(Calendar.MINUTE, 0);
            endCalendar.set(Calendar.SECOND, 0);
            Date secondStartTime = endCalendar.getTime();

            ConfluenceDetail secondConfluenceDetail = doUserPerformanceSummary(user, secondStartTime, secondEndTime);
            //总价
            confluence.setTotalPrice(secondConfluenceDetail.getTotalPrice() + confluence.getTotalPrice());
            //计算销售提成价格
            confluence.setRoyalty(secondConfluenceDetail.getRoyalty() + confluence.getRoyalty());
            //净利润=总价-优惠券-提成
            confluence.setProfit(secondConfluenceDetail.getProfit() + confluence.getProfit());
        }
        return confluence;
    }

    @Override
    public ConfluenceDetail buyerPerformanceSummary(Buyer buyer, Date startTime, Date endTime) {
        ConfluenceDetail confluence = new ConfluenceDetail();
        //商户Id
        confluence.setId(buyer.getId());
        //商户店铺名称
        if (StringUtils.isNotBlank(buyer.getShopName())) {
            confluence.setName(buyer.getShopName());
        } else {
            confluence.setName(buyer.getUserName());
        }
        confluence.setTotalPrice(0L);
        confluence.setRoyalty(0L);
        //1.判断开始时间和结束时间是否是同年同月
        //1.1 是则直接查询
        //1.2 否以开始时间的月底为分割时间，分开查询
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startTime);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        int startYear = startCalendar.get(Calendar.YEAR);
        int startMonth = startCalendar.get(Calendar.MONTH);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endTime);
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        int endYear = endCalendar.get(Calendar.YEAR);
        int endMonth = endCalendar.get(Calendar.MONTH);
        if (startYear == endYear && startMonth == endMonth) {
            ConfluenceDetail confluenceDetail = doBuyerPerformanceSummary(buyer, startCalendar.getTime(), endCalendar.getTime());
            //总价
            confluence.setTotalPrice(confluenceDetail.getTotalPrice() + confluence.getTotalPrice());
        } else {
            //第一段业绩
            startCalendar.set(Calendar.HOUR_OF_DAY, 0);
            startCalendar.set(Calendar.MINUTE, 0);
            startCalendar.set(Calendar.SECOND, 0);
            Date firstStartTime = startCalendar.getTime();

            startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            startCalendar.set(Calendar.HOUR_OF_DAY, 23);
            startCalendar.set(Calendar.MINUTE, 59);
            startCalendar.set(Calendar.SECOND, 59);
            Date firstEndTime = startCalendar.getTime();
            ConfluenceDetail firstConfluenceDetail = doBuyerPerformanceSummary(buyer, firstStartTime, firstEndTime);
            //总价
            confluence.setTotalPrice(firstConfluenceDetail.getTotalPrice() + confluence.getTotalPrice());
            //第二段业绩
            endCalendar.set(Calendar.HOUR_OF_DAY, 23);
            endCalendar.set(Calendar.MINUTE, 59);
            endCalendar.set(Calendar.SECOND, 59);
            Date secondEndTime = endCalendar.getTime();

            endCalendar.set(Calendar.DAY_OF_MONTH, 1);
            endCalendar.set(Calendar.HOUR_OF_DAY, 0);
            endCalendar.set(Calendar.MINUTE, 0);
            endCalendar.set(Calendar.SECOND, 0);
            Date secondStartTime = endCalendar.getTime();

            ConfluenceDetail secondConfluenceDetail = doBuyerPerformanceSummary(buyer, secondStartTime, secondEndTime);
            //总价
            confluence.setTotalPrice(secondConfluenceDetail.getTotalPrice() + confluence.getTotalPrice());
        }
        return confluence;
    }

    public ConfluenceDetail doBuyerPerformanceSummary(Buyer buyer, Date startTime, Date endTime) {
        ConfluenceDetail confluenceDetail = new ConfluenceDetail();
        confluenceDetail.setId(buyer.getId());

        Long totalPrice = 0L;
        //查询会员在指定时间的所有已经完成支付的订单
        Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            //订单状态300-700表示已经支付
            predicateList.add(cb.between(root.get("orderStatus"), 300, 600));
            predicateList.add(cb.between(root.get("payEndTime"), startTime, endTime));
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            return query.getRestriction();
        };
        List<Order> orders = orderRepository.findAll(orderSpecification);
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
            predicateList.add(cb.between(root.get("updateTime"), startTime, endTime));
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            Calendar instance = Calendar.getInstance();
            //当前月的第一天
            instance.setTime(endTime);
            instance.set(Calendar.DAY_OF_MONTH, 1);
            instance.set(Calendar.HOUR_OF_DAY, 0);
            instance.set(Calendar.MINUTE, 0);
            instance.set(Calendar.SECOND, 0);
            predicateList.add(cb.lessThan(root.get("createTime"), instance.getTime()));
            predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            return query.getRestriction();
        };
        orders = orderRepository.findAll(orderSpecification);
        if (orders.size() > 0) {
            for (Order order : orders) {
                //减去要扣除绩效的单子
                totalPrice -= order.getTotalPrice();
            }
        }
        //计算销售提成价格
        confluenceDetail.setTotalPrice(totalPrice);
        return confluenceDetail;
    }

    private ConfluenceDetail doUserPerformanceSummary(User user, Date startTime, Date endTime) {
        //计算绩效
        ConfluenceDetail confluenceDetail = new ConfluenceDetail();
        confluenceDetail.setTotalPrice(0L);
        confluenceDetail.setRoyalty(0L);
        //查询绑定会员列表
        List<Buyer> buyers = buyerRepository.findAllBySaleUserIdAndIsAvailable(user.getId(), true);
        Long totalPrice = 0L;
        //优惠券金额
        AtomicLong couponTotal = new AtomicLong(0L);
        for (Buyer buyer : buyers) {
            //查询会员在指定时间的所有已经完成支付的订单
            Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态300-700表示已经支付
                predicateList.add(cb.between(root.get("orderStatus"), 300, 600));
                predicateList.add(cb.between(root.get("payEndTime"), startTime, endTime));
                predicateList.add(cb.equal(root.get("isAvailable"), true));
                predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
                query.where(cb.and(predicateList.toArray(new Predicate[0])));
                return query.getRestriction();
            };
            List<Order> orders = orderRepository.findAll(orderSpecification);
            if (orders.size() > 0) {
                for (Order order : orders) {
                    //统计所有订单价格
                    totalPrice += order.getTotalPrice();
                    couponTotal.addAndGet(order.getPreferentialFee());
                }
            }
            //计算退货的单子
            orderSpecification = (Specification<Order>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                //订单状态大于700表示要扣除绩效
                predicateList.add(cb.greaterThan(root.get("orderStatus"), 700));
                predicateList.add(cb.between(root.get("updateTime"), startTime, endTime));
                predicateList.add(cb.equal(root.get("isAvailable"), true));
                Calendar instance = Calendar.getInstance();
                //当前月的第一天
                instance.setTime(endTime);
                instance.set(Calendar.DAY_OF_MONTH, 1);
                instance.set(Calendar.HOUR_OF_DAY, 0);
                instance.set(Calendar.MINUTE, 0);
                instance.set(Calendar.SECOND, 0);
                predicateList.add(cb.lessThan(root.get("createTime"), instance.getTime()));
                predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
                query.where(cb.and(predicateList.toArray(new Predicate[0])));
                return query.getRestriction();
            };
            orders = orderRepository.findAll(orderSpecification);
            if (orders.size() > 0) {
                for (Order order : orders) {
                    //减去要扣除绩效的单子
                    totalPrice -= order.getTotalPrice();
                }
            }
        }
        //总价
        confluenceDetail.setTotalPrice(totalPrice);
        //计算销售提成价格
        confluenceDetail.setRoyalty((long) (totalPrice * user.getRatio() * 0.01));
        //净利润=总价-优惠券-提成
        confluenceDetail.setProfit(totalPrice - couponTotal.get() - ((long) (totalPrice * user.getRatio() * 0.01)));
        return confluenceDetail;
    }
//
//
//    public static void main(String[] args) throws ParseException {
//        Calendar calendar = Calendar.getInstance();
//        Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-02-02 13:13:12");
//        calendar.setTime(parse);
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//        calendar.set(Calendar.HOUR_OF_DAY, 23);
//        calendar.set(Calendar.MINUTE, 59);
//        calendar.set(Calendar.SECOND, 59);
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));
//    }
}
