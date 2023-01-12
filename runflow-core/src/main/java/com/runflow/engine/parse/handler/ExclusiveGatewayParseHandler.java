package com.runflow.engine.parse.handler;

import com.runflow.engine.behavior.ExclusiveGatewayActivityBehavior;
import com.runflow.engine.parse.AbstractActivityBpmnParseHandler;
import com.runflow.engine.parse.BpmnParse;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.ExclusiveGateway;

public class ExclusiveGatewayParseHandler extends AbstractActivityBpmnParseHandler<ExclusiveGateway> {

    public Class<? extends BaseElement> getHandledType() {
        return ExclusiveGateway.class;
    }

    protected void executeParse(BpmnParse bpmnParse, ExclusiveGateway gateway) {
     gateway.setBehavior(new ExclusiveGatewayActivityBehavior());
    }
}
