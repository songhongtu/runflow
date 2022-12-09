package com.runflow.engine;

import com.runflow.engine.bpmn.entity.Deployment;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.impl.ProcessEngineImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import com.runflow.engine.utils.ConditionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;


public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    ProcessEngineConfigurationImpl conf = new ProcessEngineConfigurationImpl();
    ProcessEngineImpl processEngine = conf.buildProcessEngine();


    public static void main(String[] args) {

        Application application = new Application();

        RunTimeServiceImpl repositoryService = application.conf.getRepositoryService();
        String path = Application.class.getClassLoader().getResource("").getPath();//注意getResource("")里面是空字符串

        File file = new File(path + "/bpmn/");
        for (File f : file.listFiles()) {
            try {
                Deployment deploy = repositoryService.createDeployment().name(f.getName()).addInputStream(f.getName(), new FileInputStream(f)).deploy();
            } catch (Exception e) {
                logger.error("文件：{}部署错误", file.getName());
            }

        }

        Map<String, Object> map = new HashMap();
        map.put("conditionUtil", new ConditionUtil());
        map.put("exclusivegateway1", true);
        map.put("deptleaderapprove", true);
        map.put("hrapprove", false);
        map.put("reapply", true);


        application.conf.getRepositoryService().startWorkflow("Process_1");
        application.conf.getExecutorService().shutdown();
//
    }
}

