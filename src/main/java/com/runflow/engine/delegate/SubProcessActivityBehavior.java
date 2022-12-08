package com.runflow.engine.delegate;

import com.runflow.engine.ExecutionEntityImpl;

public interface SubProcessActivityBehavior extends ActivityBehavior{

    void completing(ExecutionEntityImpl execution, ExecutionEntityImpl subProcessInstance);

    /**
     * called after the process instance is destroyed for this activity to perform its outgoing control flow logic.
     */
    void completed(ExecutionEntityImpl execution) throws Exception;

}
