package com.runflow.engine.impl.agenda;

import com.runflow.engine.RunFlowException;
import com.runflow.engine.CommandExecutorImpl;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.cmd.ExecuteAsyncJobCmd;
import com.runflow.engine.context.Context;
import com.runflow.engine.delegate.ActivityBehavior;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.utils.CollectionUtil;
import org.activiti.bpmn.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class ContinueProcessOperation extends AbstractOperation {


    private static Logger logger = LoggerFactory.getLogger(ContinueProcessOperation.class);

    protected boolean forceSynchronousOperation;
    protected boolean inCompensation;


    private AtomicInteger integer = new AtomicInteger(0);

    public ContinueProcessOperation(CommandContext commandContext, ExecutionEntityImpl execution,
                                    boolean forceSynchronousOperation, boolean inCompensation) {

        super(commandContext, execution);
        this.forceSynchronousOperation = forceSynchronousOperation;
        this.inCompensation = inCompensation;
    }

    public ContinueProcessOperation(CommandContext commandContext, ExecutionEntityImpl execution) {
        this(commandContext, execution, false, false);
    }

    @Override
    public void run() {
        FlowElement currentFlowElement = getCurrentFlowElement(execution);
        if (currentFlowElement instanceof FlowNode) {
            continueThroughFlowNode((FlowNode) currentFlowElement);
        } else if (currentFlowElement instanceof SequenceFlow) {
            continueThroughSequenceFlow((SequenceFlow) currentFlowElement);
        } else {
            throw new RunFlowException("Programmatic error: no current flow element found or invalid type: " + currentFlowElement + ". Halting.");
        }
    }


    protected void continueThroughFlowNode(FlowNode flowNode) {
        final CommandContext commandContext = Context.getCommandContext();
        ExecutorService executorService = Context.getProcessEngineConfiguration().getExecutorService();
        if (flowNode instanceof Activity && ((Activity) flowNode).hasMultiInstanceLoopCharacteristics()) {
            // the multi instance execution will look at async
            throw new RunFlowException("不支持多实例异步操作");
        } else if (forceSynchronousOperation || !flowNode.isAsynchronous()) {
            executeSynchronous(flowNode);
        } else {
            CommandExecutorImpl commandExecutor = Context.getProcessEngineConfiguration().getCommandExecutor();
            executorService.execute(() ->{
                commandExecutor.execute(new ExecuteAsyncJobCmd( execution,commandContext.getMainThread(),commandContext.getSerialNumber()));
                    }
            );
        }
    }

    protected void executeSynchronous(FlowNode flowNode) {

        // Execute actual behavior
        ActivityBehavior activityBehavior = (ActivityBehavior) flowNode.getBehavior();
        final FlowElement currentFlowElement = execution.getCurrentFlowElement();
        logger.debug("[{}]-[{}]  name：{}-({})  id:{}  ",Thread.currentThread().getName(),commandContext.getSerialNumber(), currentFlowElement.getName()==null?"":currentFlowElement.getName(), currentFlowElement.getClass().getSimpleName(),execution.getId());
        if (activityBehavior != null) {
            executeActivityBehavior(activityBehavior);
        } else {
            logger.debug("No activityBehavior on activity '{}' with execution {}", flowNode.getId(), execution.getId());
            Context.getAgenda().planTakeOutgoingSequenceFlowsOperation(execution, true);
        }
    }

    protected void executeActivityBehavior(ActivityBehavior activityBehavior) {
            activityBehavior.execute(execution);
    }

    protected void continueThroughSequenceFlow(SequenceFlow sequenceFlow) {
        // Firing event that transition is being taken
        FlowElement targetFlowElement = sequenceFlow.getTargetFlowElement();
        execution.setCurrentFlowElement(targetFlowElement);

        Context.getAgenda().planContinueProcessOperation(execution);
    }


}
