package com.runflow.engine.cmd;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.bpmn.entity.impl.DefaultDeploymentCache;
import com.runflow.engine.bpmn.entity.impl.ProcessDefinitionCacheEntry;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandContext;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StartProcessInstanceCmd implements Command<ExecutionEntityImpl> {
    private String key;
    protected Map<String, Object> variables;


    public StartProcessInstanceCmd(String key, Map<String, Object> variables) {
        this.key = key;
        this.variables = variables;
    }

    @Override
    public ExecutionEntityImpl execute(CommandContext commandContext) {
        Context.getProcessEngineConfiguration().scan();
        DefaultDeploymentCache<ProcessDefinitionCacheEntry> processDefinitionCache = commandContext.getProcessEngineConfiguration().getProcessDefinitionCache();
        ProcessDefinitionCacheEntry leave = processDefinitionCache.get(key);
        Process process = leave.getProcess();
        FlowElement initialFlowElement = process.getInitialFlowElement();


        String uuid = UUID.randomUUID().toString();
        ExecutionEntityImpl processInstance = new ExecutionEntityImpl();
        processInstance.setMainThread(Thread.currentThread());
        processInstance.setSerialNumber(uuid);
        processInstance.setId(processInstance.randomID());

//        logger.info("线程名称："+Thread.currentThread().getName()+"："+"创建父节点:{}", processInstance.getId());

        processInstance.executions = new ArrayList<>();
        processInstance.variableInstances = new ConcurrentHashMap<>();
        ExecutionEntityImpl execution = processInstance.createChildExecution(processInstance);
        processInstance.setScope(true);
        execution.setCurrentFlowElement(initialFlowElement);
        if (variables != null) {
            for (Map.Entry<String,Object> entry : variables.entrySet()) {
                processInstance.variableInstances.put(entry.getKey(),entry.getValue());
            }


        }
        commandContext.getDefaultSession().putSingle(processInstance);
        startProcessInstance(processInstance, Context.getCommandContext());
        return processInstance;
    }


    public void startProcessInstance(ExecutionEntityImpl processInstance, CommandContext commandContext) {
        commandContext.setSerialNumber(processInstance.getSerialNumber());
        ExecutionEntityImpl execution = processInstance.getExecutions().get(0); // There will always be one child execution created
        commandContext.getAgenda().planContinueProcessOperation(execution);
    }


}
