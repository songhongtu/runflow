package com.runflow.engine.impl.agenda;

import com.runflow.engine.RunFlowException;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.utils.ConditionUtil;
import org.activiti.bpmn.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TakeOutgoingSequenceFlowsOperation extends AbstractOperation {

    private static final Logger logger = LoggerFactory.getLogger(TakeOutgoingSequenceFlowsOperation.class);

    protected boolean evaluateConditions;

    public TakeOutgoingSequenceFlowsOperation(CommandContext commandContext, ExecutionEntityImpl executionEntity, boolean evaluateConditions) {
        super(commandContext, executionEntity);
        this.evaluateConditions = evaluateConditions;
    }

    @Override
    public void run() {
        FlowElement currentFlowElement = getCurrentFlowElement(execution);

        // Compensation check
        if ((currentFlowElement instanceof Activity)
                && (((Activity) currentFlowElement)).isForCompensation()) {

            /*
             * If the current flow element is part of a compensation, we don't always
             * want to follow the regular rules of leaving an activity.
             * More specifically, if there are no outgoing sequenceflow, we simply must stop
             * the execution there and don't go up in the scopes as we usually do
             * to find the outgoing sequenceflow
             */
            throw new RunFlowException("不支持");
        }

        if (currentFlowElement instanceof FlowNode) {
            handleFlowNode((FlowNode) currentFlowElement);
        } else if (currentFlowElement instanceof SequenceFlow) {
            handleSequenceFlow();
        }
    }

    protected void handleFlowNode(FlowNode flowNode) {
        if (flowNode.getParentContainer() != null
                && flowNode.getParentContainer() instanceof AdhocSubProcess) {
            throw new RunFlowException("不支持");
        } else {
            leaveFlowNode(flowNode);
        }
    }


    protected void leaveFlowNode(FlowNode flowNode) {
        // Get default sequence flow (if set)
        String defaultSequenceFlowId = null;
        if (flowNode instanceof Activity) {
            defaultSequenceFlowId = ((Activity) flowNode).getDefaultFlow();
        } else if (flowNode instanceof Gateway) {
            defaultSequenceFlowId = ((Gateway) flowNode).getDefaultFlow();
        }

        // Determine which sequence flows can be used for leaving
        List<SequenceFlow> outgoingSequenceFlows = new ArrayList<SequenceFlow>();
        for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
            String skipExpressionString = sequenceFlow.getSkipExpression();
            String conditionExpression = sequenceFlow.getConditionExpression();
            if (!StringUtils.isEmpty(conditionExpression)) {
                if (evaluateConditions && ConditionUtil.hasTrueCondition(conditionExpression, (ExecutionEntityImpl) execution)) {
                    outgoingSequenceFlows.add(sequenceFlow);
                }
            } else if (StringUtils.isEmpty(skipExpressionString)) {
                outgoingSequenceFlows.add(sequenceFlow);
            } else if (flowNode.getOutgoingFlows().size() == 1) {
                // The 'skip' for a sequence flow means that we skip the condition, not the sequence flow.
                outgoingSequenceFlows.add(sequenceFlow);
            }
        }

        // Check if there is a default sequence flow
        if (outgoingSequenceFlows.size() == 0 && evaluateConditions) { // The elements that set this to false also have no support for default sequence flow
            if (defaultSequenceFlowId != null) {
                for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
                    if (defaultSequenceFlowId.equals(sequenceFlow.getId())) {
                        outgoingSequenceFlows.add(sequenceFlow);
                        break;
                    }
                }
            }
        }

        // No outgoing found. Ending the execution
        if (outgoingSequenceFlows.size() == 0) {
            if (flowNode.getOutgoingFlows() == null || flowNode.getOutgoingFlows().size() == 0) {
                Context.getAgenda().planEndExecutionOperation(execution);
            } else {
                throw new RunFlowException("No outgoing sequence flow of element '" + flowNode.getId() + "' could be selected for continuing the process");
            }

        } else {
            List<ExecutionEntityImpl> outgoingExecutions = new ArrayList<ExecutionEntityImpl>(flowNode.getOutgoingFlows().size());
            SequenceFlow sequenceFlow = outgoingSequenceFlows.get(0);
            // Reuse existing one
            execution.setCurrentFlowElement(sequenceFlow);
            execution.setActive(true);
            outgoingExecutions.add(execution);

            // Executions for all the other one
            if (outgoingSequenceFlows.size() > 1) {
                for (int i = 1; i < outgoingSequenceFlows.size(); i++) {
                    ExecutionEntityImpl parent = (ExecutionEntityImpl) (execution.getParentId() != null ? execution.getParent() : execution);
                    ExecutionEntityImpl outgoingExecutionEntity = parent.createChildExecution(parent);
                    SequenceFlow outgoingSequenceFlow = outgoingSequenceFlows.get(i);
                    outgoingExecutionEntity.setCurrentFlowElement(outgoingSequenceFlow);
                    outgoingExecutions.add(outgoingExecutionEntity);
                }
            }

            // Leave (only done when all executions have been made, since some queries depend on this)
            for (ExecutionEntityImpl outgoingExecution : outgoingExecutions) {
                Context.getAgenda().planContinueProcessOperation(outgoingExecution);
            }
        }
    }


    protected void handleSequenceFlow() {
        Context.getAgenda().planContinueProcessOperation(execution);
    }


}
