package com.runflow.engine.cmd;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandContext;

public class ExecuteAsyncJobCmd implements Command<Object> {


    protected ExecutionEntityImpl execution;

    protected Thread mainThread;

    protected String serialNumber;

  public  ExecuteAsyncJobCmd(ExecutionEntityImpl executionEntity,Thread mainThread,String serialNumber){
        this.execution = executionEntity;
        this.mainThread = mainThread;
        this.serialNumber = serialNumber;
    }

    @Override
    public Object execute(CommandContext commandContext) {
        commandContext.setMainThread(mainThread);
        commandContext.setSerialNumber(serialNumber);
        Context.getAgenda().planContinueProcessSynchronousOperation(execution);
        return null;
    }
}
