package com.runflow.engine.parse.handler;

import com.runflow.engine.RunFlowException;
import com.runflow.engine.behavior.NoneEndEventActivityBehavior;
import com.runflow.engine.parse.AbstractActivityBpmnParseHandler;
import com.runflow.engine.parse.BpmnParse;
import org.activiti.bpmn.model.*;

public class EndEventParseHandler  extends AbstractActivityBpmnParseHandler<EndEvent> {


    public Class<? extends BaseElement> getHandledType() {
        return EndEvent.class;
    }

    @Override
    protected void executeParse(BpmnParse bpmnParse, EndEvent endEvent) {

        EventDefinition eventDefinition = null;
        if (endEvent.getEventDefinitions().size() > 0) {
                throw new RunFlowException("不支持");
        } else {
            endEvent.setBehavior(new NoneEndEventActivityBehavior());
        }
    }

}