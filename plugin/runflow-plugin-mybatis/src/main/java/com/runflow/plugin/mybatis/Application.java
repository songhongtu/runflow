package com.runflow.plugin.mybatis;

import com.runflow.engine.RunTimeService;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import com.runflow.plugin.mybatis.bpmn.converter.MyBatisTaskXMLConverter;
import com.runflow.plugin.mybatis.bpmn.handler.MyBatisTaskHandler;
import com.runflow.spring.boot.SpringContextUtil;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        RunTimeServiceImpl bean = SpringContextUtil.getBean(RunTimeServiceImpl.class);
        bean.startWorkflow("Process_16719365975");
    }
}
