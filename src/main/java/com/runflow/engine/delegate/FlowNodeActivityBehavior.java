package com.runflow.engine.delegate;

import com.runflow.engine.ActivitiException;
import com.runflow.engine.ExecutionEntity;
import org.activiti.bpmn.model.FlowNode;

public abstract class FlowNodeActivityBehavior implements TriggerableActivityBehavior {

    private static final long serialVersionUID = 1L;

    protected BpmnActivityBehavior bpmnActivityBehavior = new BpmnActivityBehavior();

    /**
     * Default behaviour: just leave the activity with no extra functionality.
     */
    public void execute(ExecutionEntity execution) {
        leave(execution);
    }

    /**
     * Default way of leaving a BPMN 2.0 activity: evaluate the conditions on the outgoing sequence flow and take those that evaluate to true.
     */
    public void leave(ExecutionEntity execution) {
        bpmnActivityBehavior.performDefaultOutgoingBehavior((ExecutionEntity) execution);
    }

    public void leaveIgnoreConditions(DelegateExecution execution) {
        bpmnActivityBehavior.performIgnoreConditionsOutgoingBehavior((ExecutionEntity) execution);
    }

    public void trigger(DelegateExecution execution, String signalName, Object signalData) {
        // concrete activity behaviours that do accept signals should override this method;
        throw new ActivitiException("this activity isn't waiting for a trigger");
    }

    protected String parseActivityType(FlowNode flowNode) {
        String elementType = flowNode.getClass().getSimpleName();
        elementType = elementType.substring(0, 1).toLowerCase() + elementType.substring(1);
        return elementType;
    }

}