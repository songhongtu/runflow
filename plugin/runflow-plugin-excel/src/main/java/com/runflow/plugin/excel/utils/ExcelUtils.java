package com.runflow.plugin.excel.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExcelUtils {




    public static Workbook getWorkbook(List<String> titleList, List<List> dataList) {
        // 创建工作簿
        Workbook workbook = new SXSSFWorkbook(); //生成.xlsx的excel
        // 创建工作表
        Sheet sheet = workbook.createSheet();

        // 构建头单元格样式
        CellStyle cellStyle = buildHeadCellStyle(sheet.getWorkbook());
        cellStyle.setWrapText(true);//自动换行

//       log.info("开始创建标题行...");
        Row head = sheet.createRow(0);//列是从0开始计算的，我这里空了一行

        //第1-119列标题
        int i = 0;
        for (String titie : titleList) {
            Cell cell = head.createCell(i);
            cell.setCellValue(titie);
            cell.setCellStyle(cellStyle);
            i++;
        }

//        log.info("开始处理数据...");
        int rowNum = 1; //从标题下一行开始
        for (int j = 0; j < dataList.size(); j++) {
            // 构建每行的数据内容
            Row row = sheet.createRow(rowNum++);
            convertDataToRow(dataList.get(j), row);
        }

        return workbook;
    }

    private static void convertDataToRow(List<String> data, Row row) {
        for (int i = 0; i < data.size(); i++) {
            String value = data.get(i);
            Cell cell = row.createCell(i);
            cell.setCellValue(value);
        }

    }

    private static CellStyle buildHeadCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        //对齐方式设置 左右居中，上下局上
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        //边框颜色和宽度设置
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 下边框
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边框
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex()); // 右边框
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上边框
        //设置背景颜色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //粗体字设置
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }


    public static void main(String[] args) throws IOException {
        ExcelUtils excelUtils = new ExcelUtils();
        Workbook workbook = excelUtils.getWorkbook(Arrays.asList("1", "2", "3"), Arrays.asList(Arrays.asList("4", "5", "6"), Arrays.asList("7", "8", "9")));
        File file = new File("C:\\Users\\songhongtu\\Desktop\\123.xlsx");

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            workbook.write(fileOutputStream);
        }

    }


}
