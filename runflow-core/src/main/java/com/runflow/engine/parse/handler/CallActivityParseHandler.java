package com.runflow.engine.parse.handler;

import com.runflow.engine.delegate.CallActivityBehavior;
import com.runflow.engine.parse.AbstractActivityBpmnParseHandler;
import com.runflow.engine.parse.BpmnParse;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.CallActivity;

public class CallActivityParseHandler extends AbstractActivityBpmnParseHandler<CallActivity> {

    public Class<? extends BaseElement> getHandledType() {
        return CallActivity.class;
    }

    protected void executeParse(BpmnParse bpmnParse, CallActivity callActivity) {
        callActivity.setBehavior(new CallActivityBehavior(callActivity.getCalledElement(), callActivity.getMapExceptions()));
    }

}