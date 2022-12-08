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
import java.util.stream.Collectors;

public class ParallelGatewayActivityBehavior extends GatewayActivityBehavior {


    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelGatewayActivityBehavior.class);

    public void execute(ExecutionEntity e1) {

        synchronized (ParallelGatewayActivityBehavior.class) {


            ExecutionEntityImpl execution = (ExecutionEntityImpl) e1;
            execution.inactivate();

            // Join
            FlowElement flowElement = execution.getCurrentFlowElement();

            ParallelGateway parallelGateway = null;
            if (flowElement instanceof ParallelGateway) {
                parallelGateway = (ParallelGateway) flowElement;
            } else {
                throw new ActivitiException("Programmatic error: parallel gateway behaviour can only be applied" + " to a ParallelGateway instance, but got an instance of " + flowElement);
            }

            {
                int nbrOfExecutionsToJoin = parallelGateway.getIncomingFlows().size();
                List<ExecutionEntityImpl> joinedExecutions = this.findInactiveExecutionsByActivityIdAndProcessInstanceId(execution);
                int nbrOfExecutionsCurrentlyJoined = joinedExecutions.size();
                if (nbrOfExecutionsCurrentlyJoined == nbrOfExecutionsToJoin) {
                    LOGGER.info(flowElement.getName());
                    if (parallelGateway.getIncomingFlows().size() > 1) {
                        // All (now inactive) children are deleted.
                        Iterator<ExecutionEntityImpl> iterator = joinedExecutions.iterator();
//                    LOGGER.info("线程名称：" + Thread.currentThread().getName() + "：“id:" + execution.getId());
                        while (iterator.hasNext()) {
                            ExecutionEntityImpl next = iterator.next();
                            if (!next.getId().equals(execution.getId())) {
//                            LOGGER.info(execution.getCurrentFlowElement().getName() + "线程名称：" + Thread.currentThread().getName() + "：删除元素:{}", next.getId());
                                iterator.remove();

                            }

                        }


                    }
                    Context.getAgenda().planTakeOutgoingSequenceFlowsOperation((ExecutionEntityImpl) execution, false); // false -> ignoring conditions on parallel gw
                }
            }
        }
    }


    public List<ExecutionEntityImpl> findInactiveExecutionsByActivityIdAndProcessInstanceId(ExecutionEntityImpl executionEntity) {
        CurrentHashMapCache defaultSession = Context.getCommandContext().getDefaultSession();
        List<ExecutionEntityImpl> inCache = defaultSession.findInCache(executionEntity.getSerialNumber());
        return inCache.stream().filter(c -> !c.isActive() && c.getCurrentActivityId().equals(executionEntity.getCurrentActivityId())).collect(Collectors.toList());

    }


}
