package com.jingliang.mall.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.OfflineOrder;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.resp.OfflineOrderResp;
import com.jingliang.mall.service.OfflineOrderService;
import com.jingliang.mall.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.util.Map;

/**
 * 线下订单Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-25 10:05:44
 */
@RestController
@Slf4j
@Api(tags = "线下订单")
public class OfflineOrderController {

    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final OfflineOrderService offlineOrderService;
    private final UserService userService;

    public OfflineOrderController(OfflineOrderService offlineOrderService, UserService userService) {
        this.offlineOrderService = offlineOrderService;
        this.userService = userService;
    }

    /**
     * 分页查询全部
     */
    @GetMapping("/back/offlineOrder/page/all")
    public Result<MallPage<OfflineOrderResp>> pageAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer pageSize,
                                                      @ApiIgnore @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:dd")
                                                      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:dd", timezone = "GMT+8") Date createTimeStart,
                                                      @ApiIgnore @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:dd")
                                                      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:dd", timezone = "GMT+8") Date createTimeEnd,
                                                      Integer rate,
                                                      @ApiIgnore HttpSession session) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        User user = (User) session.getAttribute(sessionUser);
        Specification<OfflineOrder> specification = (Specification<OfflineOrder>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (createTimeStart != null && createTimeEnd != null) {
                predicateList.add(cb.between(root.get("createTime"), createTimeStart, createTimeEnd));
            }
            if (rate != null) {
                predicateList.add(cb.equal(root.get("rate"), rate));
            }
            predicateList.add(cb.equal(root.get("salesmanId"), user.getId()));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("createTime")));
            return query.getRestriction();
        };
        Page<OfflineOrder> offlineOrderPage = offlineOrderService.pageAll(specification, pageRequest);
        return Result.buildQueryOk(MallUtils.toMallPage(offlineOrderPage, OfflineOrderResp.class));
    }

    /**
     * 解锁订单
     */
    @PostMapping("/back/offlineOrder/unlock")
    public Result<Boolean> unlock(@RequestBody Map<String, Long> map) {
        Long id = map.get("id");
        return Result.build(Constant.OK, "解锁成功", offlineOrderService.unlock(id));
    }

    /**
     * 修改发票进度为已完成
     */
    @PostMapping("/back/offlineOrder/rate/success")
    public Result<Boolean> success(@RequestBody Map<String, Long> map) {
        Long id = map.get("id");
        return Result.build(Constant.OK, "开具发票进度修改成功", offlineOrderService.success(id));
    }

    /**
     * excel导出全部 (同时会进行订单锁定)
     */
    @GetMapping("/back/offlineOrder/down")
    public ResponseEntity<byte[]> down(@ApiIgnore @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:dd")
                                       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:dd", timezone = "GMT+8") Date createTimeStart,
                                       @ApiIgnore @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:dd")
                                       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:dd", timezone = "GMT+8") Date createTimeEnd,
                                       Integer rate) throws IOException {
        Specification<OfflineOrder> specification = (Specification<OfflineOrder>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (createTimeStart != null && createTimeEnd != null) {
                predicateList.add(cb.between(root.get("createTime"), createTimeStart, createTimeEnd));
            }
            if (rate != null) {
                predicateList.add(cb.equal(root.get("rate"), rate));
            }
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("createTime")));
            return query.getRestriction();
        };
        List<OfflineOrder> offlineOrderList = offlineOrderService.downExcel(specification);
        //生产excel
        XSSFWorkbook orderWorkbook = ExcelUtils.createExcelXlsx("线下订单", Constant.offlineOrderExcelTitle);
        XSSFSheet sheet = orderWorkbook.getSheet("销货单");
        XSSFCellStyle cellStyle = orderWorkbook.createCellStyle();
        CreationHelper createHelper = orderWorkbook.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/MM/dd HH:mm:ss"));
        int r = 1;
        for (int i = 0; i < offlineOrderList.size(); i++, r++) {
            XSSFRow row = sheet.createRow(r);
            OfflineOrder offlineOrder = offlineOrderList.get(r);
            //商铺名称
            int celNum = 0;
            String shopName = offlineOrder.getShopName();
            XSSFCell cell = row.createCell(celNum);
            cell.setCellValue(shopName);
            celNum++;

            //客户姓名
            String customerName = offlineOrder.getCustomerName();
            cell = row.createCell(celNum);
            cell.setCellValue(customerName);
            celNum++;

            //客户电话
            String customerPhone = offlineOrder.getCustomerPhone();
            cell = row.createCell(celNum);
            cell.setCellValue(customerPhone);
            celNum++;

            //商品名称
            String productName = offlineOrder.getProductName();
            String[] productNames = productName.split("/");
            cell = row.createCell(celNum);
            cell.setCellValue(productName);
            celNum++;

            //商品规格
            String productSpecification = offlineOrder.getProductSpecification();
            String[] productSpecifications = productSpecification.split("/");
            cell = row.createCell(celNum);
            cell.setCellValue(productSpecification);
            celNum++;

            //单位
            String company = offlineOrder.getCompany();
            String[] companys = company.split("/");
            cell = row.createCell(celNum);
            cell.setCellValue(company);
            celNum++;

            //数量
            String num = offlineOrder.getNum();
            String[] nums = num.split("/");
            cell = row.createCell(celNum);
            cell.setCellValue(num);
            celNum++;

            //单价(单位：分)
            String unitPrice = offlineOrder.getUnitPrice();
            String[] unitPrices = unitPrice.split("/");
            cell = row.createCell(celNum);
            cell.setCellValue(unitPrice);
            celNum++;

            //总价(单位：分)
            String totalPrice = offlineOrder.getTotalPrice();
            String[] totalPrices = totalPrice.split("/");
            cell = row.createCell(celNum);
            cell.setCellValue(totalPrice);
            celNum++;

            int detailCelNum = celNum;
            XSSFRow detailRow = row;
            for (int j = 0; j < productNames.length; j++) {
                //商品规格
                cell = detailRow.createCell(detailCelNum);
                cell.setCellValue(productSpecifications[j]);
                detailCelNum++;

                //单位
                cell = detailRow.createCell(detailCelNum);
                cell.setCellValue(companys[j]);
                detailCelNum++;

                //数量
                cell = detailRow.createCell(detailCelNum);
                cell.setCellValue(nums[j]);
                detailCelNum++;

                //单价(单位：分)
                cell = detailRow.createCell(detailCelNum);
                cell.setCellValue(unitPrices[j]);
                detailCelNum++;

                //总价(单位：分)
                cell = detailRow.createCell(detailCelNum);
                cell.setCellValue(totalPrices[j]);
                detailRow = sheet.createRow(r + j + 1);
                detailCelNum = celNum;
            }
            celNum += 6;

            //省
            String province = offlineOrder.getProvince();
            cell = row.createCell(celNum);
            cell.setCellValue(province);
            celNum++;

            //市
            String city = offlineOrder.getCity();
            cell = row.createCell(celNum);
            cell.setCellValue(city);
            celNum++;

            //区/县
            String county = offlineOrder.getCounty();
            cell = row.createCell(celNum);
            cell.setCellValue(county);
            celNum++;

            //客户地址
            String customerAddress = offlineOrder.getCustomerAddress();
            cell = row.createCell(celNum);
            cell.setCellValue(customerAddress);
            celNum++;

            //客户要求送货日期和时间
            Date deliveryTime = offlineOrder.getDeliveryTime();
            cell = row.createCell(celNum);
            cell.setCellValue(deliveryTime);
            cell.setCellStyle(cellStyle);
            celNum++;

            //业务员Id
            Long salesmanId = offlineOrder.getSalesmanId();
            cell = row.createCell(celNum);
            cell.setCellValue(salesmanId);
            celNum++;

            //业务员姓名
            String salesmanName = offlineOrder.getSalesmanName();
            cell = row.createCell(celNum);
            cell.setCellValue(salesmanName);
            celNum++;

            //业务员工号
            String salesmanNo = offlineOrder.getSalesmanNo();
            cell = row.createCell(celNum);
            cell.setCellValue(salesmanNo);
            celNum++;

            //业务员电话
            String salesmanPhone = offlineOrder.getSalesmanPhone();
            cell = row.createCell(celNum);
            cell.setCellValue(salesmanPhone);
            celNum++;

            //备注
            String remarks = offlineOrder.getRemarks();
            cell = row.createCell(celNum);
            cell.setCellValue(remarks);
            celNum++;

            //是否可用
            Boolean isAvailable = offlineOrder.getIsAvailable();
            cell = row.createCell(celNum);
            cell.setCellValue(isAvailable);
            celNum++;

            //创建时间
            Date createTime = offlineOrder.getCreateTime();
            cell = row.createCell(celNum);
            cell.setCellValue(createTime);
            cell.setCellStyle(cellStyle);
            celNum++;

            //开具发票进度
            //TODO 待添加switch
            Integer rate1 = offlineOrder.getRate();
            cell = row.createCell(celNum);
            cell.setCellValue(rate1);
            celNum++;

            //发票类型
            //TODO 待添加switch
            Integer type = offlineOrder.getType();
            cell = row.createCell(celNum);
            cell.setCellValue(type);
            celNum++;

            //单位名称
            String unitName = offlineOrder.getUnitName();
            cell = row.createCell(celNum);
            cell.setCellValue(unitName);
            celNum++;

            //纳税人识别码
            String taxpayerIdentificationNumber = offlineOrder.getTaxpayerIdentificationNumber();
            cell = row.createCell(celNum);
            cell.setCellValue(taxpayerIdentificationNumber);
            celNum++;

            //注册地址
            String registeredAddress = offlineOrder.getRegisteredAddress();
            cell = row.createCell(celNum);
            cell.setCellValue(registeredAddress);
            celNum++;

            //注册电话
            String registeredTelephone = offlineOrder.getRegisteredTelephone();
            cell = row.createCell(celNum);
            cell.setCellValue(registeredTelephone);
            celNum++;

            //开户银行
            String bankOfDeposit = offlineOrder.getBankOfDeposit();
            cell = row.createCell(celNum);
            cell.setCellValue(bankOfDeposit);
            celNum++;

            //银行账户
            String bankAccount = offlineOrder.getBankAccount();
            cell = row.createCell(celNum);
            cell.setCellValue(bankAccount);
            celNum++;

            //联系人
            String contacts = offlineOrder.getContacts();
            cell = row.createCell(celNum);
            cell.setCellValue(contacts);
            celNum++;

            //联系电话
            String contactNumber = offlineOrder.getContactNumber();
            cell = row.createCell(celNum);
            cell.setCellValue(contactNumber);
            celNum++;

            //快递地址
            String expressAddress = offlineOrder.getExpressAddress();
            cell = row.createCell(celNum);
            cell.setCellValue(expressAddress);
        }
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        orderWorkbook.write(arrayOutputStream);
        String newName = URLEncoder.encode("线下订单-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ".xlsx", "utf-8")
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