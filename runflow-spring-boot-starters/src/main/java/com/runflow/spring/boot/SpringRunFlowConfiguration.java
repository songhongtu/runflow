package com.runflow.spring.boot;

import com.runflow.engine.impl.RunTimeServiceImpl;
import com.runflow.engine.parse.AbstractActivityBpmnParseHandler;
import com.runflow.engine.parse.BpmnParseHandler;
import org.activiti.bpmn.converter.BaseBpmnXMLConverter;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.el.ELResolver;
import java.util.List;

public class SpringRunFlowConfiguration {
    @Value("${spring.runflow.location:classpath:**/bpmn/**/*.bpmn}")
    private String location;

    @Bean
    @ConditionalOnMissingBean
    public SpringProcessEngineFactoryBean processEngineConfigurationBean(ResourcePatternResolver resourcePatternResolver,
                                                                         List<BpmnParseHandler> handlers,
                                                                         BpmnXMLConverter bpmnXMLConverter,
                                                                         List<ELResolver> elResolvers
    ) {
        SpringProcessEngineFactoryBean processEngineConfiguration = new SpringProcessEngineFactoryBean();
        processEngineConfiguration.setLocation(location);
        processEngineConfiguration.setActivityBpmnParseHandlerList(handlers);
        processEngineConfiguration.setResourceLoader(resourcePatternResolver);
        processEngineConfiguration.setBpmnXMLConverter(bpmnXMLConverter);
        processEngineConfiguration.setResolverList(elResolvers);
        return processEngineConfiguration;
    }


    @Bean
    @ConditionalOnMissingBean
    public BpmnXMLConverter bpmnXMLConverter(List<BaseBpmnXMLConverter> list) {
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        for (BaseBpmnXMLConverter baseBpmnXMLConverter : list) {
            BpmnXMLConverter.addConverter(baseBpmnXMLConverter);
        }
        return bpmnXMLConverter;
    }

    @Bean
    @ConditionalOnMissingBean
    public RunTimeServiceImpl runTimeServiceBean(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
        return springProcessEngineConfiguration.getRunTimeService();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }


    @Bean
    @ConditionalOnMissingBean
    public ApplicationContextElResolver applicationContextElResolver(ApplicationContext applicationContext) {
        return new ApplicationContextElResolver(applicationContext);
    }


}
