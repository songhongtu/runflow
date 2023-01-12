package com.runflow.engine.impl;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.bpmn.entity.ProcessDefinition;
import com.runflow.engine.bpmn.entity.ProcessDefinitionEntity;
import com.runflow.engine.bpmn.entity.ResourceEntity;
import com.runflow.engine.bpmn.entity.impl.ProcessDefinitionCacheEntry;
import com.runflow.engine.bpmn.entity.impl.ResourceEntityImpl;
import com.runflow.engine.context.Context;
import com.runflow.engine.parse.BpmnParse;
import com.runflow.engine.parse.BpmnParser;
import com.runflow.engine.util.io.IoUtil;
import com.runflow.engine.utils.ResourceNameUtil;
import org.activiti.bpmn.model.BpmnModel;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;

public class GeneraImageCommand implements Command<InputStream>, Serializable {

    private String key;

    public GeneraImageCommand(String key) {
        this.key = key;
    }


    @Override
    public InputStream execute(CommandContext commandContext) {
        Context.getProcessEngineConfiguration().scan();
        ProcessEngineConfigurationImpl processEngineConfiguration = commandContext.getProcessEngineConfiguration();
        ProcessDefinitionCacheEntry processDefinitionCacheEntry = processEngineConfiguration.getProcessDefinitionCache().get(key);
        return this.createDiagramForProcessDefinition(processDefinitionCacheEntry.getProcessDefinition(), processDefinitionCacheEntry.getBpmnModel());
    }


    public InputStream createDiagramForProcessDefinition(ProcessDefinition processDefinition, BpmnModel bpmnModel) {

        if (StringUtils.isEmpty(processDefinition.getKey()) || StringUtils.isEmpty(processDefinition.getResourceName())) {
            throw new IllegalStateException("Provided process definition must have both key and resource name set.");
        }
        ProcessEngineConfigurationImpl processEngineConfiguration = Context.getCommandContext().getProcessEngineConfiguration();
        try {
            return processEngineConfiguration.getProcessDiagramGenerator().generateDiagram(bpmnModel, "png", Collections.emptyList(), Collections.emptyList(), "宋体", "宋体", "宋体", processEngineConfiguration.getClassLoader(), 1.0D);
        } catch (Throwable t) { // if anything goes wrong, we don't store the image (the process will still be executable).
            t.printStackTrace();
        }

        return null;
    }


}
