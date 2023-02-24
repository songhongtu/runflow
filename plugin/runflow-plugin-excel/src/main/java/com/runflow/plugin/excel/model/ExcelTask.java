package com.runflow.plugin.excel.model;


import com.runflow.engine.bpmn.entity.impl.CustomTaskFormPropertyTask;


public class ExcelTask extends CustomTaskFormPropertyTask {

    private String excelExpression;


    public String getExcelExpression() {
        return excelExpression;
    }

    public void setExcelExpression(String excelExpression) {
        this.excelExpression = excelExpression;
    }
}
