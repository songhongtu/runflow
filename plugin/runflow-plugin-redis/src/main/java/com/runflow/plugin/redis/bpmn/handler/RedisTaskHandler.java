package com.runflow.plugin.redis.bpmn.handler;

import com.runflow.engine.parse.AbstractActivityBpmnParseHandler;
import com.runflow.engine.parse.BpmnParse;
import com.runflow.plugin.redis.behavior.RedisTaskBehavior;
import com.runflow.plugin.redis.model.RedisTask;
import org.activiti.bpmn.model.BaseElement;

public class RedisTaskHandler extends AbstractActivityBpmnParseHandler<RedisTask> {

    public Class<? extends BaseElement> getHandledType() {
        return RedisTask.class;
    }

    @Override
    protected void executeParse(BpmnParse bpmnParse, RedisTask redisTask) {
        redisTask.setBehavior(new RedisTaskBehavior(redisTask));
    }

}