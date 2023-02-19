package com.runflow.plugin.mybatis.bpmn.handler;

import com.runflow.engine.parse.AbstractActivityBpmnParseHandler;
import com.runflow.engine.parse.BpmnParse;
import com.runflow.plugin.mybatis.behavior.MybatisTaskBehavior;
import com.runflow.plugin.mybatis.model.MyBatisTask;
import org.activiti.bpmn.model.BaseElement;

public class MyBatisTaskHandler extends AbstractActivityBpmnParseHandler<MyBatisTask> {

    public Class<? extends BaseElement> getHandledType() {
        return MyBatisTask.class;
    }

    @Override
    protected void executeParse(BpmnParse bpmnParse, MyBatisTask mybatisTask) {
        mybatisTask.setBehavior(new MybatisTaskBehavior(mybatisTask));
    }

}