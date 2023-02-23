package com.runflow.spring.boot;

import com.runflow.engine.parse.AbstractActivityBpmnParseHandler;
import com.runflow.engine.parse.BpmnParseHandler;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.el.ELResolver;
import java.util.List;

public class SpringProcessEngineFactoryBean implements FactoryBean<SpringProcessEngineConfiguration>, ApplicationContextAware {
    private String location;
    private ApplicationContext applicationContext;
    private ResourcePatternResolver resourceLoader;

    private List<BpmnParseHandler> activityBpmnParseHandlerList;


    private BpmnXMLConverter bpmnXMLConverter;


    private List<ELResolver> resolverList;

    @Override
    public SpringProcessEngineConfiguration getObject() throws Exception {
        SpringProcessEngineConfiguration springProcessEngineConfiguration = new SpringProcessEngineConfiguration();
        springProcessEngineConfiguration.setApplicationContext(applicationContext);
        springProcessEngineConfiguration.setResourceLoader(resourceLoader);
        springProcessEngineConfiguration.setLocation(location);
        springProcessEngineConfiguration.setCustomDefaultBpmnParseHandlers(activityBpmnParseHandlerList);
        springProcessEngineConfiguration.getResolverList().addAll(resolverList);
        springProcessEngineConfiguration.init();
        return springProcessEngineConfiguration;
    }

    @Override
    public Class<?> getObjectType() {
        return SpringProcessEngineConfiguration.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ResourcePatternResolver getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(ResourcePatternResolver resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    public List<BpmnParseHandler> getActivityBpmnParseHandlerList() {
        return activityBpmnParseHandlerList;
    }

    public void setActivityBpmnParseHandlerList(List<BpmnParseHandler> activityBpmnParseHandlerList) {
        this.activityBpmnParseHandlerList = activityBpmnParseHandlerList;
    }

    public BpmnXMLConverter getBpmnXMLConverter() {
        return bpmnXMLConverter;
    }

    public void setBpmnXMLConverter(BpmnXMLConverter bpmnXMLConverter) {
        this.bpmnXMLConverter = bpmnXMLConverter;
    }

    public List<ELResolver> getResolverList() {
        return resolverList;
    }

    public void setResolverList(List<ELResolver> resolverList) {
        this.resolverList = resolverList;
    }
}
