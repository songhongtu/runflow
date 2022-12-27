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
    public void execute(ExecutionEntityImpl execution) {
        ExecutionEntityImpl execution1 = (ExecutionEntityImpl)execution;
        LOGGER.debug("开始节点  名称：{}  id:{}  线程名称:{} ",execution1.getCurrentFlowElement().getName(),execution.getId(),Thread.currentThread().getName());
        super.execute(execution1);
    }
}