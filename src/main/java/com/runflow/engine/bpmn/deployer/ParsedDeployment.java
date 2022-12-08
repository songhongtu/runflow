package com.runflow.engine.bpmn.deployer;

import com.runflow.engine.bpmn.entity.DeploymentEntity;
import com.runflow.engine.bpmn.entity.ProcessDefinitionEntity;
import com.runflow.engine.bpmn.entity.ResourceEntity;
import com.runflow.engine.parse.BpmnParse;
import com.runflow.engine.parse.BpmnParser;
import com.runflow.engine.utils.ResourceNameUtil;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParsedDeployment {
    private static final Logger log = LoggerFactory.getLogger(ParsedDeployment.class);
    public static final String[] BPMN_RESOURCE_SUFFIXES = new String[]{"bpmn20.xml", "bpmn"};
    public static final String[] DIAGRAM_SUFFIXES = new String[]{"png", "jpg", "gif", "svg"};
    protected DeploymentEntity deploymentEntity;
    protected BpmnParse bpmnParse;
    protected List<ProcessDefinitionEntity> processDefinitions;
    protected Map<ProcessDefinitionEntity, BpmnParse> mapProcessDefinitionsToParses;
    protected Map<ProcessDefinitionEntity, ResourceEntity> mapProcessDefinitionsToResources;

    public ParsedDeployment(
            DeploymentEntity entity, List<ProcessDefinitionEntity> processDefinitions,
            Map<ProcessDefinitionEntity, BpmnParse> mapProcessDefinitionsToParses,
            Map<ProcessDefinitionEntity, ResourceEntity> mapProcessDefinitionsToResources) {
        this.deploymentEntity = entity;
        this.processDefinitions = processDefinitions;
        this.mapProcessDefinitionsToParses = mapProcessDefinitionsToParses;
        this.mapProcessDefinitionsToResources = mapProcessDefinitionsToResources;
    }


    public DeploymentEntity getDeployment() {
        return deploymentEntity;
    }

    public List<ProcessDefinitionEntity> getAllProcessDefinitions() {
        return processDefinitions;
    }

    public ResourceEntity getResourceForProcessDefinition(ProcessDefinitionEntity processDefinition) {
        return mapProcessDefinitionsToResources.get(processDefinition);
    }

    public BpmnParse getBpmnParseForProcessDefinition(ProcessDefinitionEntity processDefinition) {
        return mapProcessDefinitionsToParses.get(processDefinition);
    }

    public BpmnModel getBpmnModelForProcessDefinition(ProcessDefinitionEntity processDefinition) {
        BpmnParse parse = getBpmnParseForProcessDefinition(processDefinition);

        return (parse == null ? null : parse.getBpmnModel());
    }

    public Process getProcessModelForProcessDefinition(ProcessDefinitionEntity processDefinition) {
        BpmnModel model = getBpmnModelForProcessDefinition(processDefinition);

        return (model == null ? null : model.getProcessById(processDefinition.getKey()));
    }

    public Map<ProcessDefinitionEntity, BpmnParse> getMapProcessDefinitionsToParses() {
        return mapProcessDefinitionsToParses;
    }

    public void setMapProcessDefinitionsToParses(Map<ProcessDefinitionEntity, BpmnParse> mapProcessDefinitionsToParses) {
        this.mapProcessDefinitionsToParses = mapProcessDefinitionsToParses;
    }

    public Map<ProcessDefinitionEntity, ResourceEntity> getMapProcessDefinitionsToResources() {
        return mapProcessDefinitionsToResources;
    }

    public void setMapProcessDefinitionsToResources(Map<ProcessDefinitionEntity, ResourceEntity> mapProcessDefinitionsToResources) {
        this.mapProcessDefinitionsToResources = mapProcessDefinitionsToResources;
    }

    protected boolean isBpmnResource(String resourceName) {
        for (String suffix : ResourceNameUtil.BPMN_RESOURCE_SUFFIXES) {
            if (resourceName.endsWith(suffix)) {
                return true;
            }
        }

        return false;
    }

    public BpmnParse getBpmnParse() {
        return bpmnParse;
    }

    public void setBpmnParse(BpmnParse bpmnParse) {
        this.bpmnParse = bpmnParse;
    }

    public ProcessDefinitionEntity getProcessDefinition(String processDefinitionKey) {
        for (ProcessDefinitionEntity processDefinition : processDefinitions) {
            if (processDefinition.getKey().equals(processDefinitionKey)) {
                return processDefinition;
            }
        }
        return null;
    }

//    protected BpmnParse createBpmnParseFromResource(ResourceEntity resource) {
//        String resourceName = resource.getName();
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(resource.getBytes());
//        BpmnParse bpmnParse = new BpmnParse()
//                .sourceInputStream(inputStream)
//                .setSourceSystemId(resourceName)
//                .deployment(deploymentEntity)
//                .name(resourceName);
//        try {
//            bpmnParse.execute();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return bpmnParse;
//    }


}
