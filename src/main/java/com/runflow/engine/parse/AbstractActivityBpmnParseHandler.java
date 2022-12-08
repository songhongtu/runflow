package com.runflow.engine.parse;

import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractActivityBpmnParseHandler<T extends FlowNode> extends AbstractFlowNodeBpmnParseHandler<T> {

    @Override
    public void parse(BpmnParse bpmnParse, BaseElement element) {
        super.parse(bpmnParse, element);

        if (element instanceof Activity && ((Activity) element).getLoopCharacteristics() != null) {
            createMultiInstanceLoopCharacteristics(bpmnParse, (Activity) element);
        }
    }

    protected void createMultiInstanceLoopCharacteristics(BpmnParse bpmnParse, Activity modelActivity) {

        MultiInstanceLoopCharacteristics loopCharacteristics = modelActivity.getLoopCharacteristics();


    }
}
