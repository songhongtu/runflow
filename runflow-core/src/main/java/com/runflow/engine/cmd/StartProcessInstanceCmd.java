package com.runflow.engine.cmd;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.bpmn.entity.impl.DefaultDeploymentCache;
import com.runflow.engine.bpmn.entity.impl.ProcessDefinitionCacheEntry;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.impl.agenda.ContinueProcessOperation;
import com.runflow.engine.utils.BpmnUtils;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        if (leave == null) {
            throw new RuntimeException(key + "找不到该key");
        }
        Process process = leave.getProcess();
        Map<String, FlowElement> flowElementMap = process.getFlowElementMap();
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
        Map<String, String> extensionElementPropertity = BpmnUtils.getExtensionElementPropertity(process.getExtensionElements());


        if (variables == null) {
            variables = new HashMap<>();
        }
        variables.putAll(extensionElementPropertity);
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String key = entry.getKey();
            FlowElement flowElement = flowElementMap.get(key);
            if (flowElement != null) {
                throw new RuntimeException("当前初始化参数" + key + "与" + flowElement.getName() + "冲突");
            } else {
                processInstance.variableInstances.put(key, entry.getValue());
            }

        }
        commandContext.getAllRunTimeExecution().putSingle(processInstance);
        startProcessInstance(processInstance, Context.getCommandContext());
        return processInstance;
    }


    public void startProcessInstance(ExecutionEntityImpl processInstance, CommandContext commandContext) {
        commandContext.setSerialNumber(processInstance.getSerialNumber());
        ExecutionEntityImpl execution = processInstance.getExecutions().get(0); // There will always be one child execution created
        commandContext.getAgenda().planContinueProcessOperation(execution);
    }


}
