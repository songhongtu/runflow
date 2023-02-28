package com.runflow.plugin.redis.config;

import com.runflow.engine.parse.BpmnParseHandler;
import com.runflow.plugin.redis.bpmn.converter.RedisTaskXMLConverter;
import com.runflow.plugin.redis.bpmn.handler.RedisTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisRunFlowConfiguration {
    @Bean
    public BpmnParseHandler redisTaskHandler() {
        return new RedisTaskHandler();
    }

    @Bean
    public RedisTaskXMLConverter redisTaskXMLConverter() {
        return new RedisTaskXMLConverter();
    }

    @Bean
    public RedisContextElResolver redisContextElResolver(RedisTemplate redisTemplate) {
        return new RedisContextElResolver(redisTemplate);
    }


}
