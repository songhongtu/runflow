package com.runflow.plugin.redis;

import com.runflow.engine.impl.RunTimeServiceImpl;
import com.runflow.spring.boot.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class RedisPluginApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisPluginApplication.class, args);
    }

}
