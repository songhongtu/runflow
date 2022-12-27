package com.runflow.engine.cmd;

import com.runflow.engine.bpmn.deployer.ParsedDeployment;
import com.runflow.engine.bpmn.entity.Deployment;
import com.runflow.engine.bpmn.entity.DeploymentEntity;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.impl.DeploymentBuilderImpl;

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
        Map<String, Object> deploymentSettings = new HashMap<String, Object>();
        deploymentSettings.put(DeploymentSettings.IS_BPMN20_XSD_VALIDATION_ENABLED, deploymentBuilder.isBpmn20XsdValidationEnabled());
        deploymentSettings.put(DeploymentSettings.IS_PROCESS_VALIDATION_ENABLED, deploymentBuilder.isProcessValidationEnabled());
        // Actually deploy
        ParsedDeployment deploy = commandContext.getProcessEngineConfiguration().getBpmnDeployer().deploy(deployment, deploymentSettings, commandContext.getProcessEngineConfiguration().getBpmnParser());
        deployment.setParsedDeployment(deploy);
        return deployment;
    }





}
