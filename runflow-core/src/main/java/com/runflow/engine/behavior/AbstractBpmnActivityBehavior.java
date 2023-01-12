package com.runflow.engine.behavior;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.delegate.FlowNodeActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractBpmnActivityBehavior extends FlowNodeActivityBehavior {


    @Override
    public void leave(ExecutionEntityImpl execution) {
        super.leave(execution);
    }


}
