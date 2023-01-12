package com.runflow.spring.boot;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.support.ResourcePatternResolver;

public class SpringProcessEngineFactoryBean implements FactoryBean<SpringProcessEngineConfiguration>, ApplicationContextAware {
    private String location;
    private ApplicationContext applicationContext;
    private ResourcePatternResolver resourceLoader;

    @Override
    public SpringProcessEngineConfiguration getObject() throws Exception {
        SpringProcessEngineConfiguration springProcessEngineConfiguration = new SpringProcessEngineConfiguration();
        springProcessEngineConfiguration.setApplicationContext(applicationContext);
        springProcessEngineConfiguration.setResourceLoader(resourceLoader);
        springProcessEngineConfiguration.setLocation(location);
        springProcessEngineConfiguration.getResolverList().add(new ApplicationContextElResolver(applicationContext));
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
}
