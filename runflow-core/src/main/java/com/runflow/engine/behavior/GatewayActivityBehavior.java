package com.runflow.engine.behavior;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.delegate.FlowNodeActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GatewayActivityBehavior extends FlowNodeActivityBehavior {

    private static final long serialVersionUID = 1L;


    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayActivityBehavior.class);


    @Override
    public void execute(ExecutionEntityImpl execution) {
        LOGGER.debug("排他网关  名称：{}  id:{}  线程名称:{} ",execution.getCurrentFlowElement().getName(),execution.getId(),Thread.currentThread().getName());
        super.execute(execution);
    }
}
