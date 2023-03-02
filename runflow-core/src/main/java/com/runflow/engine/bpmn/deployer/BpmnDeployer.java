package com.runflow.engine.bpmn.deployer;

import com.runflow.engine.bpmn.entity.DeploymentEntity;
import com.runflow.engine.bpmn.entity.ProcessDefinitionEntity;
import com.runflow.engine.bpmn.entity.ResourceEntity;
import com.runflow.engine.bpmn.entity.impl.DefaultDeploymentCache;
import com.runflow.engine.bpmn.entity.impl.ProcessDefinitionCacheEntry;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.parse.BpmnParse;
import com.runflow.engine.parse.BpmnParser;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class BpmnDeployer {

    private static final Logger log = LoggerFactory.getLogger(BpmnDeployer.class);

    public ParsedDeployment deploy(DeploymentEntity deployment, BpmnParser bpmnParser) {
        log.debug("Processing deployment {}", deployment.getName());
        ParsedDeployment parsedDeployment = build(deployment, bpmnParser);
        for (ProcessDefinitionEntity processDefinition : parsedDeployment.getAllProcessDefinitions()) {
            String resourceName = parsedDeployment.getResourceForProcessDefinition(processDefinition).getName();
            processDefinition.setResourceName(resourceName);
        }
        updateCaching(parsedDeployment);
        return parsedDeployment;
    }


    protected BpmnParse createBpmnParseFromResource(ResourceEntity resource, BpmnParser bpmnParser, DeploymentEntity deploymentEntity) {
        String resourceName = resource.getName();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(resource.getBytes());

        ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        BpmnXMLConverter bpmnXMLConverter = processEngineConfiguration.getBpmnXMLConverter();
        BpmnParse bpmnParse = new BpmnParse(bpmnParser)
                .sourceInputStream(inputStream)
                .setSourceSystemId(resourceName)
                .deployment(deploymentEntity)
                .bpmnXmlConverter(bpmnXMLConverter)
                .name(resourceName);
        bpmnParse.execute();
        return bpmnParse;
    }

    public ParsedDeployment build(DeploymentEntity deploymentEntity, BpmnParser bpmnParser) {
        List<ProcessDefinitionEntity> processDefinitions = new ArrayList<>();
        Map<ProcessDefinitionEntity, BpmnParse> processDefinitionsToBpmnParseMap
                = new LinkedHashMap<>();
        Map<ProcessDefinitionEntity, ResourceEntity> processDefinitionsToResourceMap
                = new LinkedHashMap<>();

        for (ResourceEntity resource : deploymentEntity.getResources().values()) {
            //createProcessDefinitions
            BpmnParse parse = createBpmnParseFromResource(resource, bpmnParser, deploymentEntity);
            for (ProcessDefinitionEntity processDefinition : parse.getProcessDefinitions()) {
                processDefinitions.add(processDefinition);
                processDefinitionsToBpmnParseMap.put(processDefinition, parse);
                processDefinitionsToResourceMap.put(processDefinition, resource);
            }
        }

        return new ParsedDeployment(deploymentEntity, processDefinitions,
                processDefinitionsToBpmnParseMap, processDefinitionsToResourceMap);
    }


    public void updateCaching(ParsedDeployment parsedDeployment) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        DefaultDeploymentCache<ProcessDefinitionCacheEntry> processDefinitionCache
                = processEngineConfiguration.getProcessDefinitionCache();

        for (ProcessDefinitionEntity processDefinition : parsedDeployment.getAllProcessDefinitions()) {
            BpmnModel bpmnModel = parsedDeployment.getBpmnModelForProcessDefinition(processDefinition);
            Process process = parsedDeployment.getProcessModelForProcessDefinition(processDefinition);
            ProcessDefinitionCacheEntry cacheEntry = new ProcessDefinitionCacheEntry(processDefinition, bpmnModel, process);
            processDefinitionCache.add(processDefinition.getKey(), cacheEntry);
        }
    }


}
