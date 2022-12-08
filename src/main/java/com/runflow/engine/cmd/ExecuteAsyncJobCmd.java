package com.runflow.engine.cmd;

import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;

public class ExecuteAsyncJobCmd implements Command<Object>, Serializable {


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
