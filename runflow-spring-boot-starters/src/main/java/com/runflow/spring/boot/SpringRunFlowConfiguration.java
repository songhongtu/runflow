package com.runflow.spring.boot;

import com.runflow.engine.impl.RunTimeServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.ResourcePatternResolver;

public class SpringRunFlowConfiguration {
    @Value("${spring.runflow.location:classpath:**/bpmn/**/*.bpmn}")
    private String location;

    @Bean
    @ConditionalOnMissingBean
    public SpringProcessEngineFactoryBean processEngineConfigurationBean(ResourcePatternResolver resourcePatternResolver) {
        SpringProcessEngineFactoryBean processEngineConfiguration = new SpringProcessEngineFactoryBean();
        processEngineConfiguration.setLocation(location);
        processEngineConfiguration.setResourceLoader(resourcePatternResolver);
        return processEngineConfiguration;
    }

    @Bean
    @ConditionalOnMissingBean
    public RunTimeServiceImpl runTimeServiceBean(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
        return springProcessEngineConfiguration.getRunTimeService();
    }


}
