package com.runflow.engine.impl.agenda;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.cache.impl.CurrentHashMapCache;
import com.runflow.engine.delegate.SubProcessActivityBehavior;
import com.runflow.engine.impl.CommandContext;
import org.activiti.bpmn.model.FlowNode;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.locks.LockSupport;

public class EndExecutionOperation extends AbstractOperation {


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
        ExecutionEntityImpl execution =  this.execution;

        CurrentHashMapCache<ExecutionEntityImpl> runTimeExecution = commandContext.getProcessEngineConfiguration().getRunTimeExecution();
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
