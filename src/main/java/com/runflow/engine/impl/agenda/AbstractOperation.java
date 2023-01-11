package com.runflow.engine.impl.agenda;

import com.runflow.engine.RunFlowException;
import com.runflow.engine.Agenda;
import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.delegate.DefaultRunFlowEngineAgenda;
import com.runflow.engine.impl.CommandContext;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.HasExecutionListeners;

public abstract class AbstractOperation implements Runnable {

    protected CommandContext commandContext;
    protected Agenda agenda;
    protected ExecutionEntityImpl execution;

    protected AbstractOperation(CommandContext commandContext, ExecutionEntityImpl execution) {
        this.commandContext = commandContext;
        this.execution = execution;
        this.agenda = commandContext.getAgenda();
    }

    /**
     * Helper method to match the activityId of an execution with a FlowElement of the process definition referenced by the execution.
     */
    protected FlowElement getCurrentFlowElement(final ExecutionEntity execution) {
        if (execution.getCurrentFlowElement() != null) {
            return execution.getCurrentFlowElement();
        } else if (execution.getCurrentActivityId() != null) {
        }
        return null;
    }






    public CommandContext getCommandContext() {
        return commandContext;
    }

    public void setCommandContext(CommandContext commandContext) {
        this.commandContext = commandContext;
    }

    public Agenda getAgenda() {
        return agenda;
    }


    public ExecutionEntityImpl getExecution() {
        return execution;
    }

    public void setExecution(ExecutionEntityImpl execution) {
        this.execution = execution;
    }

}
