package com.runflow.engine.delegate;

import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

public class BpmnActivityBehavior implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger log = LoggerFactory.getLogger(BpmnActivityBehavior.class);

    /**
     * Performs the default outgoing BPMN 2.0 behavior, which is having parallel paths of executions for the outgoing sequence flow.
     *
     * More precisely: every sequence flow that has a condition which evaluates to true (or which doesn't have a condition), is selected for continuation of the process instance. If multiple sequencer
     * flow are selected, multiple, parallel paths of executions are created.
     */
    public void performDefaultOutgoingBehavior(ExecutionEntityImpl activityExecution) {
        performOutgoingBehavior(activityExecution, true, false);
    }

    /**
     * dispatch job canceled event for job associated with given execution entity
     *
     * @param activityExecution
     */

    /**
     * Performs the default outgoing BPMN 2.0 behavior (@see {@link #performDefaultOutgoingBehavior(ExecutionEntity)}), but without checking the conditions on the outgoing sequence flow.
     *
     * This means that every outgoing sequence flow is selected for continuing the process instance, regardless of having a condition or not. In case of multiple outgoing sequence flow, multiple
     * parallel paths of executions will be created.
     */
    public void performIgnoreConditionsOutgoingBehavior(ExecutionEntityImpl activityExecution) {
        performOutgoingBehavior(activityExecution, false, false);
    }

    /**
     * Actual implementation of leaving an activity.
     *
     * @param execution
     *          The current execution context
     * @param checkConditions
     *          Whether or not to check conditions before determining whether or not to take a transition.
     * @param throwExceptionIfExecutionStuck
     *          If true, an {@link ActivitiException} will be thrown in case no transition could be found to leave the activity.
     */
    protected void performOutgoingBehavior(ExecutionEntityImpl execution, boolean checkConditions, boolean throwExceptionIfExecutionStuck) {
     Context.getAgenda().planTakeOutgoingSequenceFlowsOperation(execution, true);
    }

}