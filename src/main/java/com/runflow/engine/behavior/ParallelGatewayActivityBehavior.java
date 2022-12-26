package com.runflow.engine.behavior;

import com.runflow.engine.ActivitiException;
import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.cache.impl.CurrentHashMapCache;
import com.runflow.engine.context.Context;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ParallelGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ParallelGatewayActivityBehavior extends GatewayActivityBehavior {


    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelGatewayActivityBehavior.class);


    @Override
    public synchronized void execute(ExecutionEntityImpl e1) {


        ExecutionEntityImpl execution = e1;
        execution.inactivate();

        // Join
        FlowElement flowElement = execution.getCurrentFlowElement();

        ParallelGateway parallelGateway = null;
        if (flowElement instanceof ParallelGateway) {
            parallelGateway = (ParallelGateway) flowElement;
        } else {
            throw new ActivitiException("Programmatic error: parallel gateway behaviour can only be applied" + " to a ParallelGateway instance, but got an instance of " + flowElement);
        }

        String parallelGatewayId = parallelGateway.getId();


        {
            int nbrOfExecutionsToJoin = parallelGateway.getIncomingFlows().size();
            List<ExecutionEntityImpl> joinedExecutions = this.findInactiveExecutionsByActivityIdAndProcessInstanceId(execution);
            int nbrOfExecutionsCurrentlyJoined = joinedExecutions.size();
            if (nbrOfExecutionsCurrentlyJoined == nbrOfExecutionsToJoin) {
                LOGGER.debug("并行网关 id:" + execution.getId() + ":" + "线程名称：" + Thread.currentThread().getName() + "：" + parallelGateway.getName());
                if (parallelGateway.getIncomingFlows().size() > 1) {
                    // All (now inactive) children are deleted.
                    Iterator<ExecutionEntityImpl> iterator = joinedExecutions.iterator();
                    while (iterator.hasNext()) {
                        ExecutionEntityImpl next = iterator.next();
                        if (!next.getId().equals(execution.getId())) {
                            iterator.remove();
                        }
                    }
                }
                Context.getAgenda().planTakeOutgoingSequenceFlowsOperation((ExecutionEntityImpl) execution, false); // false -> ignoring conditions on parallel gw
            }
        }
    }


    public List<ExecutionEntityImpl> findInactiveExecutionsByActivityIdAndProcessInstanceId(ExecutionEntityImpl executionEntity) {
        CurrentHashMapCache defaultSession = Context.getCommandContext().getDefaultSession();
        List<ExecutionEntityImpl> inCache = defaultSession.findInCache(executionEntity.getSerialNumber());
        return inCache.stream().filter(c -> !c.isActive() && c.getCurrentActivityId().equals(executionEntity.getCurrentActivityId())).collect(Collectors.toList());

    }


}
