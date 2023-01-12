package com.runflow.engine;

public interface RunFlowEngineAgenda extends Agenda{
    void planContinueProcessOperation(ExecutionEntityImpl execution);
    void planTakeOutgoingSequenceFlowsOperation(ExecutionEntityImpl execution, boolean evaluateConditions);

    void planContinueProcessSynchronousOperation(ExecutionEntityImpl execution);


    void planEndExecutionOperation(ExecutionEntityImpl execution);

}
