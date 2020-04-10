package com.jingliang.mall.controller;

import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.*;
import com.jingliang.mall.req.OrderReq;
import com.jingliang.mall.resp.OrderResp;
import com.jingliang.mall.service.BuyerService;
import com.jingliang.mall.service.OfflinePaymentService;
import com.jingliang.mall.service.OrderDetailService;
import com.jingliang.mall.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 订单表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
@RequestMapping("/back/order")
@Api(tags = "订单")
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

    public OrderController(OrderService orderService, OrderDetailService orderDetailService, BuyerService buyerService, OfflinePaymentService offlinePaymentService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.buyerService = buyerService;
        this.offlinePaymentService = offlinePaymentService;
    }

    /**
     * 发货
     */
    @ApiOperation(value = "发货")
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
     * 退货(不扣绩效)
     */
    @ApiOperation(value = "退货(不扣绩效)")
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
    @ApiOperation(value = "退货(扣绩效)")
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
    @ApiOperation(value = "分页查询全部用户订单信息")
    @GetMapping("/page/all")
    public Result<MallPage<OrderResp>> pageAll(OrderReq orderReq) {
        log.debug("请求参数：{}", orderReq);
        PageRequest pageRequest = PageRequest.of(orderReq.getPage(), orderReq.getPageSize());
        if (StringUtils.isNotBlank(orderReq.getClause())) {
            pageRequest = PageRequest.of(orderReq.getPage(), orderReq.getPageSize(), Sort.by(MallUtils.separateOrder(orderReq.getClause())));
        }
        Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
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
            if (Objects.nonNull(orderReq.getOrderStatus())) {
                predicateList.add(cb.equal(root.get("orderStatus"), orderReq.getOrderStatus()));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
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
    @ApiOperation(value = "导出订单excel")
    @GetMapping("/download/excel")
    public ResponseEntity<byte[]> download(OrderReq orderReq) throws IOException {
        Specification<Order> orderSpecification = (Specification<Order>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
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
            if (Objects.nonNull(orderReq.getOrderStatus())) {
                predicateList.add(cb.equal(root.get("orderStatus"), orderReq.getOrderStatus()));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
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
            //"单据日期", "单据编号", "客户编号", "客户名称", "销售人员"
            //            , "优惠金额", "客户承担费用", "本次收款", "结算账户", "单据备注", "商品编号", "商品名称", "商品型号", "属性",
            //            "单位", "数量", "单价", "折扣率%", "折扣额", "金额", "税率%", "仓库", "备注"
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
            //客户编号
            Long buyerId = buyer.getId();
            row.createCell(++celNum).setCellValue(buyerId);
            //客户名称
            String shopName = buyer.getShopName();
            row.createCell(++celNum).setCellValue(shopName);
            //销售人员
            String userName = buyer.getSale().getUserName();
            row.createCell(++celNum).setCellValue(userName);
            //优惠金额(元)
            double preferentialFee = (order.getPreferentialFee() * 1.00) / 100;
            row.createCell(++celNum).setCellValue(preferentialFee);
            //客户承担费用
            ++celNum;
            //本次收款
            Long payableFee = order.getPayableFee();
            row.createCell(++celNum).setCellValue(payableFee * 1.00 / 100);
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
            for (OrderDetail orderDetail : orderDetails) {
                int productCelNum = celNum;
                //商品编号
                String productNo = orderDetail.getProduct().getProductNo();
                row.createCell(++productCelNum).setCellValue(productNo);
                //商品名称
                String productName = orderDetail.getProduct().getProductName();
                row.createCell(++productCelNum).setCellValue(productName);
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
                //备注（放收货地址 + 收货人 + 电话）
                row.createCell(++productCelNum).setCellValue(order.getDetailAddress() + " " + order.getReceiverName() + " " + order.getReceiverPhone());
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


}