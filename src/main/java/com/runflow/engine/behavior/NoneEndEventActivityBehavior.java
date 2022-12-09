package com.runflow.engine.behavior;

import com.runflow.engine.ActivitiException;
import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.context.Context;
import com.runflow.engine.delegate.FlowNodeActivityBehavior;
import com.runflow.engine.parse.AbstractBpmnParseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoneEndEventActivityBehavior extends FlowNodeActivityBehavior {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelGatewayActivityBehavior.class);
    private static final long serialVersionUID = 1L;

    public void execute(ExecutionEntityImpl execution) {
        Context.getAgenda().planTakeOutgoingSequenceFlowsOperation( execution, true);
    }

}
