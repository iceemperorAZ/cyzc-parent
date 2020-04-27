package com.jingliang.mall.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * excel处理工具类
 *
 * @author Zhenfeng Li
 * @date 2019-12-16 09:46:10
 */
@Slf4j
public class ExcelUtils {

    /**
     * 生成 xlsx格式的excel
     * @param sheetName sheet名称
     * @param titleList 标题列集合
     * @return 返回创建成功的excel
     */
    public static XSSFWorkbook createExcelXlsx(String sheetName, List<String> titleList) {
        // 声明一个工作薄
        XSSFWorkbook workBook = new XSSFWorkbook();
        CellStyle cellStyle = workBook.createCellStyle();
        // 水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        XSSFFont font = workBook.createFont();
        //字体加粗
        font.setBold(true);
        cellStyle.setFont(font);
        // 生成一个表格
        XSSFSheet sheet = workBook.createSheet();
        // 冻结第一行
        sheet.createFreezePane(0, 1, 0, 1);
        workBook.setSheetName(0, sheetName);
        // 创建表格标题行 第一行
        XSSFRow titleRow = sheet.createRow(0);
        for (int i = 0; i < titleList.size(); i++) {
            sheet.setColumnWidth(i, 4866);
            XSSFCell cell = titleRow.createCell(i);
            cell.setCellValue(titleList.get(i));
            cell.setCellStyle(cellStyle);
        }
        return workBook;
    }

    /**
     * 按照excel真实后缀获取内容
     * @param cell        单元格
     * @param rowCellType 单元格类型
     * @return
     * @throws ExcelException
     */
    public static String getCellValue(Cell cell, CellType rowCellType) throws ExcelException {
        String value;
        switch (rowCellType) {
            // 数字
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    if (date != null) {
                        //有 年/月/日
                        if (HSSFDateUtil.isCellInternalDateFormatted(cell)) {
                            //默认的日期格式
                            value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        } else {
                            //默认的时间格式
                            value = new SimpleDateFormat("HH:mm:ss").format(date);
                        }
                    } else {
                        value = null;
                    }
                } else {
                    NumberFormat numberFormat = NumberFormat.getNumberInstance();
                    numberFormat.setGroupingUsed(false);
                    //默认保留8位小数
                    numberFormat.setMaximumFractionDigits(8);
                    value = numberFormat.format(cell.getNumericCellValue());
                }
                break;
            // 字符串
            case STRING:
                value = cell.getStringCellValue();
                break;
            // Boolean
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            //是否存在 嵌套 公式类型
            case FORMULA:
                value = getCellValue(cell, cell.getCachedFormulaResultType());
                break;
            // 空值
            case BLANK:
                value = null;
                break;
            // 故障
            case ERROR:
                value = ErrorEval.getText(cell.getErrorCellValue());
                throw new ExcelException(Msg.TEXT_EXCEL_ANALYSIS_FAIL);
            default:
                value = "未知类型";
                throw new ExcelException(Msg.TEXT_EXCEL_ANALYSIS_FAIL);
        }
        return value;
    }
}
