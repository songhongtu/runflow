package com.runflow.engine.behavior;

import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.context.Context;
import com.runflow.engine.delegate.FlowNodeActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GatewayActivityBehavior extends FlowNodeActivityBehavior {

    private static final long serialVersionUID = 1L;


    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelGatewayActivityBehavior.class);


    @Override
    public void execute(ExecutionEntityImpl execution) {
        LOGGER.debug("排它网关 id:" + execution.getId() + ":" + "线程名称：" + Thread.currentThread().getName() + "：" + execution.getCurrentFlowElement().getName());
        super.execute(execution);
    }
}
