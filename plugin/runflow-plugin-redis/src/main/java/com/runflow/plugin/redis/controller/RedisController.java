package com.runflow.plugin.redis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import com.runflow.plugin.redis.model.RedisTask;
import com.runflow.spring.boot.SpringContextUtil;
import com.runflow.spring.boot.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@RestController("redisController")
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ApplicationContext applicationContext;


    public void tt(String key) {
        System.out.println(key);
    }

    @GetMapping("/demo1")
    public Map<String, Object> demo1() throws JsonProcessingException {
        SpringProcessEngineConfiguration.refresh();
        RunTimeServiceImpl bean = SpringContextUtil.getApplicationContext().getBean(RunTimeServiceImpl.class);
        ExecutionEntityImpl executionEntity = bean.startWorkflow("Process_1677138255418");
        return ((Map<String, Object>) executionEntity.getVariableInstances());
    }


}
