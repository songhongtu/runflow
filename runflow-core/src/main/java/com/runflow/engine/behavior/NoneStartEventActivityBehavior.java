package com.runflow.engine.behavior;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.delegate.FlowNodeActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoneStartEventActivityBehavior extends FlowNodeActivityBehavior {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(NoneStartEventActivityBehavior.class);

    // Nothing to see here.
    // The default behaviour of the BpmnActivity is exactly what
    // a none start event should be doing.


    @Override
    public void execute(ExecutionEntityImpl execution) {
        super.execute(execution);
    }
}