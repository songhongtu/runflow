package com.runflow.engine.parse.handler;

import com.runflow.engine.ActivitiException;
import com.runflow.engine.behavior.NoneStartEventActivityBehavior;
import com.runflow.engine.parse.AbstractActivityBpmnParseHandler;
import com.runflow.engine.parse.BpmnParse;
import com.runflow.engine.utils.CollectionUtil;
import org.activiti.bpmn.model.*;

public class StartEventParseHandler extends AbstractActivityBpmnParseHandler<StartEvent> {

    @Override
    public Class<? extends BaseElement> getHandledType() {
        return StartEvent.class;
    }

    @Override
    protected void executeParse(BpmnParse bpmnParse, StartEvent element) {
        if (element.getSubProcess() != null && element.getSubProcess() instanceof EventSubProcess) {
                throw new ActivitiException("暂时不支持子流程");
        } else if (CollectionUtil.isEmpty(element.getEventDefinitions())) {
            element.setBehavior(new NoneStartEventActivityBehavior());
        }

        if (element.getSubProcess() == null && (CollectionUtil.isEmpty(element.getEventDefinitions()) ||
                bpmnParse.getCurrentProcess().getInitialFlowElement() == null)) {

            bpmnParse.getCurrentProcess().setInitialFlowElement(element);
        }
    }

}
