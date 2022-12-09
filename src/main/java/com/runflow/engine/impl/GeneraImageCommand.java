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

import java.io.Serializable;
import java.util.Collections;

public class GeneraImageCommand <T> implements Command<Object>, Serializable {

    private String key;

    public GeneraImageCommand(String key){
        this.key = key;
    }


    @Override
    public Object execute(CommandContext commandContext) {
        ProcessEngineConfigurationImpl processEngineConfiguration = commandContext.getProcessEngineConfiguration();
        ProcessDefinitionCacheEntry processDefinitionCacheEntry = processEngineConfiguration.getProcessDefinitionCache().get(key);
        ResourceEntity diagramForProcessDefinition = this.createDiagramForProcessDefinition(processDefinitionCacheEntry.getProcessDefinition(), processDefinitionCacheEntry.getBpmnModel());

        return null;
    }



    public ResourceEntity createDiagramForProcessDefinition(ProcessDefinition processDefinition, BpmnModel bpmnModel) {

        if (StringUtils.isEmpty(processDefinition.getKey()) || StringUtils.isEmpty(processDefinition.getResourceName())) {
            throw new IllegalStateException("Provided process definition must have both key and resource name set.");
        }

        ResourceEntity resource = new ResourceEntityImpl();
        ProcessEngineConfigurationImpl processEngineConfiguration = Context.getCommandContext().getProcessEngineConfiguration();
        try {
            byte[] diagramBytes = IoUtil.readInputStream(
                    processEngineConfiguration.getProcessDiagramGenerator().generateDiagram(bpmnModel, "png", Collections.emptyList(), Collections.emptyList(), "宋体", "宋体", "宋体", processEngineConfiguration.getClassLoader(), 1.0D), null);
            String diagramResourceName = ResourceNameUtil.getProcessDiagramResourceName(
                    processDefinition.getResourceName(), processDefinition.getKey(), "png");

            resource.setName(diagramResourceName);
            resource.setBytes(diagramBytes);
            resource.setDeploymentId(processDefinition.getDeploymentId());

            // Mark the resource as 'generated'
            resource.setGenerated(true);

        } catch (Throwable t) { // if anything goes wrong, we don't store the image (the process will still be executable).
            t.printStackTrace();
            resource = null;
        }

        return resource;
    }



}
