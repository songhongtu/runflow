package com.runflow.engine.behavior;

import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.delegate.FlowNodeActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoneStartEventActivityBehavior extends FlowNodeActivityBehavior {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskActivityBehavior.class);

    // Nothing to see here.
    // The default behaviour of the BpmnActivity is exactly what
    // a none start event should be doing.


    @Override
    public void execute(ExecutionEntity execution) {
        ExecutionEntityImpl execution1 = (ExecutionEntityImpl)execution;
        LOGGER.info(execution1.getCurrentFlowElement().getName());
        super.execute(execution1);
    }
}