package com.runflow.plugin.mybatis;

import com.runflow.engine.RunTimeService;
import com.runflow.engine.impl.RunTimeServiceImpl;
import com.runflow.spring.boot.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyBatisPluginApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyBatisPluginApplication.class, args);


        RunTimeServiceImpl bean = SpringContextUtil.getApplicationContext().getBean(RunTimeServiceImpl.class);
        bean.startWorkflow("Process_1677056136325");
    }
}
