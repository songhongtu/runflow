package com.runflow.engine.parse.handler;

import com.runflow.engine.behavior.ParallelGatewayActivityBehavior;
import com.runflow.engine.parse.AbstractActivityBpmnParseHandler;
import com.runflow.engine.parse.BpmnParse;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.ParallelGateway;

public class ParallelGatewayParseHandler extends AbstractActivityBpmnParseHandler<ParallelGateway> {

    public Class<? extends BaseElement> getHandledType() {
        return ParallelGateway.class;
    }


    protected void executeParse(BpmnParse bpmnParse, ParallelGateway gateway) {
        gateway.setBehavior(new  ParallelGatewayActivityBehavior());
    }

}