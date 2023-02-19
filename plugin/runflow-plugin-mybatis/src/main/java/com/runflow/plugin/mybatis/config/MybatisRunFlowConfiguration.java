package com.runflow.plugin.mybatis.config;

import com.runflow.engine.parse.BpmnParseHandler;
import com.runflow.plugin.mybatis.bpmn.converter.MyBatisTaskXMLConverter;
import com.runflow.plugin.mybatis.bpmn.handler.MyBatisTaskHandler;
import com.runflow.spring.boot.SpringProcessEngineFactoryBean;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;

@Configuration
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
