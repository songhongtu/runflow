package com.runflow.engine.cmd;

import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.bpmn.entity.IdentityLinkEntity;
import com.runflow.engine.bpmn.entity.TaskEntity;
import com.runflow.engine.bpmn.entity.impl.DefaultDeploymentCache;
import com.runflow.engine.bpmn.entity.impl.ProcessDefinitionCacheEntry;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.impl.agenda.TakeOutgoingSequenceFlowsOperation;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.StartEvent;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

public class StartProcessInstanceCmd<T> implements Command<ExecutionEntityImpl>, Serializable {
    private String key;
    protected Map<String, Object> variables;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TakeOutgoingSequenceFlowsOperation.class);

    public StartProcessInstanceCmd(String key, Map<String, Object> variables) {
        this.key = key;
        this.variables = variables;
    }

    @Override
    public ExecutionEntityImpl execute(CommandContext commandContext) {
        DefaultDeploymentCache<ProcessDefinitionCacheEntry> processDefinitionCache = commandContext.getProcessEngineConfiguration().getProcessDefinitionCache();
        ProcessDefinitionCacheEntry leave = processDefinitionCache.get(key);
        Process process = leave.getProcess();
        FlowElement initialFlowElement = process.getInitialFlowElement();

        // Create the process instance
        String initiatorVariableName = null;
        if (initialFlowElement instanceof StartEvent) {
            initiatorVariableName = ((StartEvent) initialFlowElement).getInitiator();
        }


        String uuid = UUID.randomUUID().toString();
        ExecutionEntityImpl processInstance = new ExecutionEntityImpl();
        processInstance.setMainThread(Thread.currentThread());
        processInstance.setSerialNumber(uuid);
        processInstance.setId(processInstance.randomID());

//        logger.info("线程名称："+Thread.currentThread().getName()+"："+"创建父节点:{}", processInstance.getId());

        processInstance.executions = new ArrayList<ExecutionEntityImpl>(1);
        processInstance.tasks = new ArrayList<TaskEntity>(1);
        processInstance.variableInstances = new HashMap<String, Object>(1);
        processInstance.identityLinks = new ArrayList<IdentityLinkEntity>(1);
        ExecutionEntityImpl execution = processInstance.createChildExecution(processInstance);
        processInstance.setScope(true);
        execution.setCurrentFlowElement(initialFlowElement);
        startProcessInstance(processInstance, Context.getCommandContext());

        if (variables != null) {
            for (String key : variables.keySet()) {
                processInstance.setVariableInstances(key, variables.get(key));
            }
        }

        commandContext.getDefaultSession().putSingle(processInstance);


        return processInstance;
    }


    public void startProcessInstance(ExecutionEntityImpl processInstance, CommandContext commandContext) {
        commandContext.setSerialNumber(processInstance.getSerialNumber());
        ExecutionEntity execution = processInstance.getExecutions().get(0); // There will always be one child execution created
        commandContext.getAgenda().planContinueProcessOperation(execution);
    }


}
