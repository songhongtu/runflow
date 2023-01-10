package com.runflow.engine.behavior;

import com.runflow.engine.RunFlowException;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.parse.handler.EndEventParseHandler;
import com.runflow.engine.utils.ConditionUtil;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.SequenceFlow;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class ExclusiveGatewayActivityBehavior extends GatewayActivityBehavior {



    @Override
    public void leave(ExecutionEntityImpl execution) {

        ExclusiveGateway exclusiveGateway = (ExclusiveGateway) execution.getCurrentFlowElement();


        SequenceFlow outgoingSequenceFlow = null;
        SequenceFlow defaultSequenceFlow = null;
        String defaultSequenceFlowId = exclusiveGateway.getDefaultFlow();
        boolean b = false;
        // Determine sequence flow to take
        Iterator<SequenceFlow> sequenceFlowIterator = exclusiveGateway.getOutgoingFlows().iterator();

        SequenceFlow emptyOutgoingSequenceFlow = null;

        while (outgoingSequenceFlow == null && sequenceFlowIterator.hasNext()) {
            SequenceFlow sequenceFlow = sequenceFlowIterator.next();
            String conditionExpression = sequenceFlow.getConditionExpression();

            if (!StringUtils.isEmpty(conditionExpression)) {
                b = ConditionUtil.hasTrueCondition(conditionExpression, (ExecutionEntityImpl) execution);
                if (b) {
                    outgoingSequenceFlow = sequenceFlow;
                }
            } else {
                emptyOutgoingSequenceFlow = sequenceFlow;

            }


            // Already store it, if we would need it later. Saves one for loop.
            if (defaultSequenceFlowId != null && defaultSequenceFlowId.equals(sequenceFlow.getId())) {
                defaultSequenceFlow = sequenceFlow;
            }

        }

        // Leave the gateway
        if (outgoingSequenceFlow != null) {
            execution.setCurrentFlowElement(outgoingSequenceFlow);
        } else {
            if (defaultSequenceFlow != null) {
                execution.setCurrentFlowElement(defaultSequenceFlow);
            } else {
                if (emptyOutgoingSequenceFlow != null) {
                    execution.setCurrentFlowElement(emptyOutgoingSequenceFlow);
                } else {
                    // No sequence flow could be found, not even a default one
                    throw new RunFlowException("No outgoing sequence flow of the exclusive gateway '" + exclusiveGateway.getId() + "' could be selected for continuing the process");
                }
            }
        }

        super.leave(execution);
    }


}
