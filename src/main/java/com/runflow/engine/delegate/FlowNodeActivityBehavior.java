package com.runflow.engine.delegate;

import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.ExecutionEntityImpl;

public abstract class FlowNodeActivityBehavior implements TriggerableActivityBehavior {

    private static final long serialVersionUID = 1L;

    protected BpmnActivityBehavior bpmnActivityBehavior = new BpmnActivityBehavior();

    /**
     * Default behaviour: just leave the activity with no extra functionality.
     */
    public void execute(ExecutionEntityImpl execution) {
        leave(execution);
    }

    /**
     * Default way of leaving a BPMN 2.0 activity: evaluate the conditions on the outgoing sequence flow and take those that evaluate to true.
     */
    public void leave(ExecutionEntityImpl execution) {
        bpmnActivityBehavior.performDefaultOutgoingBehavior( execution);
    }




}