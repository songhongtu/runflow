package com.runflow.plugin.mybatis.config;

import com.runflow.engine.parse.BpmnParseHandler;
import com.runflow.plugin.mybatis.bpmn.converter.MyBatisTaskXMLConverter;
import com.runflow.plugin.mybatis.bpmn.handler.MyBatisTaskHandler;
import org.springframework.context.annotation.Bean;

public class MybatisRunFlowConfiguration {
    @Bean
    public BpmnParseHandler myBatisBmpnParseHandler() {
        return new MyBatisTaskHandler();
    }

    @Bean
    public MyBatisTaskXMLConverter myBatisTaskXMLConverter() {
        return new MyBatisTaskXMLConverter();
    }


}
