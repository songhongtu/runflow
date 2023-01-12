package com.runflow.spring.boot;

import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.ArrayList;
import java.util.List;

public class SpringProcessEngineConfiguration extends ProcessEngineConfigurationImpl {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SpringProcessEngineConfiguration.class);

    private String location;

    private ResourcePatternResolver resourceLoader;

    private ApplicationContext applicationContext;

    private List<Resource> resourceList;


    protected void discoverLocation() {
        try {
            Resource[] resources = resourceLoader.getResources(location);
            for (Resource resource : resources) {
                this.resourceList.add(resource);
            }
        } catch (Exception e) {
            logger.error("文件异常：{}", e.getMessage());
        }

    }

    public ResourcePatternResolver getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(ResourcePatternResolver resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    //扫描bpmn文件，确保只扫描一次
    public void baseScan() throws Exception {
        resourceList = new ArrayList<>();
        discoverLocation();
        logger.info("文件开始部署：{}", resourceList);
        for (Resource resource : resourceList) {
            deploy(resource.getFilename(), resource.getInputStream());
        }
    }


}
