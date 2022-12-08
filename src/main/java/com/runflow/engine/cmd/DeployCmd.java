package com.runflow.engine.cmd;

import com.runflow.engine.ActivitiException;
import com.runflow.engine.bpmn.deployer.ParsedDeployment;
import com.runflow.engine.bpmn.entity.Deployment;
import com.runflow.engine.bpmn.entity.DeploymentEntity;
import com.runflow.engine.bpmn.entity.ProcessDefinitionEntity;
import com.runflow.engine.bpmn.entity.ResourceEntity;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.impl.DeploymentBuilderImpl;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;

import java.io.Serializable;
import java.util.*;

public class  DeployCmd<T> implements Command<Deployment>, Serializable {

    private static final long serialVersionUID = 1L;
    protected DeploymentBuilderImpl deploymentBuilder;

    public DeployCmd(DeploymentBuilderImpl deploymentBuilder) {
        this.deploymentBuilder = deploymentBuilder;
    }

    public Deployment execute(CommandContext commandContext) {


        return executeDeploy(commandContext);
    }

    protected Deployment executeDeploy(CommandContext commandContext) {
        DeploymentEntity deployment = deploymentBuilder.getDeployment();

        deployment.setDeploymentTime(new Date());


        deployment.setNew(true);

        // Save the data

        //todo
//        commandContext.getDeploymentEntityManager().insert(deployment);

//        if (commandContext.getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
//            commandContext.getProcessEngineConfiguration().getEventDispatcher().dispatchEvent(ActivitiEventBuilder.createEntityEvent(ActivitiEventType.ENTITY_CREATED, deployment));
//        }

        // Deployment settings
        Map<String, Object> deploymentSettings = new HashMap<String, Object>();
        deploymentSettings.put(DeploymentSettings.IS_BPMN20_XSD_VALIDATION_ENABLED, deploymentBuilder.isBpmn20XsdValidationEnabled());
        deploymentSettings.put(DeploymentSettings.IS_PROCESS_VALIDATION_ENABLED, deploymentBuilder.isProcessValidationEnabled());

        // Actually deploy
        ParsedDeployment deploy = commandContext.getProcessEngineConfiguration().getBpmnDeployer().deploy(deployment, deploymentSettings, commandContext.getProcessEngineConfiguration().getBpmnParser());

        if (deploymentBuilder.getProcessDefinitionsActivationDate() != null) {
            scheduleProcessDefinitionActivation(commandContext, deployment);
        }

//        if (commandContext.getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
//            commandContext.getProcessEngineConfiguration().getEventDispatcher().dispatchEvent(ActivitiEventBuilder.createEntityEvent(ActivitiEventType.ENTITY_INITIALIZED, deployment));
//        }
        deployment.setParsedDeployment(deploy);
        return deployment;
    }


    protected boolean deploymentsDiffer(DeploymentEntity deployment, DeploymentEntity saved) {

        if (deployment.getResources() == null || saved.getResources() == null) {
            return true;
        }

        Map<String, ResourceEntity> resources = deployment.getResources();
        Map<String, ResourceEntity> savedResources = saved.getResources();

        for (String resourceName : resources.keySet()) {
            ResourceEntity savedResource = savedResources.get(resourceName);

            if (savedResource == null)
                return true;

            if (!savedResource.isGenerated()) {
                ResourceEntity resource = resources.get(resourceName);

                byte[] bytes = resource.getBytes();
                byte[] savedBytes = savedResource.getBytes();
                if (!Arrays.equals(bytes, savedBytes)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void scheduleProcessDefinitionActivation(CommandContext commandContext, DeploymentEntity deployment) {
            throw new ActivitiException("不支持");
    }


}
