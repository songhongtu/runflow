package com.runflow.engine.impl.agenda;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.cache.impl.CurrentHashMapCache;
import com.runflow.engine.context.Context;
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

        ExecutionEntityImpl parent = execution.getParent();
        ExecutionEntityImpl superExecution = parent.getSuperExecution();

        CurrentHashMapCache<ExecutionEntityImpl> runTimeExecution = this.commandContext.getProcessEngineConfiguration().getRunTimeExecution();
        String serialNumber = execution.getSerialNumber();
        if (!StringUtils.isEmpty(serialNumber)) {
            runTimeExecution.remove(serialNumber);
        }
        //子流程不用释放中断线程
        if (this.commandContext.getMainThread() != null&&superExecution==null) {
          LockSupport.unpark(this.commandContext.getMainThread());
        }



        if (superExecution != null) {
            //执行父流程
            commandContext.setSerialNumber(superExecution.getSerialNumber());
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
