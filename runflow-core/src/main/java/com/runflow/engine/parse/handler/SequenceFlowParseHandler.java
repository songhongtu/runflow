package com.runflow.engine.parse.handler;

import com.runflow.engine.parse.AbstractBpmnParseHandler;
import com.runflow.engine.parse.BpmnParse;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.SequenceFlow;

public class SequenceFlowParseHandler extends AbstractBpmnParseHandler<SequenceFlow> {

    public static final String PROPERTYNAME_CONDITION = "condition";
    public static final String PROPERTYNAME_CONDITION_TEXT = "conditionText";

    public Class<? extends BaseElement> getHandledType() {
        return SequenceFlow.class;
    }

    protected void executeParse(BpmnParse bpmnParse, SequenceFlow sequenceFlow) {
        org.activiti.bpmn.model.Process process = bpmnParse.getCurrentProcess();
        sequenceFlow.setSourceFlowElement(process.getFlowElement(sequenceFlow.getSourceRef(), true));
        sequenceFlow.setTargetFlowElement(process.getFlowElement(sequenceFlow.getTargetRef(), true));
    }

}
