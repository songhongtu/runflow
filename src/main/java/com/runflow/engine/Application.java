package com.runflow.engine;

import com.runflow.engine.bpmn.entity.impl.DefaultDeploymentCache;
import com.runflow.engine.bpmn.entity.impl.ProcessDefinitionCacheEntry;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.impl.ProcessEngineImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import com.runflow.engine.utils.ConditionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;


public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    ProcessEngineConfigurationImpl conf = new ProcessEngineConfigurationImpl();
    ProcessEngineImpl processEngine = conf.buildProcessEngine();


    public static void main(String[] args) {

        Application application = new Application();

        RunTimeServiceImpl repositoryService = application.conf.getRepositoryService();
        String path = Application.class.getClassLoader().getResource("").getPath();//注意getResource("")里面是空字符串

        File file = new File(path + application.conf.getResourcePath());
        for (File f : file.listFiles()) {
            try {
                repositoryService.createDeployment().name(f.getName()).addInputStream(f.getName(), new FileInputStream(f)).deploy();
            } catch (Exception e) {
                logger.error("文件：{}部署错误", f.getPath());
            }

        }

        Map<String, Object> map = new HashMap();
        map.put("conditionUtil", new ConditionUtil());
        map.put("exclusivegateway1", true);
        map.put("deptleaderapprove", true);
        map.put("hrapprove", false);
        map.put("reapply", true);
        DefaultDeploymentCache<ProcessDefinitionCacheEntry> processDefinitionCache = application.conf.getProcessDefinitionCache();
        System.out.println(processDefinitionCache);
        application.conf.getExecutorService().shutdown();
//
    }
}

