package com.runflow.plugin.redis.model;


import com.runflow.engine.bpmn.entity.impl.CustomTaskFormPropertyTask;


public class RedisTask extends CustomTaskFormPropertyTask {


    private String type;


    private String redisExpression;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getRedisExpression() {
        return redisExpression;
    }

    public void setRedisExpression(String redisExpression) {
        this.redisExpression = redisExpression;
    }
}
