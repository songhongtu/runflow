package com.runflow.engine.cmd;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandContext;

public class ExecuteAsyncJobCmd implements Command<Object> {


    protected ExecutionEntityImpl execution;

  public  ExecuteAsyncJobCmd(ExecutionEntityImpl executionEntity){
        this.execution = executionEntity;
    }

    @Override
    public Object execute(CommandContext commandContext) {
        Context.getAgenda().planContinueProcessSynchronousOperation(execution);
        return null;
    }
}
