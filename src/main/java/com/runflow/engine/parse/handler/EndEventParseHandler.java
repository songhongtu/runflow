package com.runflow.engine.parse.handler;

import com.runflow.engine.RunFlowException;
import com.runflow.engine.behavior.NoneEndEventActivityBehavior;
import com.runflow.engine.parse.AbstractActivityBpmnParseHandler;
import com.runflow.engine.parse.BpmnParse;
import org.activiti.bpmn.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndEventParseHandler  extends AbstractActivityBpmnParseHandler<EndEvent> {

    private static final Logger logger = LoggerFactory.getLogger(EndEventParseHandler.class);

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