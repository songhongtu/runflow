package com.runflow.engine.parse.handler;

import com.runflow.engine.behavior.UserTaskActivityBehavior;
import com.runflow.engine.parse.AbstractActivityBpmnParseHandler;
import com.runflow.engine.parse.BpmnParse;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.UserTask;

public class UserTaskParseHandler extends AbstractActivityBpmnParseHandler<UserTask> {

    public Class<? extends BaseElement> getHandledType() {
        return UserTask.class;
    }

    @Override
    protected void executeParse(BpmnParse bpmnParse, UserTask userTask) {
        userTask.setBehavior(new UserTaskActivityBehavior(userTask));
    }

}