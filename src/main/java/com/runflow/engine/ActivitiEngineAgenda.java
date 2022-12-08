package com.runflow.engine;

public interface ActivitiEngineAgenda extends Agenda{
    void planContinueProcessOperation(ExecutionEntity execution);
    void planTakeOutgoingSequenceFlowsOperation(ExecutionEntity execution, boolean evaluateConditions);

    void planContinueProcessSynchronousOperation(ExecutionEntity execution);


    void planEndExecutionOperation(ExecutionEntity execution);

}
