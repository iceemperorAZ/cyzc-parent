package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.BuyerAddress;
import com.jingliang.mall.entity.BuyerSale;
import com.jingliang.mall.repository.BuyerAddressRepository;
import com.jingliang.mall.repository.BuyerRepository;
import com.jingliang.mall.repository.BuyerSaleRepository;
import com.jingliang.mall.service.BuyerSaleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商户销售绑定表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-01-03 13:10:34
 */
@Service
@Slf4j
public class BuyerSaleServiceImpl implements BuyerSaleService {

    private final BuyerSaleRepository buyerSaleRepository;
    private final BuyerRepository buyerRepository;
    private final BuyerAddressRepository buyerAddressRepository;

    public BuyerSaleServiceImpl(BuyerSaleRepository buyerSaleRepository, BuyerRepository buyerRepository, BuyerAddressRepository buyerAddressRepository) {
        this.buyerSaleRepository = buyerSaleRepository;
        this.buyerRepository = buyerRepository;
        this.buyerAddressRepository = buyerAddressRepository;
    }

    @Override
    public BuyerSale save(BuyerSale buyerSale) {
        return buyerSaleRepository.save(buyerSale);
    }

    @Override
    public List<BuyerSale> saveAll(List<BuyerSale> buyerSale) {
        return buyerSaleRepository.saveAll(buyerSale);
    }

    @Override
    public List<BuyerSale> findAllBySaleIdAndBuyerIdAndIsAvailable(Long saleUserId, Long buyerId) {
        return buyerSaleRepository.findAllBySaleIdAndBuyerIdAndIsAvailable(saleUserId, buyerId, true);
    }

    @Override
    public List<BuyerSale> finAll(Specification<BuyerSale> buyerSaleSpecification) {
        return buyerSaleRepository.findAll(buyerSaleSpecification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Buyer bindingSale(BuyerSale buyerSale, Buyer buyer, BuyerAddress address) {
        buyerSaleRepository.save(buyerSale);
        BuyerAddress buyerAddress = buyerAddressRepository.findFirstByBuyerIdAndIsDefaultAndIsAvailable(buyer.getId(), true, true);
        if (buyerAddress != null) {
            buyerAddress.setIsDefault(false);
            buyerAddressRepository.save(buyerAddress);
        }
        buyerAddressRepository.save(address);
        return buyerRepository.saveAndFlush(buyer);
    }
    /*
    * 查询商户、销售员、区域信息，导入到excel
    * */
    @Override
    public Boolean findBuyerSaleByTime(Date startTime, Date endTime) {

        List<Map<String, Object>> buyerSaleByTime = buyerSaleRepository.findBuyerSaleByTime(startTime, endTime);
        Object time1 = buyerSaleByTime.get(0).get("时间");
        // 定义一个新的工作簿
        XSSFWorkbook wb = new XSSFWorkbook();
        // 创建一个Sheet页
        XSSFSheet sheet = wb.createSheet("First sheet");
        //设置行高
        sheet.setDefaultRowHeight((short) (2 * 256));
        //设置列宽
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        XSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        //获得表格第一行
        XSSFRow row = sheet.createRow(0);
        //根据需要给第一行每一列设置标题
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("时间");
        cell = row.createCell(1);
        cell.setCellValue("商户id");
        cell = row.createCell(2);
        cell.setCellValue("商户名称");
        cell = row.createCell(3);
        cell.setCellValue("商户所在区");
        cell = row.createCell(4);
        cell.setCellValue("商户地址");
        cell = row.createCell(5);
        cell.setCellValue("销售员id");
        cell = row.createCell(6);
        cell.setCellValue("销售员");
        cell = row.createCell(7);
        cell.setCellValue("区域经理");
        cell = row.createCell(8);
        cell.setCellValue("销售员所在区");
        XSSFRow rows;
        XSSFCell cells;
        //循环拿到的数据给所有行每一列设置对应的值
        for (int i = 0; i < buyerSaleByTime.size(); i++) {

            // 在这个sheet页里创建一行
            rows = sheet.createRow(i + 1);
            // 该行创建一个单元格,在该单元格里设置值
            String time = buyerSaleByTime.get(i).get("时间").toString();
            String uid = buyerSaleByTime.get(i).get("商户id").toString();
            String uname = buyerSaleByTime.get(i).get("商户名称").toString();
            String uarea = buyerSaleByTime.get(i).get("商户所在区").toString();
            String buyerAddress = buyerSaleByTime.get(i).get("商户地址").toString().concat("  ").concat(buyerSaleByTime.get(i).get("收货人").toString()).concat("  ").concat(buyerSaleByTime.get(i).get("手机号").toString());
            String sid = buyerSaleByTime.get(i).get("销售员id").toString();
            String sname = buyerSaleByTime.get(i).get("销售员").toString();
            String manager = buyerSaleByTime.get(i).get("区域经理").toString();
            String sarea = buyerSaleByTime.get(i).get("销售员所在区").toString();
            cells = rows.createCell(0);
            cells.setCellValue(time);
            cells = rows.createCell(1);
            cells.setCellValue(uid);
            cells = rows.createCell(2);
            cells.setCellValue(uname);
            cells = rows.createCell(3);
            cells.setCellValue(uarea);
            cells = rows.createCell(4);
            cells.setCellValue(buyerAddress);
            cells = rows.createCell(5);
            cells.setCellValue(sid);
            cells = rows.createCell(6);
            cells.setCellValue(sname);
            cells = rows.createCell(7);
            cells.setCellValue(manager);
            cells = rows.createCell(8);
            cells.setCellValue(sarea);

        }
        try {
            File file = new File("d:/商户信息.xlsx");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            wb.write(fileOutputStream);
            wb.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
    * 查询商户、销售员、区域信息，展示给前端页面
    * */
    @Override
    public List<Map<String, Object>> findBuyerSaleByTimeToHtml(Date startTime, Date endTime) {
        return buyerSaleRepository.findBuyerSaleByTimeToHtml(startTime,endTime);
    }
}