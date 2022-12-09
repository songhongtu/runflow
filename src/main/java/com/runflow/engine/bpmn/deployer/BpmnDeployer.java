package com.runflow.engine.bpmn.deployer;

import com.runflow.engine.bpmn.entity.DeploymentEntity;
import com.runflow.engine.bpmn.entity.ProcessDefinitionEntity;
import com.runflow.engine.bpmn.entity.ResourceEntity;
import com.runflow.engine.bpmn.entity.impl.DefaultDeploymentCache;
import com.runflow.engine.bpmn.entity.impl.ProcessDefinitionCacheEntry;
import com.runflow.engine.bpmn.entity.impl.ResourceEntityImpl;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.parse.BpmnParse;
import com.runflow.engine.parse.BpmnParser;
import com.runflow.engine.util.io.IoUtil;
import com.runflow.engine.utils.ResourceNameUtil;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class BpmnDeployer {

    private static final Logger log = LoggerFactory.getLogger(BpmnDeployer.class);

    public ParsedDeployment deploy(DeploymentEntity deployment, Map<String, Object> deploymentSettings,BpmnParser bpmnParser) {
        log.debug("Processing deployment {}", deployment.getName());

        // The ParsedDeployment represents the deployment, the process definitions, and the BPMN
        // resource, parse, and model associated with each process definition.
        ParsedDeployment parsedDeployment =build(deployment,deploymentSettings,bpmnParser);

            for (ProcessDefinitionEntity processDefinition : parsedDeployment.getAllProcessDefinitions()) {
                String resourceName = parsedDeployment.getResourceForProcessDefinition(processDefinition).getName();
                processDefinition.setResourceName(resourceName);
            }


        setProcessDefinitionDiagramNames(parsedDeployment);

            updateCaching(parsedDeployment);

return parsedDeployment;
    }


    /**
     * Updates all the process definition entities to have the correct diagram resource name.  Must
     * be called after createAndPersistNewDiagramsAsNeeded to ensure that any newly-created diagrams
     * already have their resources attached to the deployment.
     */
    protected void setProcessDefinitionDiagramNames(ParsedDeployment parsedDeployment) {
        Map<String, ResourceEntity> resources = parsedDeployment.getDeployment().getResources();

        for (ProcessDefinitionEntity processDefinition : parsedDeployment.getAllProcessDefinitions()) {
            String diagramResourceName = ResourceNameUtil.getProcessDiagramResourceNameFromDeployment(processDefinition, resources);
            processDefinition.setDiagramResourceName(diagramResourceName);
        }
    }












    public boolean shouldCreateDiagram(ProcessDefinitionEntity processDefinition, DeploymentEntity deployment) {
        if (deployment.isNew()
                && processDefinition.isGraphicalNotationDefined()) {

            // If the 'getProcessDiagramResourceNameFromDeployment' call returns null, it means
            // no diagram image for the process definition was provided in the deployment resources.
            return ResourceNameUtil.getProcessDiagramResourceNameFromDeployment(processDefinition, deployment.getResources()) == null;
        }

        return false;
    }

    protected BpmnParse createBpmnParseFromResource(ResourceEntity resource, BpmnParser bpmnParser,Map<String, Object> deploymentSettings,DeploymentEntity deploymentEntity) {
        String resourceName = resource.getName();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(resource.getBytes());

        BpmnParse bpmnParse =new BpmnParse(bpmnParser)
                .sourceInputStream(inputStream)
                .setSourceSystemId(resourceName)
                .deployment(deploymentEntity)
                .name(resourceName);
        bpmnParse.execute();
        return bpmnParse;
    }

    public ParsedDeployment build(DeploymentEntity deploymentEntity, Map<String, Object> deploymentSettings,BpmnParser bpmnParser) {
        List<ProcessDefinitionEntity> processDefinitions = new ArrayList<ProcessDefinitionEntity>();
        Map<ProcessDefinitionEntity, BpmnParse> processDefinitionsToBpmnParseMap
                = new LinkedHashMap<ProcessDefinitionEntity, BpmnParse>();
        Map<ProcessDefinitionEntity, ResourceEntity> processDefinitionsToResourceMap
                = new LinkedHashMap<ProcessDefinitionEntity, ResourceEntity>();

        for (ResourceEntity resource : deploymentEntity.getResources().values()) {
            if (isBpmnResource(resource.getName())) {
                log.debug("Processing BPMN resource {}", resource.getName());
                //createProcessDefinitions
                BpmnParse parse = createBpmnParseFromResource(resource,bpmnParser,deploymentSettings,deploymentEntity);
                for (ProcessDefinitionEntity processDefinition : parse.getProcessDefinitions()) {
                    processDefinitions.add(processDefinition);
                    processDefinitionsToBpmnParseMap.put(processDefinition, parse);
                    processDefinitionsToResourceMap.put(processDefinition, resource);
                }
            }
        }

        return new ParsedDeployment(deploymentEntity, processDefinitions,
                processDefinitionsToBpmnParseMap, processDefinitionsToResourceMap);
    }



    public void updateCaching(ParsedDeployment parsedDeployment) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        DefaultDeploymentCache<ProcessDefinitionCacheEntry> processDefinitionCache
                = processEngineConfiguration.getProcessDefinitionCache();
        DeploymentEntity deployment = parsedDeployment.getDeployment();

        for (ProcessDefinitionEntity processDefinition : parsedDeployment.getAllProcessDefinitions()) {
            BpmnModel bpmnModel = parsedDeployment.getBpmnModelForProcessDefinition(processDefinition);
            Process process = parsedDeployment.getProcessModelForProcessDefinition(processDefinition);
            ProcessDefinitionCacheEntry cacheEntry = new ProcessDefinitionCacheEntry(processDefinition, bpmnModel, process);
            processDefinitionCache.add(processDefinition.getKey(), cacheEntry);
            // Add to deployment for further usage
            deployment.addDeployedArtifact(processDefinition);
        }
    }



    protected boolean isBpmnResource(String resourceName) {
        for (String suffix : ResourceNameUtil.BPMN_RESOURCE_SUFFIXES) {
            if (resourceName.endsWith(suffix)) {
                return true;
            }
        }

        return false;
    }











}
