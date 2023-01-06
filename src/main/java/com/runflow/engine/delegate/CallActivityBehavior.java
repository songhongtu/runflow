package com.runflow.engine.delegate;

import com.runflow.engine.RunFlowException;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.behavior.AbstractBpmnActivityBehavior;
import com.runflow.engine.bpmn.entity.ProcessDefinition;
import com.runflow.engine.bpmn.entity.impl.ProcessDefinitionCacheEntry;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;

import javax.el.Expression;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CallActivityBehavior extends AbstractBpmnActivityBehavior implements SubProcessActivityBehavior {

    protected String processDefinitonKey;
    protected Expression processDefinitionExpression;
    protected List<MapExceptionEntry> mapExceptions;

    public CallActivityBehavior(String processDefinitionKey, List<MapExceptionEntry> mapExceptions) {
        this.processDefinitonKey = processDefinitionKey;
        this.mapExceptions = mapExceptions;
    }

    public CallActivityBehavior(Expression processDefinitionExpression, List<MapExceptionEntry> mapExceptions) {
        this.processDefinitionExpression = processDefinitionExpression;
        this.mapExceptions = mapExceptions;
    }

    @Override
    public void execute(ExecutionEntityImpl execution) {

        String finalProcessDefinitonKey = processDefinitonKey;

        ProcessDefinitionCacheEntry processDefinitionCacheEntry = Context.getProcessEngineConfiguration().getProcessDefinitionCache().get(finalProcessDefinitonKey);
        if (processDefinitionCacheEntry == null) {
            throw new RunFlowException("找不到子流程 key:"+processDefinitonKey);
        }

        String uuid = UUID.randomUUID().toString();

        ProcessDefinition processDefinition = processDefinitionCacheEntry.getProcessDefinition();
        Process subProcess = processDefinitionCacheEntry.getProcess();
        FlowElement initialFlowElement = subProcess.getInitialFlowElement();
        ExecutionEntityImpl executionEntity = (ExecutionEntityImpl) execution;
        CallActivity callActivity = (CallActivity) execution.getCurrentFlowElement();
        ExecutionEntityImpl subProcessInstance = executionEntity.createSubprocessInstance(processDefinition, executionEntity, null);
        subProcessInstance.setMainThread(Thread.currentThread());
        subProcessInstance.setSerialNumber(uuid);

        ExecutionEntityImpl subProcessInitialExecution = executionEntity.createChildExecution(subProcessInstance);
        subProcessInitialExecution.setCurrentFlowElement(initialFlowElement);

        List<IOParameter> inParameters = callActivity.getInParameters();
        Map<String, Object> variableInstances = execution.getVariableInstances();

        subProcessInstance.setVariableInstances(variableInstances);
        ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();


        Context.getAgenda().planContinueProcessOperation(subProcessInitialExecution);

    }

    @Override
    public void completing(ExecutionEntityImpl execution, ExecutionEntityImpl subProcessInstance) {
        ExecutionEntityImpl executionEntity = (ExecutionEntityImpl) execution;

    }

    @Override
    public void completed(ExecutionEntityImpl execution) throws Exception {
        ExecutionEntityImpl executionEntity = (ExecutionEntityImpl) execution;

        leave(executionEntity);
    }
}
