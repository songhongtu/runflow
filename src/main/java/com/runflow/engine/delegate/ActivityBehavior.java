package com.runflow.engine.delegate;

import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.ExecutionEntityImpl;

import java.io.Serializable;

public interface ActivityBehavior  extends Serializable {
    void execute(ExecutionEntityImpl execution);
}
