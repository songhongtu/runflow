package com.runflow.engine.delegate;

import com.runflow.engine.RunFlowEngineAgenda;
import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.RunFlowException;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.impl.agenda.AbstractOperation;
import com.runflow.engine.impl.agenda.ContinueProcessOperation;
import com.runflow.engine.impl.agenda.EndExecutionOperation;
import com.runflow.engine.impl.agenda.TakeOutgoingSequenceFlowsOperation;

import java.util.LinkedList;

public class DefaultRunFlowEngineAgenda implements RunFlowEngineAgenda {



    protected LinkedList<Runnable> operations = new LinkedList<>();
    protected CommandContext commandContext;

    public DefaultRunFlowEngineAgenda(CommandContext commandContext) {
        this.commandContext = commandContext;
    }

    @Override
    public boolean isEmpty() {
        return operations.isEmpty();
    }

    @Override
    public Runnable getNextOperation() {
        return operations.poll();
    }

    /**
     * Generic method to plan a {@link Runnable}.
     */
    @Override
    public void planOperation(Runnable operation) {
        boolean add = operations.add(operation);
        if (!add) {
            throw new RunFlowException("错误");
        }

        if (operation instanceof AbstractOperation) {
            ExecutionEntity execution = ((AbstractOperation) operation).getExecution();
            if (execution != null) {
                commandContext.addInvolvedExecution(execution);
            }
        }
    }

    @Override
    public void planContinueProcessOperation(ExecutionEntityImpl execution) {
        planOperation(new ContinueProcessOperation(commandContext, execution));
    }

    @Override
    public void planTakeOutgoingSequenceFlowsOperation(ExecutionEntityImpl execution, boolean evaluateConditions) {
        planOperation(new TakeOutgoingSequenceFlowsOperation(commandContext, execution, evaluateConditions));
    }

    @Override
    public void planContinueProcessSynchronousOperation(ExecutionEntityImpl execution) {
        planOperation(new ContinueProcessOperation(commandContext, execution, true, false));
    }

    @Override
    public void planEndExecutionOperation(ExecutionEntityImpl execution) {
        planOperation(new EndExecutionOperation(commandContext, execution));
    }


}
