package com.runflow.engine.delegate;

import com.runflow.engine.ExecutionEntityImpl;


public interface ActivityBehavior  {
    void execute(ExecutionEntityImpl execution);
}
