package com.jingliang.mall.controller;

import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.*;
import com.jingliang.mall.req.OrderReq;
import com.jingliang.mall.resp.OrderResp;
import com.jingliang.mall.service.*;
import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.citrsw.annatation.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.jingliang.mall.common.NullToString.nullString;

/**
 * 订单表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
@RequestMapping("/back/order")
@Api(description = "订单")
@Slf4j
@RestController("backOrderController")
public class OrderController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final BuyerService buyerService;
    private final OfflinePaymentService offlinePaymentService;
    private final GroupService groupService;

    public OrderController(OrderService orderService, OrderDetailService orderDetailService, BuyerService buyerService, OfflinePaymentService offlinePaymentService, GroupService groupService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.buyerService = buyerService;
        this.offlinePaymentService = offlinePaymentService;
        this.groupService = groupService;
    }

    /**
     * 发货
     */
    @ApiOperation(description = "发货")
    @PostMapping("/deliver")
    public Result<OrderResp> deliver(@RequestBody OrderReq orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq);
        if (Objects.isNull(orderReq.getId()) || StringUtils.isBlank(orderReq.getDeliveryName())
                || StringUtils.isBlank(orderReq.getDeliveryPhone()) || StringUtils.isBlank(orderReq.getStorehouse())) {
            return Result.buildParamFail();
        }
        Order order1 = orderService.findById(orderReq.getId());
        if (order1.getOrderStatus() > 300) {
            return Result.build(Msg.FAIL, "此订单已完成发货");
        }
        Order order = new Order();
        order.setId(orderReq.getId());
        order.setDeliveryName(orderReq.getDeliveryName());
        order.setStorehouse(orderReq.getStorehouse());
        order.setOrderStatus(400);
        order.setDeliveryPhone(orderReq.getDeliveryPhone());
        order.setUpdateTime(new Date());
        User user = (User) session.getAttribute(sessionUser);
        order.setUpdateUserId(user.getId());
        order.setUpdateUserName(user.getUserName());
        order = orderService.update(order);
        if (Objects.isNull(order)) {
            log.debug("返回结果：{}", Msg.TEXT_ORDER_DELIVER_SKU_FAIL);
            return Result.build(Msg.ORDER_FAIL, Msg.TEXT_ORDER_DELIVER_SKU_FAIL);
        }
        OrderResp orderResp = BeanMapper.map(order, OrderResp.class);
        log.debug("返回结果：{}", orderResp);
        return Result.buildUpdateOk(orderResp);
    }

    /**
     * 发货
     */
    @ApiOperation(description = "批量发货")
    @PostMapping("/deliverAll")
    public Result<List<OrderResp>> deliverAll(@RequestBody Map<String, Object> map, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", map);
        List<Long> orderIds = ((List<?>) map.get("orderIds")).stream().map(p -> Long.valueOf(p.toString())).collect(Collectors.toList());
        //获取map集合中的配送员参数
        String house = (String) map.get("storehouseName");
        String deliveryName = (String) map.get("expressUser");
        String deliveryPhone = (String) map.get("expressPhone");
        //创建订单类，方便根据id查询后存储。
        Order order2 = new Order();
        //创建订单请求类，对该类进行循环遍历
        List<OrderReq> orderReqs = new ArrayList<>();
        //创建订单返回类，进行订单返回结果的存储
        List<OrderResp> orderResps = new ArrayList<>();
        //对订单类进行查询，并存储到list中
        for (Long id : orderIds) {
            order2 = orderService.findById(id);
            order2.setStorehouse(house);
            order2.setDeliveryName(deliveryName);
            order2.setDeliveryPhone(deliveryPhone);
            orderReqs.add(BeanMapper.map(order2, OrderReq.class));
        }
        //开始对每个订单进行发货操作
        for (OrderReq orderReq : orderReqs) {
            if (Objects.isNull(orderReq.getId()) || StringUtils.isBlank(orderReq.getDeliveryName())
                    || StringUtils.isBlank(orderReq.getDeliveryPhone()) || StringUtils.isBlank(orderReq.getStorehouse())) {
                return Result.buildParamFail();
            }
            Order order1 = orderService.findById(orderReq.getId());
            if (order1.getOrderStatus() > 300) {
                return Result.build(Msg.FAIL, "此订单已完成发货");
            }
            Order order = new Order();
            order.setId(orderReq.getId());
            order.setDeliveryName(orderReq.getDeliveryName());
            order.setStorehouse(orderReq.getStorehouse());
            order.setOrderStatus(400);
            order.setDeliveryPhone(orderReq.getDeliveryPhone());
            order.setUpdateTime(new Date());
            User user = (User) session.getAttribute(sessionUser);
            order.setUpdateUserId(user.getId());
            order.setUpdateUserName(user.getUserName());
            order = orderService.update(order);
            if (Objects.isNull(order)) {
                log.debug("返回结果：{}", Msg.TEXT_ORDER_DELIVER_SKU_FAIL);
                return Result.build(Msg.ORDER_FAIL, Msg.TEXT_ORDER_DELIVER_SKU_FAIL);
            }
            OrderResp orderResp = BeanMapper.map(order, OrderResp.class);
            orderResps.add(orderResp);
        }
        System.out.println(orderResps);
        log.debug("返回结果：{}", orderResps);
        //此处返回一个订单返回类的list
        return Result.buildUpdateOk(orderResps);
    }

    /**
     * 退货(不扣绩效)
     */
    @ApiOperation(description = "退货(不扣绩效)")
    @PostMapping("/refunds")
    public Result<OrderResp> refunds(@RequestBody OrderReq orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq);
        if (Objects.isNull(orderReq.getId())) {
            return Result.buildParamFail();
        }
        Date date = new Date();
        Order order = new Order();
        order.setId(orderReq.getId());
        order.setDeliveryName(orderReq.getDeliveryName());
        order.setOrderStatus(700);
        order.setDeliveryPhone(orderReq.getDeliveryPhone());
        order.setFinishTime(date);
        order.setUpdateTime(date);
        User user = (User) session.getAttribute(sessionUser);
        order.setUpdateUserId(user.getId());
        order.setUpdateUserName(user.getUserName());
        order = orderService.update(order);
        OrderResp orderResp = BeanMapper.map(order, OrderResp.class);
        log.debug("返回结果：{}", orderResp);
        return Result.buildUpdateOk(orderResp);
    }

    /**
     * 退货(扣绩效)
     */
    @ApiOperation(description = "退货(扣绩效)")
    @PostMapping("/refunds/money")
    public Result<OrderResp> refundsMoney(@RequestBody OrderReq orderReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", orderReq);
        if (Objects.isNull(orderReq.getId())) {
            return Result.buildParamFail();
        }
        Date date = new Date();
        Order order = new Order();
        order.setId(orderReq.getId());
        order.setDeliveryName(orderReq.getDeliveryName());
        order.setOrderStatus(800);
        order.setDeliveryPhone(orderReq.getDeliveryPhone());
        order.setFinishTime(date);
        order.setUpdateTime(date);
        User user = (User) session.getAttribute(sessionUser);
        order.setUpdateUserId(user.getId());
        order.setUpdateUserName(user.getUserName());
        order = orderService.update(order);
        OrderResp orderResp = BeanMapper.map(order, OrderResp.class);
        log.debug("返回结果：{}", orderResp);
        return Result.buildUpdateOk(orderResp);
    }

    /**
     * 分页查询全部用户订单信息
     */
    @ApiOperation(description = "分页查询全部用户订单信息")
    @GetMapping("/page/all")
    public Result<MallPage<OrderResp>> pageAll(OrderReq orderReq, String region, @RequestParam(value = "orderStatuses", required = false) List<Integer> orderStatuses) {
        log.debug("请求参数：{}", orderReq);
        PageRequest pageRequest = PageRequest.of(orderReq.getPage(), orderReq.getPageSize());
        if (StringUtils.isNotBlank(orderReq.getClause())) {
            pageRequest = PageRequest.of(orderReq.getPage(), orderReq.getPageSize(), Sort.by(MallUtils.separateOrder(orderReq.getClause())));
        }
        Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            List<Predicate> orOredicateList = new ArrayList<>();
            if (Objects.nonNull(orderReq.getId())) {
                predicateList.add(cb.equal(root.get("id"), orderReq.getId()));
            }
            if (StringUtils.isNotBlank(orderReq.getOrderNo())) {
                predicateList.add(cb.equal(root.get("orderNo"), orderReq.getOrderNo()));
            }
            if (Objects.nonNull(orderReq.getBuyerId())) {
                predicateList.add(cb.equal(root.get("buyerId"), orderReq.getBuyerId()));
            }
            if (Objects.nonNull(orderReq.getCreateTimeStart()) && Objects.nonNull(orderReq.getCreateTimeEnd())) {
                predicateList.add(cb.between(root.get("createTime"), orderReq.getCreateTimeStart(), orderReq.getCreateTimeEnd()));
            }
            if (!CollectionUtils.isEmpty(orderStatuses)) {
                for (Integer orderStatus : orderStatuses) {
                    orOredicateList.add(cb.equal(root.get("orderStatus"), orderStatus));
                }
            }

            if (StringUtils.isNotBlank(region)) {
                predicateList.add(cb.like(root.get("detailAddressArea"), region.replaceAll("0*$", "") + "%"));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            if (!CollectionUtils.isEmpty(orderStatuses)) {
                query.where(cb.and(predicateList.toArray(new Predicate[0])), cb.or(orOredicateList.toArray(new Predicate[0])));
            } else {
                query.where(cb.and(predicateList.toArray(new Predicate[0])));
            }
            query.orderBy(cb.desc(root.get("createTime")));
            return query.getRestriction();
        };
        Page<Order> orderPage = orderService.findAll(orderSpecification, pageRequest);
        MallPage<OrderResp> orderRespMallPage = MallUtils.toMallPage(orderPage, OrderResp.class);
        log.debug("返回结果：{}", orderRespMallPage);
        return Result.buildQueryOk(orderRespMallPage);
    }


    /**
     * 导出excel
     */
    @ApiOperation(description = "导出订单excel")
    @GetMapping("/download/excel")
    public ResponseEntity<byte[]> download(OrderReq orderReq, @RequestParam(value = "orderStatuses", required = false) List<Integer> orderStatuses) throws IOException {
        Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            List<Predicate> orOredicateList = new ArrayList<>();
            if (Objects.nonNull(orderReq.getId())) {
                predicateList.add(cb.equal(root.get("id"), orderReq.getId()));
            }
            if (StringUtils.isNotBlank(orderReq.getOrderNo())) {
                predicateList.add(cb.equal(root.get("orderNo"), orderReq.getOrderNo()));
            }
            if (Objects.nonNull(orderReq.getBuyerId())) {
                predicateList.add(cb.equal(root.get("buyerId"), orderReq.getBuyerId()));
            }
            if (Objects.nonNull(orderReq.getCreateTimeStart()) && Objects.nonNull(orderReq.getCreateTimeEnd())) {
                predicateList.add(cb.between(root.get("createTime"), orderReq.getCreateTimeStart(), orderReq.getCreateTimeEnd()));
            }
            if (!CollectionUtils.isEmpty(orderStatuses)) {
                for (Integer orderStatus : orderStatuses) {
                    orOredicateList.add(cb.equal(root.get("orderStatus"), orderStatus));
                }
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            if (!CollectionUtils.isEmpty(orderStatuses)) {
                query.where(cb.and(predicateList.toArray(new Predicate[0])), cb.or(orOredicateList.toArray(new Predicate[0])));
            } else {
                query.where(cb.and(predicateList.toArray(new Predicate[0])));
            }
            query.orderBy(cb.desc(root.get("createTime")));
            return query.getRestriction();
        };
        List<Order> orders = orderService.findAll(orderSpecification);
        XSSFWorkbook orderWorkbook = ExcelUtils.createExcelXlsx("销货单", Msg.orderExcelTitle);
        XSSFSheet sheet = orderWorkbook.getSheet("销货单");
        XSSFCellStyle cellStyle = orderWorkbook.createCellStyle();
        CreationHelper createHelper = orderWorkbook.getCreationHelper();
        cellStyle.setDataFormat(
                createHelper.createDataFormat().getFormat("yyyy/MM/dd HH:mm:ss"));
        int rowNum = 1;
        for (Order order : orders) {
            XSSFRow row = sheet.createRow(rowNum);
            //"单据日期", "单据编号","大区", "客户编号", "客户名称", "销售人员"
            //            , "优惠金额", "客户承担费用", "订单金额","本次收款","使用金币数","返金币数","结算账户", "单据备注", "商品编号", "商品名称", "商品型号", "属性",
            //            "单位", "数量", "单价", "折扣率%", "折扣额", "金额", "税率%", "仓库","收货地址+收货人+电话","备注"
            //单据日期
            int celNum = 0;
            Date createTime = order.getCreateTime();
            XSSFCell cell = row.createCell(celNum);
            cell.setCellValue(createTime);
            cell.setCellStyle(cellStyle);
            //单据编号
            String orderNo = order.getOrderNo();
            row.createCell(++celNum).setCellValue(orderNo);
            Buyer buyer = buyerService.findById(order.getBuyerId());
            //大区
            Group group = groupService.findByGroupNo(order.getGroupNo());
            row.createCell(++celNum).setCellValue(group == null ? "未分区" : group.getGroupName());
            //客户编号
            Long buyerId = buyer.getId();
            row.createCell(++celNum).setCellValue(buyerId);
            //客户名称
            String shopName = buyer.getShopName();
            row.createCell(++celNum).setCellValue(shopName);
            //销售人员
            String userName =order.getSale().getUserName();
            row.createCell(++celNum).setCellValue(userName);
            //优惠金额(元)
            double preferentialFee = (order.getPreferentialFee() * 1.00) / 100;
            row.createCell(++celNum).setCellValue(preferentialFee);
            //客户承担费用
            ++celNum;
            //订单金额
            row.createCell(++celNum).setCellValue((order.getTotalPrice() * 1.00) / 100);
            //本次收款
            row.createCell(++celNum).setCellValue((order.getPayableFee() * 1.00) / 100);
            //判断是否使用金币
            if (order.getIsGold()) {
                //使用金币数
                row.createCell(++celNum).setCellValue(order.getGold());
            } else {
                ++celNum;
            }
            if (order.getOrderStatus() >= 300 && order.getOrderStatus() < 600) {
                //待返金币数
                if (order.getReturnGold() != null) {
                    row.createCell(++celNum).setCellValue(order.getReturnGold());
                } else {
                    ++celNum;
                }
            } else {
                ++celNum;
            }
            if (order.getOrderStatus() >= 600) {
                //已返金币数
                if (order.getReturnGold() != null) {
                    row.createCell(++celNum).setCellValue(order.getReturnGold());
                } else {
                    ++celNum;
                }
            } else {
                ++celNum;
            }
            if (order.getPayWay() == 200) {
                OfflinePayment offlinePayment = offlinePaymentService.findByOrderId(order.getId());
                if (Objects.nonNull(offlinePayment)) {
                    //结算账户
                    String payWay = offlinePayment.getPayWay();
                    row.createCell(++celNum).setCellValue(payWay);
                    //单据备注
                    String remark = offlinePayment.getRemark();
                    row.createCell(++celNum).setCellValue(remark);
                } else {
                    ++celNum;
                    ++celNum;
                }
            } else {
                ++celNum;
                ++celNum;
            }
            List<OrderDetail> orderDetails = orderDetailService.findByOrderId(order.getId());
            boolean flag = false;
            for (OrderDetail orderDetail : orderDetails) {
                int productCelNum = celNum;
                //商品编号
                String productNo = orderDetail.getProduct().getProductNo();
                row.createCell(++productCelNum).setCellValue(productNo);
                //商品名称
                String productName = orderDetail.getProduct().getProductName();
                row.createCell(++productCelNum).setCellValue(productName.concat(" ").concat(orderDetail.getProduct().getSpecs()));
                //商品型号
                String specs = orderDetail.getProduct().getSpecs();
                row.createCell(++productCelNum).setCellValue(specs);
//                ++productCelNum;
                //属性(商品分类)
                String productTypeName = orderDetail.getProduct().getProductTypeName();
                row.createCell(++productCelNum).setCellValue(productTypeName);
                //单位
                ++productCelNum;
                //数量
                Integer productNum = orderDetail.getProductNum();
                row.createCell(++productCelNum).setCellValue(productNum);
                //单价
                Long sellingPrice = orderDetail.getSellingPrice();
                row.createCell(++productCelNum).setCellValue(sellingPrice * 1.00 / 100);
                //折扣率%
                ++productCelNum;
                //折扣额
                ++productCelNum;
                //金额
                ++productCelNum;
                //税率%
                ++productCelNum;
                //仓库
                String storehouse = order.getStorehouse();
                row.createCell(++productCelNum).setCellValue(storehouse);
                //重复地址只写入一次
                if (!flag) {
                    //收货地址
                    String detailAddress = order.getDetailAddress();
                    //收货人
                    String receiverName = order.getReceiverName();
                    //电话
                    String receiverPhone = order.getReceiverPhone();
                    row.createCell(++productCelNum).setCellValue(detailAddress.concat(" ").concat(receiverName).concat(" ").concat(receiverPhone));
                    flag = true;
                }
                //创建一行
                row = sheet.createRow(++rowNum);
            }
        }
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        orderWorkbook.write(arrayOutputStream);
        String newName = URLEncoder.encode("销货单明细-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ".xlsx", "utf-8")
                .replaceAll("\\+", "%20").replaceAll("%28", "\\(")
                .replaceAll("%29", "\\)").replaceAll("%3B", ";")
                .replaceAll("%40", "@").replaceAll("%23", "\\#")
                .replaceAll("%26", "\\&").replaceAll("%2C", "\\,");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", newName));
        headers.add("Expires", "0");
        headers.add("Pragma", "no-cache");
        return ResponseEntity.ok().headers(headers)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(arrayOutputStream.size())
                .body(arrayOutputStream.toByteArray());
    }

    /**
     * 导出excel优化第二版
     */
    @ApiOperation(description = "导出订单excel")
    @GetMapping("/fast/excel")
    public ResponseEntity<byte[]> download() throws IOException {
        XSSFWorkbook orderWorkbook = ExcelUtils.createExcelXlsx("销货单", Msg.orderExcelTitle);
        XSSFSheet sheet = orderWorkbook.getSheet("销货单");
        int rowNum = 1;
        //获取数据集合
        List<Map<String, String>> mapList = orderService.orderExcel();
        HashSet<String> set = new HashSet<>();
        for (Map<String, String> stringMap : mapList) {
            //根据订单编号向Set集合存数据
            boolean existence = set.add(stringMap.get("orderNo"));
            XSSFRow row = sheet.createRow(rowNum);
            int celNum = 0;
            if (existence) {
                //不重复的数据将map中的所有数据存入新的resultMap中
                //"单据日期", "单据编号","大区", "客户编号", "客户名称", "销售人员"
                //            , "优惠金额", "客户承担费用", "订单金额","本次收款","使用金币数","返金币数","结算账户", "单据备注", "商品编号", "商品名称", "商品型号", "属性",
                //            "单位", "数量", "单价", "折扣率%", "折扣额", "金额", "税率%", "仓库","收货地址+收货人+电话","备注"
                //单据日期
                row.createCell(celNum).setCellValue(nullString(stringMap.get("createTime")));
                //单据编号
                String orderNo = nullString(stringMap.get("orderNo"));
                row.createCell(++celNum).setCellValue(orderNo);
                //大区
                row.createCell(++celNum).setCellValue(nullString(stringMap.get("groupName")));
                //客户编号
                row.createCell(++celNum).setCellValue(nullString(stringMap.get("buyerId")));
                //客户名称
                String shopName = nullString(stringMap.get("shopName"));
                row.createCell(++celNum).setCellValue(shopName);
                //销售人员
                String userName = nullString(stringMap.get("userName"));
                row.createCell(++celNum).setCellValue(userName);
                //优惠金额(元)
                row.createCell(++celNum).setCellValue(nullString(stringMap.get("preferentialFee")));
                //客户承担费用
                ++celNum;
                //订单金额
                if (stringMap.get("totalPrice") != null) {
                    row.createCell(++celNum).setCellValue(Integer.valueOf(stringMap.get("totalPrice")) * 1.00 / 100);
                } else {
                    ++celNum;
                }
                //本次收款
                if (stringMap.get("totalPrice") != null) {
                    row.createCell(++celNum).setCellValue(Integer.valueOf(stringMap.get("payableFee")) * 1.00 / 100);
                } else {
                    ++celNum;
                }
                //判断是否使用金币
                if (nullString(stringMap.get("isGold")).equals("1")) {
                    //使用金币数
                    row.createCell(++celNum).setCellValue(nullString(stringMap.get("gold")));
                } else {
                    ++celNum;
                }
                if (stringMap.get("orderStatus") != null) {
                    Integer integer = Integer.valueOf(stringMap.get("orderStatus"));
                    if (integer >= 300 && integer < 600) {
                        //待返金币数
                        if (stringMap.get("returnGold") != null) {
                            row.createCell(++celNum).setCellValue(stringMap.get("returnGold"));
                        } else {
                            ++celNum;
                        }
                    } else {
                        ++celNum;
                    }
                    if (integer >= 600) {
                        //已返金币数
                        if (stringMap.get("returnGold") != null) {
                            row.createCell(++celNum).setCellValue(stringMap.get("returnGold"));
                        } else {
                            ++celNum;
                        }
                    } else {
                        ++celNum;
                    }
                } else {
                    ++celNum;
                    ++celNum;
                }
                if (nullString(stringMap.get("payWay")).equals("200")) {
                    OfflinePayment offlinePayment = offlinePaymentService.findByOrderId(Long.valueOf(stringMap.get("orderId")));
                    if (Objects.nonNull(offlinePayment)) {
                        //结算账户
                        String payWay = offlinePayment.getPayWay();
                        row.createCell(++celNum).setCellValue(payWay);
                        //单据备注
                        String remark = offlinePayment.getRemark();
                        row.createCell(++celNum).setCellValue(remark);
                    } else {
                        ++celNum;
                        ++celNum;
                    }
                } else {
                    ++celNum;
                    ++celNum;
                }
                //商品编号
                String productNo = stringMap.get("productNo");
                row.createCell(++celNum).setCellValue(productNo);
                //商品名称
                String productName = stringMap.get("productName");
                row.createCell(++celNum).setCellValue(productName.concat("%20").concat(stringMap.get("specs")));
                //商品型号
                String specs = stringMap.get("specs");
                row.createCell(++celNum).setCellValue(specs);
//                ++celNum;
                //属性(商品分类)
                String productTypeName = stringMap.get("productTypeName");
                row.createCell(++celNum).setCellValue(productTypeName);
                //单位
                ++celNum;
                //数量
                Integer productNum = Integer.valueOf(stringMap.get("productNum"));
                row.createCell(++celNum).setCellValue(productNum);
                //单价
                Long sellingPrice = Long.valueOf(stringMap.get("sellingPrice"));
                row.createCell(++celNum).setCellValue(sellingPrice * 1.00 / 100);
                //折扣率%
                ++celNum;
                //折扣额
                ++celNum;
                //金额
                ++celNum;
                //税率%
                ++celNum;
                //仓库
                String storehouse = stringMap.get("storehouse");
                row.createCell(++celNum).setCellValue(storehouse);
                //收货地址
                String detailAddress = nullString(stringMap.get("detailAddress"));
                //收货人
                String receiverName = nullString(stringMap.get("receiverName"));
                //电话
                String receiverPhone = nullString(stringMap.get("receiverPhone"));
                row.createCell(++celNum).setCellValue(detailAddress.concat("%20").concat(receiverName).concat("%20").concat(receiverPhone));
            } else {
                ++celNum;
                ++celNum;
                ++celNum;
                ++celNum;
                ++celNum;
                ++celNum;
                ++celNum;
                ++celNum;
                ++celNum;
                ++celNum;
                ++celNum;
                ++celNum;
                ++celNum;
                ++celNum;
                //商品编号
                String productNo = stringMap.get("productNo");
                row.createCell(++celNum).setCellValue(productNo);
                //商品名称
                String productName = stringMap.get("productName");
                row.createCell(++celNum).setCellValue(productName.concat(" ").concat(stringMap.get("specs")));
                //商品型号
                String specs = stringMap.get("specs");
                row.createCell(++celNum).setCellValue(specs);
//                ++celNum;
                //属性(商品分类)
                String productTypeName = stringMap.get("productTypeName");
                row.createCell(++celNum).setCellValue(productTypeName);
                //单位
                ++celNum;
                //数量
                Integer productNum = Integer.valueOf(stringMap.get("productNum"));
                row.createCell(++celNum).setCellValue(productNum);
                //单价
                Long sellingPrice = Long.valueOf(stringMap.get("sellingPrice"));
                row.createCell(++celNum).setCellValue(sellingPrice * 1.00 / 100);
                //折扣率%
                ++celNum;
                //折扣额
                ++celNum;
                //金额
                ++celNum;
                //税率%
                ++celNum;
                //仓库
                String storehouse = stringMap.get("storehouse");
                row.createCell(++celNum).setCellValue(storehouse);
                ++celNum;
            }
            //创建一行
            row = sheet.createRow(++rowNum);
        }
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        orderWorkbook.write(arrayOutputStream);
        String newName = URLEncoder.encode("销货单明细-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ".xlsx", "utf-8")
                .replaceAll("\\+", "%20").replaceAll("%28", "\\(")
                .replaceAll("%29", "\\)").replaceAll("%3B", ";")
                .replaceAll("%40", "@").replaceAll("%23", "\\#")
                .replaceAll("%26", "\\&").replaceAll("%2C", "\\,");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", newName));
        headers.add("Expires", "0");
        headers.add("Pragma", "no-cache");
        return ResponseEntity.ok().headers(headers)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(arrayOutputStream.size())
                .body(arrayOutputStream.toByteArray());
    }
}