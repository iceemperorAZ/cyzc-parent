package com.jingliang.mall.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.OfflineOrder;
import com.jingliang.mall.resp.OfflineOrderResp;
import com.jingliang.mall.service.OfflineOrderService;
import com.jingliang.mall.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
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
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(name = "createTimeStart", required = false) Date createTimeStart,
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(name = "createTimeEnd", required = false) Date createTimeEnd,
                                                      Integer rate, @RequestParam(value = "orderStatuses", required = false) List<Integer> orderStatuses,
                                                      @RequestParam(value = "regions", required = false) List<String> regions,
                                                      @RequestParam(value = "provinces", required = false) List<String> provinces,
                                                      @RequestParam(value = "cities", required = false) List<String> cities,
                                                      @RequestParam(value = "counties", required = false) List<String> counties) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Specification<OfflineOrder> specification = (Specification<OfflineOrder>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (createTimeStart != null && createTimeEnd != null) {
                predicateList.add(cb.between(root.get("createTime"), createTimeStart, createTimeEnd));
            }
            if (rate != null) {
                predicateList.add(cb.equal(root.get("rate"), rate));
            }
            List<Predicate> orderStatusList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(orderStatuses)) {
                for (Integer orderStatus : orderStatuses) {
                    orderStatusList.add(cb.equal(root.get("orderStatus"), orderStatus));
                }
                predicateList.add(cb.or(orderStatusList.toArray(new Predicate[0])));
            }
            List<Predicate> regionList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(regions)) {
                for (String region : regions) {
                    regionList.add(cb.equal(root.get("region"), region));
                }
                predicateList.add(cb.or(regionList.toArray(new Predicate[0])));
            }
            List<Predicate> provinceList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(provinces)) {
                for (String province : provinces) {
                    provinceList.add(cb.equal(root.get("province"), province));
                }
                predicateList.add(cb.or(provinceList.toArray(new Predicate[0])));
            }
            List<Predicate> cityList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(cities)) {
                for (String city : cities) {
                    cityList.add(cb.equal(root.get("city"), city));
                }
                predicateList.add(cb.or(cityList.toArray(new Predicate[0])));
            }
            List<Predicate> countyList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(counties)) {
                for (String county : counties) {
                    countyList.add(cb.equal(root.get("county"), county));
                }
                predicateList.add(cb.or(countyList.toArray(new Predicate[0])));
            }
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
        return Result.build(Msg.OK, "解锁成功", offlineOrderService.unlock(id));
    }

    /**
     * 修改订单状态
     */
    @PostMapping("/back/offlineOrder/status")
    public Result<Boolean> status(@RequestBody Map<String, Object> map) {
        Long id = (Long) map.get("id");
        Integer status = (Integer) map.get("status");
        return Result.build(Msg.OK, "修改订单状态成功", offlineOrderService.status(id, status));
    }

    /**
     * 修改发票进度为已完成
     */
    @PostMapping("/back/offlineOrder/rate/success")
    public Result<Boolean> success(@RequestBody Map<String, Long> map) {
        Long id = map.get("id");
        return Result.build(Msg.OK, "开具发票进度修改成功", offlineOrderService.success(id));
    }

    /**
     * excel导出全部 (同时会进行订单锁定)
     */
    @GetMapping("/back/offlineOrder/down")
    public ResponseEntity<byte[]> down(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(name = "createTimeStart", required = false) Date createTimeStart,
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(name = "createTimeEnd", required = false) Date createTimeEnd,
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
        XSSFWorkbook orderWorkbook = ExcelUtils.createExcelXlsx("线下订单", Msg.offlineOrderExcelTitle);
        XSSFSheet sheet = orderWorkbook.getSheet("线下订单");
        XSSFCellStyle cellStyle = orderWorkbook.createCellStyle();
        CreationHelper createHelper = orderWorkbook.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/MM/dd HH:mm:ss"));
        XSSFCellStyle doubleCellStyle = orderWorkbook.createCellStyle();
        doubleCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

        int r = 1;
        for (OfflineOrder order : offlineOrderList) {
            XSSFRow row = sheet.createRow(r);
            int celNum = 0;
            XSSFCell cell;

            //商品名称
            String productName = order.getProductName();
            String[] productNames = productName.split("\\/");

            //商品规格
            String productSpecification = order.getProductSpecification();
            String[] productSpecifications = productSpecification.split("\\/");

            //单位
            String company = order.getCompany();
            String[] companys = company.split("\\/");

            //数量
            String num = order.getNum();
            String[] nums = num.split("\\/");

            //单价(单位：分)
            String unitPrice = order.getUnitPrice();
            String[] unitPrices = unitPrice.split("\\/");


            int detailCelNum = celNum;
            XSSFRow detailRow = row;
            for (int j = 0; j < productNames.length; j++) {

                //客户要求送货日期和时间
                Date deliveryTime = order.getDeliveryTime();
                cell = detailRow.createCell(detailCelNum);
                cell.setCellValue(deliveryTime);
                cell.setCellStyle(cellStyle);
                detailCelNum++;

                //商品名称
                cell = detailRow.createCell(detailCelNum);
                cell.setCellValue(productNames[j]);
                detailCelNum++;

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
                cell.setCellValue(Double.parseDouble(unitPrices[j]) / 100);
                cell.setCellStyle(doubleCellStyle);

                //换行
                detailRow = sheet.createRow(++r);
                detailCelNum = celNum;
            }
            celNum += 6;

            //总价(单位：分)
            //空一行
            row.createCell(celNum);
            celNum++;

            //商铺名称
            String shopName = order.getShopName();
            cell = row.createCell(celNum);
            cell.setCellValue(shopName);
            celNum++;

            //客户姓名
            String customerName = order.getCustomerName();
            cell = row.createCell(celNum);
            cell.setCellValue(customerName);
            celNum++;

            //客户电话
            String customerPhone = order.getCustomerPhone();
            cell = row.createCell(celNum);
            cell.setCellValue(customerPhone);
            celNum++;

            //省
            String province = order.getProvince();
            //市
            String city = order.getCity();
            //区/县
            String county = order.getCounty();
            //客户地址
            String customerAddress = order.getCustomerAddress();
            cell = row.createCell(celNum);
            cell.setCellValue(province + "/" + city + "/" + county + "/" + customerAddress);
            celNum++;

            //业务员姓名
            String salesmanName = order.getSalesmanName();
            cell = row.createCell(celNum);
            cell.setCellValue(salesmanName);
            celNum++;

            //业务员电话
            String salesmanPhone = order.getSalesmanPhone();
            cell = row.createCell(celNum);
            cell.setCellValue(salesmanPhone);
            celNum++;

            //备注
            String remarks = order.getRemarks();
            cell = row.createCell(celNum);
            cell.setCellValue(remarks);
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

    /**
     * excel导出全部 (同时会进行订单锁定)
     */
    @GetMapping("/back/offlineOrder/finance/down")
    public ResponseEntity<byte[]> financeDown(@ApiIgnore @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                              @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") Date createTimeStart,
                                              @ApiIgnore @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                              @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") Date createTimeEnd,
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
        List<OfflineOrder> offlineOrderList = offlineOrderService.financeDown(specification);
        //生产excel
        XSSFWorkbook orderWorkbook = ExcelUtils.createExcelXlsx("线下订单", Msg.offlineOrderExcelTitle2);
        XSSFSheet sheet = orderWorkbook.getSheet("线下订单");
        XSSFCellStyle cellStyle = orderWorkbook.createCellStyle();
        CreationHelper createHelper = orderWorkbook.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/MM/dd HH:mm:ss"));
        XSSFCellStyle doubleCellStyle = orderWorkbook.createCellStyle();
        doubleCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

        //绿颜色背景
        XSSFCellStyle greenCellStyle = orderWorkbook.createCellStyle();
        greenCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        greenCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //黄颜色背景
        XSSFCellStyle yellowCellStyle = orderWorkbook.createCellStyle();
        yellowCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        yellowCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        int r = 1;
        for (OfflineOrder order : offlineOrderList) {
            XSSFRow row = sheet.createRow(r);

            //商铺名称
            int celNum = 0;
            String shopName = order.getShopName();
            XSSFCell cell = row.createCell(celNum);
            cell.setCellValue(shopName);
            celNum++;

            //客户姓名
            String customerName = order.getCustomerName();
            cell = row.createCell(celNum);
            cell.setCellValue(customerName);
            celNum++;

            //客户电话
            String customerPhone = order.getCustomerPhone();
            cell = row.createCell(celNum);
            cell.setCellValue(customerPhone);
            celNum++;

            //商品名称
            String productName = order.getProductName();
            String[] productNames = productName.split("\\/");

            //商品规格
            String productSpecification = order.getProductSpecification();
            String[] productSpecifications = productSpecification.split("\\/");

            //单位
            String company = order.getCompany();
            String[] companys = company.split("\\/");

            //数量
            String num = order.getNum();
            String[] nums = num.split("\\/");

            //单价(单位：分)
            String unitPrice = order.getUnitPrice();
            String[] unitPrices = unitPrice.split("\\/");

            int detailCelNum = celNum;
            XSSFRow detailRow = row;
            for (int j = 0; j < productNames.length; j++) {

                //商品名称
                cell = detailRow.createCell(detailCelNum);
                cell.setCellValue(productNames[j]);
                detailCelNum++;

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

                //单价(单位：元)
                cell = detailRow.createCell(detailCelNum);
                cell.setCellValue(Double.parseDouble(unitPrices[j]) / 100);
                cell.setCellStyle(doubleCellStyle);

                //换行
                detailRow = sheet.createRow(++r);
                detailCelNum = celNum;
            }
            celNum += 5;

            //总价(单位：元)
            String totalPrice = order.getTotalPrice();
            cell = row.createCell(celNum);
            cell.setCellValue(Double.parseDouble(totalPrice) / 100);
            cell.setCellStyle(doubleCellStyle);
            celNum++;

            //省
            String province = order.getProvince();
            cell = row.createCell(celNum);
            cell.setCellValue(province);
            celNum++;

            //市
            String city = order.getCity();
            cell = row.createCell(celNum);
            cell.setCellValue(city);
            celNum++;

            //区/县
            String county = order.getCounty();
            cell = row.createCell(celNum);
            cell.setCellValue(county);
            celNum++;

            //客户地址
            String customerAddress = order.getCustomerAddress();
            cell = row.createCell(celNum);
            cell.setCellValue(customerAddress);
            celNum++;

            //客户要求送货日期和时间
            Date deliveryTime = order.getDeliveryTime();
            cell = row.createCell(celNum);
            cell.setCellValue(deliveryTime);
            cell.setCellStyle(cellStyle);
            celNum++;

            //业务员Id
            Long salesmanId = order.getSalesmanId();
            cell = row.createCell(celNum);
            cell.setCellValue(salesmanId);
            celNum++;

            //业务员姓名
            String salesmanName = order.getSalesmanName();
            cell = row.createCell(celNum);
            cell.setCellValue(salesmanName);
            celNum++;

            //业务员工号
            String salesmanNo = order.getSalesmanNo();
            cell = row.createCell(celNum);
            cell.setCellValue(salesmanNo);
            celNum++;

            //业务员电话
            String salesmanPhone = order.getSalesmanPhone();
            cell = row.createCell(celNum);
            cell.setCellValue(salesmanPhone);
            celNum++;

            //备注
            String remarks = order.getRemarks();
            cell = row.createCell(celNum);
            cell.setCellValue(remarks);
            celNum++;

            //创建时间
            Date createTime = order.getCreateTime();
            cell = row.createCell(celNum);
            cell.setCellValue(createTime);
            cell.setCellStyle(cellStyle);
            celNum++;

            //开具发票进度
            Integer rate1 = order.getRate();
            cell = row.createCell(celNum);
            switch (rate1) {
                case 100:
                    cell.setCellValue("无需发票");
                    continue;
                case 200:
                    cell.setCellValue("待开发票");
                    cell.setCellStyle(yellowCellStyle);
                    break;
                default:
                    cell.setCellStyle(greenCellStyle);
                    cell.setCellValue("已开发票");
            }
            celNum++;

            //发票类型
            Integer type = order.getType();
            cell = row.createCell(celNum);
            if (type == 100) {
                cell.setCellValue("增值税专用发票");
            } else if (type == 200) {
                cell.setCellValue("增值税普通发票");
            }
            celNum++;

            //单位名称
            String unitName = order.getUnitName();
            cell = row.createCell(celNum);
            cell.setCellValue(unitName);
            celNum++;

            //纳税人识别码
            String taxpayerIdentificationNumber = order.getTaxpayerIdentificationNumber();
            cell = row.createCell(celNum);
            cell.setCellValue(taxpayerIdentificationNumber);
            celNum++;

            //注册地址
            String registeredAddress = order.getRegisteredAddress();
            cell = row.createCell(celNum);
            cell.setCellValue(registeredAddress);
            celNum++;

            //注册电话
            String registeredTelephone = order.getRegisteredTelephone();
            cell = row.createCell(celNum);
            cell.setCellValue(registeredTelephone);
            celNum++;

            //开户银行
            String bankOfDeposit = order.getBankOfDeposit();
            cell = row.createCell(celNum);
            cell.setCellValue(bankOfDeposit);
            celNum++;

            //银行账户
            String bankAccount = order.getBankAccount();
            cell = row.createCell(celNum);
            cell.setCellValue(bankAccount);
            celNum++;

            //联系人
            String contacts = order.getContacts();
            cell = row.createCell(celNum);
            cell.setCellValue(contacts);
            celNum++;

            //联系电话
            String contactNumber = order.getContactNumber();
            cell = row.createCell(celNum);
            cell.setCellValue(contactNumber);
            celNum++;

            //快递地址
            String expressAddress = order.getExpressAddress();
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