package com.runflow.engine.behavior;

import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.context.Context;
import com.runflow.engine.delegate.FlowNodeActivityBehavior;

public abstract class GatewayActivityBehavior extends FlowNodeActivityBehavior {

    private static final long serialVersionUID = 1L;

    protected void lockFirstParentScope(ExecutionEntity execution) {


    }

}
