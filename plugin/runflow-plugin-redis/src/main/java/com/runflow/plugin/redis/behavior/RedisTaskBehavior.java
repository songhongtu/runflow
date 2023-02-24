package com.runflow.plugin.redis.behavior;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.behavior.TaskActivityBehavior;
import com.runflow.engine.behavior.UserTaskActivityBehavior;
import com.runflow.engine.utils.CollectionUtil;
import com.runflow.engine.utils.ConditionUtil;
import com.runflow.engine.utils.Conv;
import com.runflow.plugin.redis.config.RedisContextElResolver;
import com.runflow.plugin.redis.constant.RedisTaskConstant;
import com.runflow.plugin.redis.model.RedisTask;
import com.runflow.spring.boot.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisTaskBehavior extends TaskActivityBehavior implements RedisTaskConstant {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskActivityBehavior.class);

    protected RedisTask redisTask;

    public RedisTaskBehavior(RedisTask redisTask) {
        this.redisTask = redisTask;
    }

    @Override
    public void execute(ExecutionEntityImpl execution) {


        String name = execution.getCurrentFlowElement().getName();
        LOGGER.debug("redis任务  名称：{}  id:{}  线程名称:{} ", name, execution.getId(), Thread.currentThread().getName());
        String redisExpression = redisTask.getRedisExpression();
        String el = Conv.NS(redisExpression).trim();
        if (!el.equals("")) {

            String type = Conv.NS(redisTask.getType(), DEFAULT_TYPE);
            String s = "${" + type + "." + redisExpression + "}";
            Object expression = ConditionUtil.createExpression(s, execution);
            ExecutionEntityImpl rootParent = execution.findRootParent(execution);
            if(expression!=null){
                rootParent.variableInstances.put(execution.getActivityId(), expression);

                rootParent.variableInstances.put(execution.getActivityId()+"_STATE", true);

            }else{
                rootParent.variableInstances.put(execution.getActivityId()+"_STATE", false);
            }


        }
        leave(execution);
    }
}
