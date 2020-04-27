package com.jingliang.mall.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lmd
 * @date 2020/4/27
 * 读取excel
 * @company 晶粮
 */
public class ReadExcelUtil {
    /**
     * java读取excel表格,返回List<String>
     */
    public static List<String> readExcel(File file) {
        List<String> list = new ArrayList<>();
        try {
            //拿到对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            //一个excel可能有多个sheet,所以遍历sheet
            for (int k = 0; k < workbook.getNumberOfSheets(); k++) {
                //读取每个 sheet 工作表对象
                Sheet sheet = workbook.getSheetAt(k);
                //获取excel总行数
                int count = sheet.getPhysicalNumberOfRows();
                //逐行处理 excel 数据
                for (int i = 0; i < count; i++) {
                    //跳过首行(标题行)
                    if (i == 0) {
                        continue;
                    }
                    //获取行对象
                    Row row = sheet.getRow(i);
                    //跳过空行
                    if (row == null) {
                        continue;
                    }
                    //获取列值
                    Cell cell = row.getCell(0);
                    //设置取值为String
                    if (StringUtils.isBlank(cell.toString())) {
                        continue;
                    }
                    list.add(cell.toString());
                }
                workbook.close();
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}


