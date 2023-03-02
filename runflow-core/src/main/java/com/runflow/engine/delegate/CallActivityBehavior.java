package com.runflow.engine.delegate;

import com.runflow.engine.RunFlowException;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.behavior.AbstractBpmnActivityBehavior;
import com.runflow.engine.bpmn.entity.ProcessDefinition;
import com.runflow.engine.bpmn.entity.impl.ProcessDefinitionCacheEntry;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.utils.BpmnUtils;
import com.runflow.engine.utils.ConditionUtil;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.apache.commons.lang3.StringUtils;

import javax.el.Expression;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
        if (StringUtils.isEmpty(finalProcessDefinitonKey)) {
            leave(execution);
            return;
        }
        ProcessDefinitionCacheEntry processDefinitionCacheEntry = Context.getProcessEngineConfiguration().getProcessDefinitionCache().get(finalProcessDefinitonKey);
        if (processDefinitionCacheEntry == null) {
            throw new RunFlowException("找不到子流程 key:" + processDefinitonKey);
        }


        final Map<String, String> extensionElementPropertity = BpmnUtils.getExtensionElementPropertity(execution.getCurrentFlowElement().getExtensionElements());


        String uuid = UUID.randomUUID().toString();
        final CommandContext commandContext = Context.getCommandContext();
        commandContext.setSerialNumber(uuid);
        ProcessDefinition processDefinition = processDefinitionCacheEntry.getProcessDefinition();
        Process subProcess = processDefinitionCacheEntry.getProcess();
        FlowElement initialFlowElement = subProcess.getInitialFlowElement();

        ExecutionEntityImpl subProcessInstance = execution.createSubprocessInstance(processDefinition, execution, uuid);
        subProcessInstance.setSerialNumber(uuid);
        subProcessInstance.executions = new ArrayList<>();
        subProcessInstance.variableInstances = new ConcurrentHashMap<>();
        if(extensionElementPropertity!=null){
            for(Map.Entry<String,String> entry:extensionElementPropertity.entrySet()){
                subProcessInstance.variableInstances.put(entry.getKey(),  ConditionUtil.createExpression(entry.getValue(),execution));
            }
        }

        ExecutionEntityImpl subProcessInitialExecution = execution.createChildExecution(subProcessInstance);
        subProcessInitialExecution.setCurrentFlowElement(initialFlowElement);


        Context.getAgenda().planContinueProcessOperation(subProcessInitialExecution);

    }


    @Override
    public void completing(ExecutionEntityImpl execution, ExecutionEntityImpl subProcessInstance) {
        Map<String, Object> variableInstances = subProcessInstance.getVariableInstances();
        execution.findRootParent().variableInstances.put(execution.getCurrentFlowElement().getId(), variableInstances);
    }

    @Override
    public void completed(ExecutionEntityImpl execution) {
        ExecutionEntityImpl executionEntity = execution;
        leave(executionEntity);
    }
}
