package com.runflow.plugin.excel.config;

import com.runflow.engine.parse.BpmnParseHandler;
import com.runflow.plugin.excel.bpmn.converter.ExcelTaskXMLConverter;
import com.runflow.plugin.excel.bpmn.handler.ExcelTaskHandler;
import com.runflow.plugin.excel.model.ExcelTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class ExcelRunFlowConfiguration {
    @Bean
    public BpmnParseHandler excelTashHandler() {
        return new ExcelTaskHandler();
    }

    @Bean
    public ExcelTaskXMLConverter excelTaskXMLConverter() {
        return new ExcelTaskXMLConverter();
    }


}
