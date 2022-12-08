package com.runflow.engine.bpmn.deployer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.runflow.engine.ActivitiException;
import com.runflow.engine.bpmn.entity.DeploymentEntity;
import com.runflow.engine.bpmn.entity.ProcessDefinitionEntity;
import com.runflow.engine.bpmn.entity.ResourceEntity;
import com.runflow.engine.bpmn.entity.cache.ProcessDefinitionInfoCacheObject;
import com.runflow.engine.bpmn.entity.impl.DefaultDeploymentCache;
import com.runflow.engine.bpmn.entity.impl.ProcessDefinitionCacheEntry;
import com.runflow.engine.bpmn.entity.impl.ResourceEntityImpl;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.parse.BpmnParse;
import com.runflow.engine.parse.BpmnParser;
import com.runflow.engine.util.io.IoUtil;
import com.runflow.engine.utils.ResourceNameUtil;
import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.stream.FileImageOutputStream;
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

            try {
                createAndPersistNewDiagramsIfNeeded(parsedDeployment);
            }catch (Exception e){
                e.printStackTrace();

            }

        setProcessDefinitionDiagramNames(parsedDeployment);

            updateCaching(parsedDeployment);

return parsedDeployment;
    }

    /**
     * Creates new diagrams for process definitions if the deployment is new, the process definition in
     * question supports it, and the engine is configured to make new diagrams.
     *
     * When this method creates a new diagram, it also persists it via the ResourceEntityManager
     * and adds it to the resources of the deployment.
     */
    protected void createAndPersistNewDiagramsIfNeeded(ParsedDeployment parsedDeployment) throws IOException {

        final DeploymentEntity deploymentEntity = parsedDeployment.getDeployment();


        for (ProcessDefinitionEntity processDefinition : parsedDeployment.getAllProcessDefinitions()) {
            if (shouldCreateDiagram(processDefinition, deploymentEntity)) {
                ResourceEntity resource = createDiagramForProcessDefinition(
                        processDefinition, parsedDeployment.getBpmnParseForProcessDefinition(processDefinition));

//                FileImageOutputStream bos = new FileImageOutputStream(new File("C:\\Users\\songhongtu\\Desktop\\"+resource.getName()+".png"));
//                bos.write(resource.getBytes());
//                bos.flush();
//                bos.close();
                if (resource != null) {
                    deploymentEntity.addResource(resource);  // now we'll find it if we look for the diagram name later.
                }
            }
        }
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










    public ResourceEntity createDiagramForProcessDefinition(ProcessDefinitionEntity processDefinition, BpmnParse bpmnParse) {

        if (StringUtils.isEmpty(processDefinition.getKey()) || StringUtils.isEmpty(processDefinition.getResourceName())) {
            throw new IllegalStateException("Provided process definition must have both key and resource name set.");
        }

        ResourceEntity resource = new ResourceEntityImpl();
        ProcessEngineConfigurationImpl processEngineConfiguration = Context.getCommandContext().getProcessEngineConfiguration();
        try {
            byte[] diagramBytes = IoUtil.readInputStream(
                    processEngineConfiguration.getProcessDiagramGenerator().generateDiagram(bpmnParse.getBpmnModel(), "png", Collections.emptyList(), Collections.emptyList(), "宋体", "宋体", "宋体", processEngineConfiguration.getClassLoader(), 1.0D), null);
            String diagramResourceName = ResourceNameUtil.getProcessDiagramResourceName(
                    processDefinition.getResourceName(), processDefinition.getKey(), "png");

            resource.setName(diagramResourceName);
            resource.setBytes(diagramBytes);
            resource.setDeploymentId(processDefinition.getDeploymentId());

            // Mark the resource as 'generated'
            resource.setGenerated(true);

        } catch (Throwable t) { // if anything goes wrong, we don't store the image (the process will still be executable).
            t.printStackTrace();
            log.warn("Error while generating process diagram, image will not be stored in repository", t);
            resource = null;
        }

        return resource;
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
