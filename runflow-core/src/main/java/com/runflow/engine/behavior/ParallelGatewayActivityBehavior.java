package com.runflow.engine.behavior;

import com.runflow.engine.RunFlowException;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.cache.impl.CurrentHashMapCache;
import com.runflow.engine.context.Context;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ParallelGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ParallelGatewayActivityBehavior extends GatewayActivityBehavior {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelGatewayActivityBehavior.class);

    @Override
    public void execute(ExecutionEntityImpl e1) {
        ExecutionEntityImpl execution = e1;
        synchronized (execution.getSerialNumber()) {
            execution.inactivate();
            // Join
            FlowElement flowElement = execution.getCurrentFlowElement();
            ParallelGateway parallelGateway = (ParallelGateway) flowElement;
            int nbrOfExecutionsToJoin = parallelGateway.getIncomingFlows().size();
            CurrentHashMapCache<ExecutionEntityImpl> defaultSession = Context.getCommandContext().getAllRunTimeExecution();
            Set<ExecutionEntityImpl> entitySet = defaultSession.get(execution.getSerialNumber());
            List<ExecutionEntityImpl> joinedExecutions = entitySet.stream().filter(c ->  c!=null&&!c.isActive() && c.getCurrentActivityId().equals(execution.getCurrentActivityId())).collect(Collectors.toList());
            int nbrOfExecutionsCurrentlyJoined = joinedExecutions.size();
            if (nbrOfExecutionsCurrentlyJoined == nbrOfExecutionsToJoin) {
                LOGGER.debug("并行网关  名称：{}  id:{}  线程名称:{} ", execution.getCurrentFlowElement().getName(), execution.getId(), Thread.currentThread().getName());
                if (parallelGateway.getIncomingFlows().size() > 1) {
                    Iterator<ExecutionEntityImpl> iterator = joinedExecutions.iterator();
                    while (iterator.hasNext()) {
                        ExecutionEntityImpl next = iterator.next();
                        if (!next.getId().equals(execution.getId())) {
                            entitySet.remove(next);
                        }
                    }
                }
                Context.getAgenda().planTakeOutgoingSequenceFlowsOperation(execution, false); // false -> ignoring conditions on parallel gw
            }
        }
    }


}
