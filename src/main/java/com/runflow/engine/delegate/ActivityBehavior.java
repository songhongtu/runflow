package com.runflow.engine.delegate;

import com.runflow.engine.ExecutionEntity;

import java.io.Serializable;

public interface ActivityBehavior  extends Serializable {
    void execute(ExecutionEntity execution);
}
