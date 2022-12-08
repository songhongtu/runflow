package com.runflow.engine.impl.agenda;

import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.behavior.ParallelGatewayActivityBehavior;
import com.runflow.engine.cache.impl.CurrentHashMapCache;
import com.runflow.engine.delegate.ActivityBehavior;
import com.runflow.engine.delegate.SubProcessActivityBehavior;
import com.runflow.engine.impl.CommandContext;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SubProcess;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.LockSupport;

public class EndExecutionOperation extends AbstractOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndExecutionOperation.class);

    public EndExecutionOperation(CommandContext commandContext, ExecutionEntityImpl execution) {
        super(commandContext, execution);
    }

    @Override
    public void run() {
        if (execution.getParent() == null) {
            throw new RuntimeException("不支持");
        } else {
            handleRegularExecution();
        }
    }


    protected void handleRegularExecution() {
        ExecutionEntityImpl execution = (ExecutionEntityImpl) this.execution;

        CurrentHashMapCache runTimeExecution = commandContext.getProcessEngineConfiguration().getRunTimeExecution();
        String serialNumber = execution.getSerialNumber();
        if (!StringUtils.isEmpty(serialNumber)) {
            runTimeExecution.remove(serialNumber);
        }
        commandContext.setSerialNumber(null);
        if (execution.getMainThread() != null) {
            LockSupport.unpark(execution.getMainThread());
        }


        ExecutionEntityImpl parent = execution.getParent();
        ExecutionEntityImpl superExecution = parent.getSuperExecution();
        if (superExecution != null) {
            SubProcessActivityBehavior subProcessActivityBehavior = null;
            FlowNode superExecutionElement = (FlowNode) superExecution.getCurrentFlowElement();
            subProcessActivityBehavior = (SubProcessActivityBehavior) superExecutionElement.getBehavior();
            subProcessActivityBehavior.completing(superExecution, parent);
            superExecution.setSubProcessInstance(null);
            try {
                subProcessActivityBehavior.completed(superExecution);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }
}
