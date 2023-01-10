package com.runflow.engine.behavior;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.context.Context;
import com.runflow.engine.delegate.FlowNodeActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoneEndEventActivityBehavior extends FlowNodeActivityBehavior {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoneEndEventActivityBehavior.class);
    private static final long serialVersionUID = 1L;
    @Override
    public void execute(ExecutionEntityImpl execution) {
        LOGGER.debug("结束节点  名称：{}  id:{}  线程名称:{} ",execution.getCurrentFlowElement().getName(),execution.getId(),Thread.currentThread().getName());
        Context.getAgenda().planTakeOutgoingSequenceFlowsOperation( execution, true);
    }

}
